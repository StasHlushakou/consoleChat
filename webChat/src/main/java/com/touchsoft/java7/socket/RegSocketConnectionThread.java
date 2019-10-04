package com.touchsoft.java7.socket;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.touchsoft.java7.core.UserList.UserList;
import org.apache.log4j.Logger;


public class RegSocketConnectionThread extends Thread{

    static final Logger LOGGER = Logger.getLogger(RegSocketConnectionThread.class);


    public  RegSocketConnectionThread() {
        start();
    }

    @Override
    public void run(){
                try (ServerSocket server = new ServerSocket(4045)){
            LOGGER.info("Started SocketServer");
            new UserList();

            while (true){
                Socket clientSocket = server.accept();
                new Connection(clientSocket);
            }
        } catch (IOException e) {
            LOGGER.error(e + " in SocketServer.");
        }
    }


}
