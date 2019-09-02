package com.touchsoft.java7;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server {


    public static void main(String[] args) {
        CreateConnection connection = new CreateConnection();
        try {
            try  (ServerSocket server = new ServerSocket(4044)){
                System.out.println("Сервер запущен!");


                while (true){
                    Socket clientSocket = server.accept();
                    User user = new User(clientSocket);
                    if (user.getIsAgent()){
                        connection.addAgent(user);
                    } else {
                        connection.addClient(user);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
