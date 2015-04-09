package uk.ac.cam.josi2.DataStructures;

/**
 * Created by Joe on 08/04/2015.
 */
public class FibonacciHeap<T extends Comparable<T>> {
    private FHeapNode mMin;

    private int mNumOfNodes;

    public FibonacciHeap(){
        mMin = null;
        mNumOfNodes = 0;
    }

    public void insert(T key){
        FHeapNode newNode = new FHeapNode(key);
        if(mMin == null) {
            mMin = newNode;
        }
        else{
            mMin.insertIntoChildList(newNode);
            if(mMin.mKey.compareTo(newNode.mKey) > 0){
                mMin = newNode;
            }
        }
        mNumOfNodes++;
    }


    private class FHeapNode {
        FHeapNode mParent, mChild, mLeft, mRight;
        T mKey;
        boolean mMarked;

        int mDegree;

        public FHeapNode(T key){
            mMarked = false;
            mDegree = 0;
            mLeft = this;
            mRight = this;
            mParent = null;
            mChild = null;
            mKey = key;
        }

        public void insertIntoChildList(FHeapNode child){
            child.mLeft = this;
            child.mRight = mRight;
            child.mRight.mLeft = child;
            child.mLeft.mRight = child;
        }

    }
}
