package com.touchsoft.java7;

import java.util.ArrayList;

public class Test1 {

    private static ArrayList<String> arr1;
    private ArrayList<String> arr2;

    public Test1() {
        arr1 = new ArrayList<>();
        arr2 = new ArrayList<>();
    }

    public void add1Str(String str){
        arr1.add(str);
    }

    public void add2Str(String str){
        arr2.add(str);
    }

    public void demonstratorArr (){
        System.out.println( "arr1 :" );
        for (String str : arr1){
            System.out.println(str + " ");
        }
        System.out.println("arr2 :");
        for (String str : arr2){
            System.out.println(str + " ");
        }
        System.out.println();
    }


}
