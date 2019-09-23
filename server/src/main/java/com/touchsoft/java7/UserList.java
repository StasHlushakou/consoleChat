package com.touchsoft.java7;

import org.apache.log4j.*;
import java.util.ArrayList;//

// Class for storing users, and connecting free users
public class UserList extends Thread {

    private static final Logger logger = Logger.getLogger(UserList.class);

    private static ArrayList<User> clientList;
    private static ArrayList<User> agentList;


    // Create new user and add them to collection
    public static void addUser(User user){
        if (user.getIsAgent()){
            agentList.add(user);
        } else {
            clientList.add(user);
        }
        logger.info(user.getUserName() + " and add to List");
    }

    // Dell user from collection
    public static void dellUser(User user){
        if (user.getIsAgent()){
            agentList.remove(user);
        } else {
            clientList.remove(user);
        }
        logger.info("dellUser " + user.getUserName());
    }

    // Initial collection and starting thread
    public UserList(){
        clientList = new ArrayList<>();
        agentList = new ArrayList<>();
        start();
    }

    // Return all users
    public static ArrayList<User> userInListReturn(){
        ArrayList<User> al = new ArrayList<>();
        al.addAll(clientList);
        al.addAll(agentList);
        return al;
    }

    // Thread for searching and connecting users who are waiting for a connection
    @Override
    public void run(){

        while (true) {
            for (User userClient : clientList) {
                if (!(userClient.getIsConnected()) && userClient.getWaitingConnection()) {
                    for (User userAgent : agentList) {
                        if ((!userAgent.getIsConnected()) && userAgent.getWaitingConnection()) {
                            UserSocket.connectUsers(userClient,userAgent);
                            break;
                        }
                    }
                }
            }
            try {
                sleep(500);
            }catch (InterruptedException e){
                logger.error(e + " in thread UserList.");
            }
        }
    }
}
