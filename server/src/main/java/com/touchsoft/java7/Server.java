package com.touchsoft.java7;

import java.io.*;
import java.net.*;
import org.apache.log4j.*;

// Start server class
public class Server {
    private static final Logger logger = Logger.getLogger(Server.class);

    //Start server
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(4045)){
            logger.info("Started server");
            UserList userList = new UserList();

            // Clients wait loop
            while (true){
                Socket clientSocket = server.accept();
                UserSocket userSocket = new UserSocket(clientSocket);
            }
        } catch (IOException e) {
            logger.error(e + " Server.");
        }
    }
}
