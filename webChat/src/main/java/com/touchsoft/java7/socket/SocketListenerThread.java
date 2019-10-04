package com.touchsoft.java7.socket;

import com.touchsoft.java7.core.InputMsgHandler;
import com.touchsoft.java7.core.user.User;
import com.touchsoft.java7.core.user.UserSocket;
import com.touchsoft.java7.core.UserList.UserList;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketListenerThread extends Thread {


    static final Logger LOGGER = Logger.getLogger(SocketListenerThread.class);

    private Connection connection;
    private boolean stopThread;

    Pattern pattern = Pattern.compile("/reg\\s[ac]\\s.+");
    Matcher matcher ;

    public SocketListenerThread(Connection connection){
        this.connection = connection;
        stopThread = false;
        start();
    }



    @Override
    public void run() {
        LOGGER.info("Start SocketListenerThread");
        try {
            while (!stopThread) {

                String msg = connection.getReaderUserMsg().readLine();
                if (msg == null){
                    continue;
                }

                if (connection.getUser() == null){
                    matcher = pattern.matcher(msg);
                    if (!matcher.matches()){
                        continue;
                    }
                    else {
                        Boolean isAgent;
                        if (msg.substring(5,6).equals("a")){
                            isAgent = true;
                        } else {
                            isAgent = false;
                        }
                        String userName = msg.substring(7);
                        connection.setUser(new UserSocket(isAgent, userName, connection.getWriterUserMsg()));
                        continue;
                    }
                }
                InputMsgHandler.inputMsgHandler(connection.getUser(), msg);
            }

            //Блок очистки ресурсов при выходе из потока
            connection.closeConnection();
            stopThread = true;

        } catch (IOException e) {
            LOGGER.error(e + " in SocketListenerThread.");
            connection.closeConnection();
            UserList.dellUser(connection.getUser());
        }
    }
}