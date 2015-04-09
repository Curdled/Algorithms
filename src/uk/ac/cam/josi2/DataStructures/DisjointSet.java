package uk.ac.cam.josi2.DataStructures;

/**
 * Created by joeisaacs on 09/04/2015.
 */
public class DisjointSet<T> {


    private T mValue;
    private int mRank;
    private DisjointSet mParent;

    public DisjointSet(T data) {
        mValue = data;
        mRank = 0;
        mParent = this;
    }

    //this implements path compression.
    public DisjointSet findSet() {
        DisjointSet set = this;
        if (set != set.mParent)
            set.mParent = set.mParent.findSet();
        return set.mParent;
    }

    public void union(DisjointSet other) {
        link(this.findSet(), other.findSet());
    }

    public T getValue() {
        return mValue;
    }

    public void setValue(T value) {
        mValue = value;
    }

    //this link uses union by rank.
    private void link(DisjointSet set1, DisjointSet set2){
        if(set1.mRank > set2.mRank)
            set2.mParent = set1;
        else{
            set1.mParent = set2;
            if(mRank == set2.mRank)
                set2.mRank++;
        }
    }
}
