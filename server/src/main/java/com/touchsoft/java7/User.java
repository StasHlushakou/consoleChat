package com.touchsoft.java7;

import org.apache.log4j.*;

// Class that contains user information
public class User {

    private static final Logger logger = Logger.getLogger(User.class);

    private String userName;
    private Boolean isAgent;
    private Boolean isConnected;
    private Boolean waitingConnection;
    private UserSocket userSocket;

    public String getUserName (){
        return userName;
    }

    public Boolean getIsAgent(){
        return isAgent;
    }

    public Boolean getIsConnected(){
        return isConnected;
    }

    public void setIsConnected(boolean isConnected){
        this.isConnected = isConnected;
    }

    public  Boolean getWaitingConnection () {
        return waitingConnection;
    }

    public void setWaitingConnection(boolean waitingConnection){
        this.waitingConnection = waitingConnection;
    }

    public  UserSocket getUserSocket () {
        return userSocket;
    }

    public User(Boolean isAgent, String userName, UserSocket userSocket){

        this.userSocket = userSocket;
        this.userName = userName;
        this.isAgent = isAgent;
        this.isConnected = false;
        this.waitingConnection = true;

        logger.info("Create User " + this.getUserName());
    }

}
