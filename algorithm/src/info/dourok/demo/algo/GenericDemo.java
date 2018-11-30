package info.dourok.demo.algo;

import java.util.*;

public class GenericDemo extends ArrayList {

    public static void main(String[] args) {
        Set<Integer> set = new HashSet<Integer>() {
            {
                add(1);
            }
        };
        System.out.println(set.size());
    }
}
