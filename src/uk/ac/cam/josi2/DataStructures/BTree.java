package uk.ac.cam.josi2.DataStructures;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joe on 07/04/2015.
 */
public class BTree<T extends Comparable<T>,U> {
    private BTreeNode mRoot;

    private final int mMinDegree;

    public BTree(int minDegree){

        mMinDegree = minDegree;
        mRoot = new BTreeNode();
    }

    public U search(T key){
        BTreeNode node = mRoot;
        while(true){
            int i = 0;
            //Find the correct node with respect to the key value.
            for (; i != node.mKeys.size(); i++) {
                KeyDataPair kd = node.mKeys.get(i);
                int val = kd.compareTo(key);
                if(val == 0) {
                    return kd.mData;
                }
                else if(val > 0){
                    if(node.mLeaf)
                        return null;
                    node = node.mPointers.get(i);
                    break;
                }
            }
            if(!node.mLeaf && i == node.mKeys.size())
                node = node.mPointers.get(node.mPointers.size()-1);
        }
    }


    public void insert(T key, U value){
        BTreeNode node = mRoot;
        KeyDataPair p = new KeyDataPair(key, value);
        if (node.mKeys.size() == 2*mMinDegree - 1){
            BTreeNode s = new BTreeNode();
            mRoot = s;
            s.mLeaf = false;
            s.mPointers.add(0,node);
            s.split(0);
            insertNotFull(s, p);
        }
        else
            insertNotFull(node, p);
    }


    public void delete2(T key){
        BTreeNode node = mRoot;

    }

    public void delete(T key){
        deleteFromIndex(key, mRoot, null);
    }

    private void deleteFromIndex(T key, BTreeNode subRoot , BTreeNode subRootParent){
        BTreeNode node = subRoot;
        BTreeNode parent = subRootParent;
        int parentIndex = -1;//this will only be -1 if the key to be deleted is in the root.
        while(node != null) {
            int j = 0;
            for (int i = 0; i != node.mKeys.size(); i++) {
                j = i;
                KeyDataPair kd = node.mKeys.get(i);
                int val = kd.compareTo(key);
                if(val == 0) {
                    deleteNode(node, parent, i, parentIndex);
                    return;
                }
                else if(val > 0 && !node.mLeaf){
                    parent = node;
                    node = node.mPointers.get(i);
                    if(node.mKeys.size() < mMinDegree){
                        parent.fixChild(node, i);
                    }
                    parentIndex = i;
                    break;
                    //go down the left side of the node.
                }
            }
            j++;
            //check all go down the right most side.
            if(!node.mLeaf && j == node.mKeys.size() && !node.mLeaf) {
                parent = node;
                parentIndex = node.mPointers.size() - 1;
                node = node.mPointers.get(node.mPointers.size() - 1);
                if(node.mKeys.size() < mMinDegree){
                    parent.fixChild(node, node.mPointers.size() - 1);
                }
            }
        }
    }

    public void deleteNode(BTreeNode node, BTreeNode parent, int index, int parentIndex){
        T keyToDelete = node.mKeys.get(index).mKey;
        if(node.equals(mRoot)){
            if(node.mLeaf) {
                node.mKeys.remove(index);
            }
            else {//node has children
                if(node.mKeys.size() == 1){
                    node.merge(0);
                    delete(keyToDelete);
                }
                else{//internal node.
                    if(node.mPointers.get(index).mKeys.size() > mMinDegree)
                        node.mKeys.set(index, node.findPredecessorAndDelete(index));
                    else if(node.mPointers.get(index+1).mKeys.size() > mMinDegree){
                        node.mKeys.set(index, node.findSuccessorAndDelete(index));
                    }
                    else{
                        node.merge(index);
                        deleteFromIndex(keyToDelete, node, parent);
                    }
                }
            }

        }
        else if(node.mLeaf){//leaf.
            node.mKeys.remove(index);
            if(node.mKeys.size() < mMinDegree){
                parent.fixChild(node, index);
            }
        }
    }


    private void insertNotFull(BTreeNode node, KeyDataPair keyDataPair) {

        if(node.mLeaf) {
            int i = 0;
            for (; i != node.mKeys.size(); i++) {
                if(node.mKeys.get(i).compareTo(keyDataPair) >= 0){
                    break;
                }

            }
            node.mKeys.add(i, keyDataPair);
        }
        else {
            int i = 0;
            for (; i != node.mKeys.size(); i++) {
                if(node.mKeys.get(i).compareTo(keyDataPair) >= 0){
                    break;
                }
            }
            if(node.mPointers.get(i).mKeys.size() == 2*mMinDegree - 1){
                node.split(i);
                if(node.mKeys.get(i).compareTo(keyDataPair) < 0)
                    i++;

            }
            insertNotFull(node.mPointers.get(i), keyDataPair);
        }


    }

    private class BTreeNode{
        private boolean mLeaf;
        private List<KeyDataPair> mKeys;
        private List<BTreeNode> mPointers;

        public BTreeNode(){
            mLeaf = true;
            mKeys = new LinkedList<>();
            mPointers = new LinkedList<>();
        }

        public void fixChild(BTreeNode child, int index){
            //search the sibling(s) of the child node and move from either side to the
            //new node, if both side don't have enough nodes to donate a node
            //merge with one of the nodes.
            BTreeNode leftSide = null;
            BTreeNode rightSide = null;
            if(index  >= 0)
                leftSide = mPointers.get(index);

            if(index < mKeys.size())
                rightSide = mPointers.get(index+1);

            if(leftSide != null && mMinDegree < leftSide.mKeys.size()) {//get the sibling left of the child.
                leftRotate(index);
                return;
            }
            else if(rightSide != null && mMinDegree < rightSide.mKeys.size()){//get the sibling right of the child.
                rightRotate(index);
                return;
            }
            else{//must merge with a sibling.
                merge(index);
            }

        }

        public void split(int ith){
            BTreeNode rightNode = new BTreeNode();
            BTreeNode leftNode = this.mPointers.get(ith);
            rightNode.mLeaf = leftNode.mLeaf;

            //moves all the keys that are the right side of ith to
            //the newly created rightNode.
            for (int i = 0; i != mMinDegree-1; i++) {
                rightNode.mKeys.add(leftNode.mKeys.get(i+mMinDegree));
            }

            if (!leftNode.mLeaf){
                for (int i = 0; i != mMinDegree; i++) {
                    rightNode.mPointers.add(leftNode.mPointers.get(i+mMinDegree));
                }
            }
            mPointers.add(ith + 1, rightNode);
            mKeys.add(ith, leftNode.mKeys.get(mMinDegree - 1));
            for (int i = leftNode.mKeys.size(); i > mMinDegree-1; i--) {
                leftNode.mKeys.remove(i-1);
            }
            for (int i = leftNode.mPointers.size(); i > mMinDegree; i--) {
                leftNode.mPointers.remove(i-1);
            }
        }

        public void merge(int index){
            if(index >= this.mPointers.size() || index < 0)
                return;

            BTreeNode leftSide = mPointers.get(index);
            BTreeNode rightSide = mPointers.get(index+1);

            mPointers.remove(index + 1);
            leftSide.mKeys.add(mKeys.get(index));
            for (int i = 0; i != rightSide.mKeys.size(); i++) {
                leftSide.mKeys.add(rightSide.mKeys.get(i));
            }
            for (int i  = 0; i != rightSide.mPointers.size(); i++) {
                leftSide.mPointers.add(rightSide.mPointers.get(i));
            }
            mKeys.remove(index);
            if(this.equals(mRoot)) {
                if (mKeys.size() == 0) {
                    mRoot = leftSide;
                }
            }
        }

        public void leftRotate(int index){
            if(index >= this.mPointers.size() || index < 0)
                return;
            BTreeNode leftSide = mPointers.get(index);
            BTreeNode rightSide = mPointers.get(index+1);

            rightSide.mKeys.add(0, mKeys.get(index));
            mKeys.remove(index);
            if(leftSide.mKeys.size() == 0) {
                mKeys.add(index, leftSide.mKeys.get(0));
                leftSide.mKeys.remove(0);
            }
            else {
                mKeys.add(index, leftSide.mKeys.get(leftSide.mKeys.size() - 1));
                leftSide.mKeys.remove(leftSide.mKeys.size() - 1);
            }
        }

        public void rightRotate(int index){
            if(index >= this.mPointers.size() || index < 0)
                return;
            BTreeNode leftSide = mPointers.get(index);
            BTreeNode rightSide = mPointers.get(index+1);
            if(leftSide.mKeys.size() == 0)
                leftSide.mKeys.add(0, mKeys.get(index));
            else
                leftSide.mKeys.add(mKeys.get(index));
            mKeys.remove(index);
            mKeys.add(index, rightSide.mKeys.get(0));
            rightSide.mKeys.remove(0);
        }

        public KeyDataPair findPredecessorAndDelete(int index) {
            BTreeNode subTree = mPointers.get(index);
            while(!subTree.mLeaf) {
                int childIndex = mPointers.size() - 1;
                BTreeNode child = subTree.mPointers.get(childIndex);
                if (child.mKeys.size() < mMinDegree) {
                    subTree.fixChild(child, childIndex);
                }
                subTree = child;
            }
            KeyDataPair ret = subTree.mKeys.get(subTree.mKeys.size()-1);
            subTree.mKeys.remove(subTree.mKeys.size()-1);
            return ret;
        }

        public KeyDataPair findSuccessorAndDelete(int index) {
            BTreeNode subTree = mPointers.get(index);
            while(!subTree.mLeaf) {
                BTreeNode child = subTree.mPointers.get(0);
                if (child.mKeys.size() < mMinDegree) {
                    subTree.fixChild(child, 0);
                }
                subTree = child;
            }
            KeyDataPair ret = subTree.mKeys.get(subTree.mKeys.size()-1);
            subTree.mKeys.remove(subTree.mKeys.size()-1);
            return ret;
        }
    }

    private class KeyDataPair implements Comparable<KeyDataPair>{

        private T mKey;
        private U mData;

        public KeyDataPair(T key, U value){
            mKey = key;
            mData = value;
        }

        @Override
        public int compareTo(KeyDataPair o) {
            return mKey.compareTo(o.mKey);
        }

        public int compareTo(T key) {
            return mKey.compareTo(key);
        }
    }
}
