package uk.ac.cam.josi2.DataStructures;

import java.util.List;

/**
 * Created by Joe on 07/04/2015.
 */
public class BTree<T extends Comparable<T>,U> {
    private BTreeNode mRoot;

    private final int mMinDegree;

    public BTree(int minDegree){
        mMinDegree = minDegree;
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
    
    private class BTreeNode{
        private List<KeyDataPair> mKeys;
        private List<BTreeNode> mPointers;
    }

    private class KeyDataPair implements Comparable<KeyDataPair>{

        private T mKey;
        private U mData;

        @Override
        public int compareTo(KeyDataPair o) {
            return mKey.compareTo(o.mKey);
        }

        public int compareTo(T key) {
            return mKey.compareTo(key);
        }
    }
}
