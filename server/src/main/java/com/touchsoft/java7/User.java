package com.touchsoft.java7;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class User /*extends Thread*/ {

    private static Logger logger = Logger.getLogger(User.class);

    //инициализируются конструктором
    private String userName;            //имя пользователя
    private Boolean isAgent;            //флаг агент
    private Boolean isConnected;        //флаг соединентя
    private Boolean waitingConnection;  //флаг ожидания подключения

    private User connectUser;           //соединённый пользователь
    private UserSocket userSocket;

//----------------------------------------------------------------------------------------------------------------------
    // геттер userName
    public String getUserName (){
        return userName;
    }
    // сеттер userName
    public void setUserName(String userName){
        this.userName = userName;
    }


    // геттер isAgent
    public Boolean getIsAgent(){
        return isAgent;
    }
    // сеттер isAgent
    public void setIsAgent(boolean isAgent){
        this.isAgent = isAgent;
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


    // геттер connectUser
    public  User getConnectUser () {
        return connectUser;
    }
    // сеттер waitingConnection
    public void setonnectUser(User connectUser){
        this.connectUser = connectUser;
    }
//----------------------------------------------------------------------------------------------------------------------



    //конструктор User, получает на вход сокет, инициализирует имя и класс пользователя,
    // запускает нить, слушающую сообщения от пользователя
    public User(Boolean isAgent, String userName, UserSocket userSocket){

        this.isAgent = isAgent;
        this.userName = userName;
        this.isConnected = false;
        this.waitingConnection = true;
        this.userSocket = userSocket;

        logger.info("Create User " + this.getUserName());

    }


    //создание чата двух пользователей(настройка выводных потоков,установка флага соединения и ссылки на связанного пользователя)
    public void connectUsers(User user){

        userSocket.setOutToConnected(user.userSocket);
        connectUser.userSocket.setOutToConnected(userSocket);

        try{
            userSocket.


            this.out = new BufferedWriter(new OutputStreamWriter(user.userSocket.getOutputStream()));
            user.out = new BufferedWriter(new OutputStreamWriter(this.userSocket.getOutputStream()));

            user.connectUser = this;
            this.connectUser = user;

            user.out.write("isConnected" + "\n");
            user.out.flush();

            this.out.write("isConnected" + "\n");
            this.out.flush();


            user.isConnected = true;
            this.isConnected = true;

            user.waitingConnection = false;
            this.waitingConnection = false;

        } catch (IOException e){
            logger.error(e + " in connectUsers.");
        }


        logger.info("Connect User " + this.getUserName() + " with " + user.getUserName());

    }

    //разъединение чата двух пользователей (отключение выводных потоков, сброс флага соединения и ссылки на связанного пользователя)
    public void unconnectedUsers(User user){

        user.isConnected = false;
        user.connectUser = null;

        this.isConnected = false;
        this.connectUser = null;

        user.waitingConnection = false;
        this.waitingConnection = false;


        logger.info("unconnectedUsers " + this.getUserName() + " with" + user.getUserName());

    }


}
