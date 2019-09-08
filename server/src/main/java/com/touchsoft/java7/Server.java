package com.touchsoft.java7;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server {


    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(4045)){

            System.out.println("Сервер запущен!");
            UserList userList = new UserList();

            while (true){
                Socket clientSocket = server.accept();
                userList.addUser(clientSocket);

            }
        } catch (IOException e) {
            System.err.println(e + " - исключение на Server");
        }
    }
}
