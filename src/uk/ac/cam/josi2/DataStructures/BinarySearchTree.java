package uk.ac.cam.josi2.DataStructures;

import sun.reflect.generics.tree.Tree;

/**
 * Created by Joe on 06/04/2015.
 */
public class BinarySearchTree<T extends Comparable<T>, U> {

    private TreeNode mRoot;

    public BinarySearchTree(T key, U data){
        mRoot = new TreeNode(key , data);
    }

    public void insert(T key, U data){
        TreeNode  branch = mRoot;
        if(mRoot == null)
            mRoot = new TreeNode(key , data);

        while(true) {
            //go on the left side.
            if (branch.mKey.compareTo(key) >= 0) {
                if (branch.mLeft == null) {
                    branch.mLeft = mRoot = new TreeNode(key , data);
                    branch.mLeft.mParent = branch;
                    return;
                }
                else
                    branch = branch.mLeft;
            }
            //go on the right side
            else {
                if (branch.mRight == null) {
                    branch.mRight = mRoot = new TreeNode(key , data);
                    branch.mRight.mParent = branch;
                    return;
                }
                else
                    branch = branch.mRight;
            }
        }
    }

    public void delete(T key){
        delete(getNode(key));
    }

    public U search(T key){
        return getNode(key).mData;
    }

    //This is best called with the arguments as the smaller tree.
    //This will not invalidate the argument.
    public void merge(BinarySearchTree<T,U> toMerge){
        merge(toMerge.mRoot);
    }

    private void merge(TreeNode toMerge){
        if (toMerge != null){
            insert(toMerge.mKey, toMerge.mData);
            merge(toMerge.mLeft);
            merge(toMerge.mRight);
        }
    }

    public U minValue(){
        return mRoot.treeMin().mData;
    }

    public U maxValue(){
        return mRoot.treeMax().mData;
    }


    private TreeNode getNode(T key){
        TreeNode  tail = mRoot;

        while(tail != null && tail.mKey.compareTo(key) != 0){
            //go on the left side.
            if (tail.mKey.compareTo(key) > 0) {
                tail = tail.mLeft;
            }
            //go on the right side
            else {
                tail = tail.mRight;
            }
        }
        if(tail != null)
            return tail;
        return null;
    }

    private void delete(TreeNode node){
        if(node == null)
            return;

        if(node.mLeft == null){
            //swap with right, right may be null
            node.swap(node.mRight);
        }
        else if(node.mRight == null){
            //swap with left
            node.swap(node.mLeft);
        }
        else{
            TreeNode sec = node.mRight.treeMin();
            if(sec.mParent != node){
                //The successor is not the right child of the
                //node do be deleted

                //The successor may have a right child swap the right children with
                //it's parents place
                sec.swap(sec.mRight);
                //Make the successors right children the same as the node to be
                //deleted.
                sec.mRight = node.mRight;
                sec.mRight.mParent = sec;
            }
            //Now the node can be swapped with it's successor.
            node.swap(sec);
            sec.mLeft = node.mLeft;
            sec.mLeft.mParent = sec;
        }
    }

    private class TreeNode{
        private T mKey;
        private U mData;
        private TreeNode mLeft, mRight, mParent;

        TreeNode(T key, U data){
            mKey = key;
            mData = data;
        }

        private TreeNode treeMax(TreeNode t){
            while (t.mRight != null) {
                t = t.mRight;
            }
            return t;
        }

        private TreeNode treeMax(){
            return treeMax(this);
        }

        private TreeNode treeMin(){
            return treeMin(this);
        }

        private TreeNode treeMin(TreeNode t){
            while (t.mLeft != null) {
                t = t.mLeft;
            }
            return t;
        }

        private TreeNode successor(){
            TreeNode t = this;
            if(t == null)
                return null;
            if(t.mRight != null) {
                return treeMin(t.mRight);
            }
            else {
                TreeNode  parent = t.mParent;
                while (parent != null && t == parent.mRight){
                    t = parent;
                    parent = parent.mParent;
                }
                return parent;
            }
        }

        //only use inside delete.
        private void swap(TreeNode with){
            TreeNode node = this;
            if(node.mParent == null)
                mRoot = with;
            else if (node.mParent.mLeft == node)
                node.mParent.mLeft = with;
            else
                node.mParent.mRight = with;

            if (with != null)
                with.mParent = node.mParent;
        }
    }
}
