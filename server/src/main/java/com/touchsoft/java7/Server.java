package com.touchsoft.java7;

import java.io.*;
import java.net.*;
import org.apache.log4j.*;

public class Server {
    private static Logger logger = Logger.getLogger(Server.class);

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(4045)){
            logger.info("Started server");
            UserList userList = new UserList();

            while (true){
                Socket clientSocket = server.accept();
                UserSocket userSocket = new UserSocket(clientSocket);

                //logger.info("New socked detected");

                //userList.addUser(clientSocket);
            }
        } catch (IOException e) {
            logger.error(e + " Server.");
        }
    }
}
