package com.touchsoft.java7;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Client {

    public static String ipAddr = "localhost";
    public static int port = 4045;


    public static void main(String[] args) {
        new ClientSomething(ipAddr, port);
    }

}
