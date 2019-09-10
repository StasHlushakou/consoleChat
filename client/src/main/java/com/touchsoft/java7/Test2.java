package com.touchsoft.java7;

public class Test2 {
    public static void main(String[] args) {
        Test1 t1 = new Test1();
        Test1 t2 = new Test1();

        t1.add1Str("123");
        t1.add2Str("123");
        t2.add1Str("456");
        t2.add2Str("456");

        t1.demonstratorArr();
        t2.demonstratorArr();





    }
}
