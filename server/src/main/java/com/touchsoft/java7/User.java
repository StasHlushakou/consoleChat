package com.touchsoft.java7;

import org.apache.log4j.*;



public class User {

    private static final Logger logger = Logger.getLogger(User.class);

    //инициализируются конструктором
    private String userName;            //имя пользователя
    private Boolean isAgent;            //флаг агент
    private Boolean isConnected;        //флаг соединентя
    private Boolean waitingConnection;  //флаг ожидания подключения


    private UserSocket userSocket;           //сокет, следящий за сообщениями от данного пользователя


    // геттер userName
    public String getUserName (){
        return userName;
    }


    // геттер isAgent
    public Boolean getIsAgent(){
        return isAgent;
    }


    // геттер isConnected
    public Boolean getIsConnected(){
        return isConnected;
    }
    // сеттер isConnected
    public void setIsConnected(boolean isConnected){
        this.isConnected = isConnected;
    }


    // геттер waitingConnection
    public  Boolean getWaitingConnection () {
        return waitingConnection;
    }
    // сеттер waitingConnection
    public void setWaitingConnection(boolean waitingConnection){
        this.waitingConnection = waitingConnection;
    }


    // геттер userSocket
    public  UserSocket getUserSocket () {
        return userSocket;
    }
    // сеттер userSocket


    //конструктор User, получает на вход сокет, инициализирует имя и класс пользователя,
    public User(Boolean isAgent, String userName, UserSocket userSocket){

        this.userSocket = userSocket;
        this.userName = userName;
        this.isAgent = isAgent;
        this.isConnected = false;
        this.waitingConnection = true;

        logger.info("Create User " + this.getUserName());
    }

}
