package uk.ac.cam.josi2;

import java.util.Comparator;
import java.util.List;

/**
 * Created by joeisaacs on 09/01/2015.
 */
public class QuickSort <T> implements Sorting{


    public static <T extends  Comparable<T>> List<T> sort(List<T> l)  {
        if(l.size() == 0 || l.size() == 1)
            return l;

        return sortImp(l, l.size()-1, 0);
    }

    private static <T extends Comparable<T>> List<T> sortImp(List<T> l, int hi, int lo){
        if(hi <= lo)
            return l;
        int pivot = genPivot(hi, lo);
        T pivotVal = l.get(pivot);
        swap(l, pivot, hi);
        int sortIndex = lo;
        for (int i = lo; i != hi; i++) {
            if(pivotVal.compareTo(l.get(i)) >= 0) {
                swap(l, i, sortIndex);
                sortIndex++;
            }
        }
        swap(l, sortIndex, hi);
        sortImp(l, hi, sortIndex+1);
        sortImp(l, sortIndex-1, lo);
        return l;
    }

    private static <T> void swap(List<T> l, int key1, int key2){
        T tmp = l.get(key1);
        l.set(key1, l.get(key2));
        l.set(key2, tmp);
    }

    private static int genPivot(int hi, int lo){
        return lo;
    }


}
