package uk.ac.cam.josi2.DataStructures;

/**
 * Created by Joe on 06/04/2015.
 */
public class TreeNode<T extends Comparable<T>, U> {

    private T mKey;
    private U mData;
    private TreeNode<T,U> mLeft, mRight, mParent;

    public TreeNode(T key,U data){


        mKey = key;
        mData = data;
        mLeft = null;
        mRight = null;
        mParent = null;
    }


    public void append(T key, U data){
        TreeNode<T, U> tail = this;

        while(true) {
            //go on the left side.
            if (tail.mKey.compareTo(key) >= 0) {
                if (tail.mLeft == null) {
                    tail.mLeft = new TreeNode<>(key, data);
                    tail.mLeft.mParent = tail;
                    return;
                }
                else
                    tail = tail.mLeft;
            }
            //go on the right side
            else {
                if (tail.mRight == null) {
                    tail.mRight = new TreeNode<>(key, data);
                    tail.mRight.mParent = tail;
                    return;
                }
                else
                    tail = tail.mRight;
                    tail = tail.mRight;
            }
        }
    }

    public U search(T key){
        TreeNode<T, U> tail = this;

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
            return tail.mData;
        return null;
    }

    public U minValue(){
        return treeMin(this).mData;
    }

    public U maxValue(){
        return treeMax(this).mData;
    }

    private TreeNode<T,U> treeMax(TreeNode<T,U> t){
        while (t.mRight != null) {
            t = t.mRight;
        }
        return t;
    }

    private TreeNode<T,U> treeMin(TreeNode<T,U> t){
        while (t.mLeft != null) {
            t = t.mLeft;
        }
        return t;
    }

    private TreeNode<T,U> successor(){
        TreeNode<T,U> t = this;
        if(t == null)
            return null;
        if(t.mRight != null) {
            return treeMin(t.mRight);
        }
        else {
            TreeNode<T, U> parent = t.mParent;
            while (parent != null && t == parent.mRight){
                t = parent;
                parent = parent.mParent;
            }
            return parent;
        }
    }



    public void delete(T key){
       // return delete(search(key));
    }

    public void delete(TreeNode<T,U> node){
        if(node == null)
            return;

        if(node.mLeft == null){
            //swap with right, right may be null
            swap(node, node.mRight);
        }
        else if(node.mRight == null){
            //swap with left
            swap(node, node.mLeft);
        }
        else{
            //swap with successor
            swap(node, node.successor());
        }
    }

    //only use inside delete.
    private void swap(TreeNode<T,U> node, TreeNode<T,U> with){
        if(node.mParent != null) {
            if (node.mParent.mLeft == node)
                node.mParent.mLeft = with;
            else
                node.mParent.mRight = with;
        }
        if(with != null){

            with.mLeft = node.mLeft;
            with.mRight = node.mRight;
            with.mParent = node.mParent;

        }
        node.mParent = null;

    }


}
