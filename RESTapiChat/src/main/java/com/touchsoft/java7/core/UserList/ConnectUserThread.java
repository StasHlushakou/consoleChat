package com.touchsoft.java7.core.UserList;

import com.touchsoft.java7.core.user.User;
import org.apache.log4j.Logger;
import java.util.ArrayList;

public class ConnectUserThread extends Thread {

    static final Logger LOGGER = Logger.getLogger(ConnectUserThread.class);


    public ConnectUserThread(){
        start();
    }

    @Override
    public void run(){
        LOGGER.info("Start ConnectUserThread");

        while (true) {

            ArrayList<User> clientList = UserList.getClientList();
            ArrayList<User> agentList = UserList.getAgentList();

            for (User userClient : clientList) {
                if (!(userClient.isConnected()) && userClient.isWaitingConnection()) {
                    for (User userAgent : agentList) {
                        if ((!userAgent.isConnected()) && userAgent.isWaitingConnection()) {
                            User.connectUsers(userClient,userAgent);
                            break;
                        }
                    }
                    break;
                }
            }
            try {
                sleep(100);
            }catch (InterruptedException e){
                LOGGER.error(e + " in ConnectUserThread.");
            }
        }
    }
}

