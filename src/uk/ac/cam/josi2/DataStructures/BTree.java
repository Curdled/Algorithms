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
        BTreeNode newRoot = mRoot;
        KeyDataPair p = new KeyDataPair(key, value);
        if (newRoot.mKeys.size() == 2*mMinDegree - 1){
            BTreeNode s = new BTreeNode();
            mRoot = s;
            s.mLeaf = false;
            s.mPointers.add(0,newRoot);
            s.split(0);
            insertNotFull(s, p);
        }
        else
            insertNotFull(newRoot, p);
    }

    public void delete(T key){
        BTreeNode node = mRoot;
        BTreeNode parent = null;
        int i = 0;
        while(node != null) {
            for (; i != node.mKeys.size(); i++) {
                KeyDataPair kd = node.mKeys.get(i);
                int val = kd.compareTo(key);
                if(val == 0) {
                    deleteNode(node, parent, i);
                }
                else if(val > 0){
                    parent = node;
                    //go down the left side of the node.
                }
            }
            //check all go down the right most side.
            if(!node.mLeaf && i == node.mKeys.size()) {
                parent = node;
                node = node.mPointers.get(node.mPointers.size() - 1);
            }
        }
    }

    public void deleteNode(BTreeNode node, BTreeNode parent, int index){
        if(node.equals(mRoot)){//root node.

        }
        else if(node.mLeaf){//leaf.
            if(node.mKeys.size() >= mMinDegree){
                node.mKeys.remove(index);
            }
            else{
                parent.fixChild(node, index);
            }
        }
        else{//internal node.

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

            if(index - 1 >= 0) {//get the sibling left of the child.
                BTreeNode firstSibling = mPointers.get(index - 1);
                if(mMinDegree < firstSibling.mKeys.size()) {//can move from the left side.

                }
            }
            else{//get the sibling right of the child.
                firstSibling = mPointers.get(index+1);
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
            mPointers.add(ith+1, rightNode);
            mKeys.add(ith, leftNode.mKeys.get(mMinDegree-1));
            for (int i = leftNode.mKeys.size(); i > mMinDegree-1; i--) {
                leftNode.mKeys.remove(i-1);
            }
            for (int i = leftNode.mPointers.size(); i > mMinDegree; i--) {
                leftNode.mPointers.remove(i-1);
            }
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
