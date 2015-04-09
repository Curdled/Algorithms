package uk.ac.cam.josi2.DataStructures;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joe on 08/04/2015.
 */
public class FibonacciHeap<T extends Comparable<T>, U> {
    private FHeapNode mMin;

    private int mNumOfNodes;

    //p(H) = t(H) + 2 m(H)
    //p is the potential
    //t is the number of trees
    //m is the number of marks
    public FibonacciHeap(){
        mMin = null;
        mNumOfNodes = 0;
    }

    //no affect to potential O(1)
    public U minimum(){
        return mMin.mData;
    }

    //actual cost O(1)
    //=p(H) - p(H1) - p(H2)
    //= (t(H) + 2 m(H))-(t(H1) +2 m(H1) + t(H2) + 2m(H2))
    //0
    //amortized cost O(1)
    //potential unchanged.
    public static FibonacciHeap union(FibonacciHeap heap1, FibonacciHeap heap2){
        FibonacciHeap heap = new FibonacciHeap();
        if(heap1.mMin != null) {
            heap.mMin = heap1.mMin;
            heap.mMin.mergeList(heap2.mMin);
            if(heap1.mMin.mKey.compareTo(heap2.mMin.mKey) > 0){
                heap.mMin = heap2.mMin;
            }
        }
        else if(heap2.mMin != null)
            heap.mMin = heap2.mMin;
        heap.mNumOfNodes = heap1.mNumOfNodes + heap2.mNumOfNodes;

        //invalidate both old FibonacciHeaps.
        heap1.mMin = null;
        heap2.mMin = null;
        return heap;
    }

    //p(H') - p(H) = ((t(H) + 1) + 2 m(h)) - (t(H) + 2m(H)) = 1
    //actual cost O(1), amortized cost 1+O(1) = O(1)
    public void insert(T key, U data){
        FHeapNode newNode = new FHeapNode(key, data);
        if(mMin == null) {
            mMin = newNode;
        }
        else{
            mMin.mergeList(newNode);
            if(mMin.mKey.compareTo(newNode.mKey) > 0){
                mMin = newNode;
            }
        }
        mNumOfNodes++;
    }

    public U extractMin(){
        FHeapNode oldMin = mMin;
        if(oldMin != null){
            if(oldMin.mChild != null) {
                FHeapNode child = oldMin.mChild;
                child.mParent = null;
                FHeapNode node = child.mRight;
                while (node != child) {
                    node.mParent = null;
                    node = node.mRight;
                }
                oldMin.mergeList(child);
            }
            if(oldMin.mRight == oldMin) {//only one element in the heap.
                mMin = null;
            }
            else{
                mMin = oldMin.mRight;
                oldMin.removeFromList();
                consolidate();
            }
            mNumOfNodes--;
        }
        return oldMin.mData;
    }

    private void consolidate() {
        List<FHeapNode> nodesVisited = new LinkedList<>();
        List<FHeapNode> nodesToCheck = new LinkedList<>();

        FHeapNode firstVisited = mMin;
        nodesToCheck.add(mMin);
        FHeapNode node = mMin.mRight;
        while(firstVisited != node){
            nodesToCheck.add(node);
            node = node.mRight;
        }
        for (int i = 0; i < nodesToCheck.size()+1; i++) {
            nodesVisited.add(null);
        }
        for (int i = 0; i != nodesToCheck.size(); i++) {
            FHeapNode x = nodesToCheck.get(i);
            int d = x.mDegree;
            while(nodesVisited.get(d) != null && !nodesVisited.get(d).equals(x)){
                FHeapNode y = nodesVisited.get(d);
                if(x.mKey.compareTo(y.mKey) > 0){
                    FHeapNode tmp = x;
                    x = y;
                    y = tmp;
                }
                y.heapLink(x);
                nodesVisited.set(d,null);
                d++;
            }
            nodesVisited.set(d, x);
        }
        mMin = null;
        for (int i = 0; i != nodesVisited.size(); i++) {
            FHeapNode currentNode = nodesVisited.get(i);

            if (nodesVisited.get(i) != null){
                currentNode.removeFromList();
                if(mMin == null){
                    currentNode.mLeft = currentNode;
                    currentNode.mRight = currentNode;
                    mMin = currentNode;
                }
                else{
                    mMin.mergeList(currentNode);
                    if(mMin.mKey.compareTo(currentNode.mKey) > 0){
                        mMin = currentNode;
                    }
                }
            }
        }
    }

    private class FHeapNode {
        FHeapNode mParent, mChild, mLeft, mRight;
        T mKey;
        U mData;
        boolean mMarked;

        int mDegree;

        private FHeapNode(T key, U data){
            mMarked = false;
            mDegree = 0;
            mLeft = this;
            mRight = this;
            mParent = null;
            mChild = null;
            mKey = key;
            mData = data;
        }


        private void mergeList(FHeapNode other){//merges two linked lists.
            FHeapNode tmp = mRight;
            FHeapNode tmp2 = other.mLeft;
            mRight = other;
            tmp.mLeft = other.mLeft;

            other.mLeft = this;
            tmp2.mRight = tmp;

        }

        private void removeFromList(){
            FHeapNode tmp = mRight;
            mLeft.mRight = mRight;
            mRight.mLeft = mLeft;

            mLeft = this;
            mRight = this;
        }

        public void heapLink(FHeapNode x) {
            removeFromList();
            if(x.mChild != null) {
                x.mChild.mergeList(this);
            }
            else{
                x.mChild = this;
            }
            x.mDegree++;
            mMarked = false;
        }
    }
}
