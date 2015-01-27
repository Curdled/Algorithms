package uk.ac.cam.josi2.Sorting;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by joeisaacs on 27/01/2015.
 */
public class MergeSortTopDown <T> implements Sorting {



    public static <T extends  Comparable<T>> void sort(List<T> l) {
        List<T> tmp = topDownmerge(l);
        l.clear();
        for (int i = 0; i != tmp.size(); i++) {
            l.add(tmp.get(i));
        }
    }

    private static <T extends  Comparable<T>> List<T> topDownmerge(List<T> l) {
        if (l.size() == 1)
            return l;

        int middle = l.size()/2;

        //make list of left side
        List<T> left = new LinkedList<T>();
        left.clear();
        for (int i = 0; i != middle; i++) {
            left.add(l.get(i));
        }
        List<T> right = new LinkedList<T>();
        right.clear();
        for (int j = middle; j != l.size(); j++) {
            right.add(l.get(j));
        }

        left = topDownmerge(left);
        right = topDownmerge(right);
        l = merge(left, right);
        return l;
    }

    private static <T extends  Comparable<T>> List<T> merge(List<T> left, List<T> right) {
        List<T> sortedList = new LinkedList<T>();

        int leftPos = 0;
        int rightPos = 0;
        while (leftPos < left.size() || rightPos < right.size()) {
            if (leftPos < left.size() && rightPos < right.size()){
                if(left.get(leftPos).compareTo(right.get(rightPos)) < 0){
                    sortedList.add(left.get(leftPos++));
                }
                else
                    sortedList.add(right.get(rightPos++));
            }
            else if (leftPos < left.size()) {
                sortedList.addAll(left.subList(leftPos, left.size()));
                leftPos = left.size();
            }
            else{
                sortedList.addAll(right.subList(rightPos, right.size()));
                rightPos = right.size();
            }
        }
        return sortedList;

    }

}
