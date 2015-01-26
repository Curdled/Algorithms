package uk.ac.cam.josi2;

import java.util.List;


public class BubbleSort implements Sorting{

    public static <T extends  Comparable<T>> List<T> sort(List<T> l) {
        if(l.size() == 0 || l.size() == 1)
            return l;
        for (int i = 0; i != l.size(); i++) {
            T first = l.get(0);
            for (int j = 1; j < l.size(); j++) {
                if(first.compareTo(l.get(j)) > 0) {
                    swap(l, j, j-1);
                }
                else{
                    first = l.get(j);
                }
            }
        }
        
        return l;
    }

    private static <T> void swap(List<T> l, int key1, int key2){
        T tmp = l.get(key1);
        l.set(key1, l.get(key2));
        l.set(key2, tmp);
    }
}
