package com.touchsoft.java7;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        ArrayList<Integer> i = new ArrayList<>();
        i.add(1);
        i.add(2);
        i.add(3);
        i.add(4);
        i.add(5);

        for (int in : i){
            System.out.print(in + " ");
        }
        System.out.println();

        Integer k = 4;
        i.remove(k);
        i.remove(0);
        i.remove(0);


        for (int in = 0; in < i.size(); in++){
            System.out.print(i.get(in) + " ");
        }
    }
}
