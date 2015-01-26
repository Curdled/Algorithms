package uk.ac.cam.josi2;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	    List<Integer> l = new ArrayList<>();
        l.add(4);
        l.add(6);
        l.add(1);
        l.add(5);
        l.add(8);
        l.add(4);
        l.add(2);
        List<Integer> l2 = new ArrayList<>(l);
        QuickSort.sort(l);
        BubbleSort.sort(l2);
        assert(l.equals(l2));
        assert(!l.equals(l2));
        int a = 5;
    }
}
