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
        while(node != null){
            for (int i = 0; i != node.mKeys.size(); i++) {
                //check left side if not check next one and
                //after going through the whole loop
                //go down the right side.
                int value = node.mKeys.get(i).compareTo(key);
                if(value == 0 )//found the value return it.
                    return node.mKeys.get(i).mData;
                else if(value > 0) {//value is left of the key being checked
                    node = node.mPointers.get(i);
                    break;
                }
            }
            //value not found so far so must be right of this key
            node = node.mPointers.get(node.mPointers.size()-1);
        }
        return null;
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
