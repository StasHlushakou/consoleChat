package com.touchsoft.java7;

// Start client class
public class Client {

    public static String ipAddr = "localhost";
    public static int port = 4045;

    //Start console client
    public static void main(String[] args) {
        new ClientSomething(ipAddr, port);
    }
}
