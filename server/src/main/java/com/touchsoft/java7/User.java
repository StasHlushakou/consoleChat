package com.touchsoft.java7;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class User extends Thread {

    final static Logger logger = Logger.getLogger(User.class);

    //инициализируются конструктором

    private Boolean isAgent;            //флаг агент
    private Boolean isConnected;        //флаг соединентя
    private Boolean waitingConnection;  //флаг ожидания подключения

    private Socket userSocket;          //сокет
    private String userName;            //имя пользователя

    private BufferedReader in;          //буфер ввода сообщений от пользователля
    private BufferedWriter out;         //буфер вывода сообщений другому пользователю

    private User connectUser;           //соединённый пользователь


//----------------------------------------------------------------------------------------------------------------------

    // геттер isAgent
    public Boolean getIsAgent(){
        return isAgent;
    }

    // сеттер isAgent
    public void setIsAgent(boolean isAgent){
        this.isAgent = isAgent;
    }

    // геттер getIsConect
    public Boolean getIsConnected(){
        return isConnected;
    }

    // геттер waitingConnection
    public  Boolean getWaitingConnection () {
        return waitingConnection;
    }

    // геттер имени
    public String getUserName (){
        return userName;
    }


    //конструктор User, получает на вход сокет, инициализирует имя и класс пользователя,
    // запускает нить, слушающую сообщения от пользователя
    public User(Socket userSocket){
        this.userSocket = userSocket;

        try {
            this.in = new BufferedReader(new InputStreamReader(this.userSocket.getInputStream()));
            String word = in.readLine();
            if (word.equals("agent")){
                isAgent = true;
            } else {
                isAgent = false;
            }

            word = in.readLine();
            userName = word;
            this.isConnected = false;
            this.waitingConnection = true;
            start();

        } catch (IOException e){
            logger.error(e + " in constructor User.");
        }
        logger.info("Create User " + this.getUserName());

    }


    //создание чата двух пользователей(настройка выводных потоков,установка флага соединения и ссылки на связанного пользователя)
    public void connectUsers(User user){
        try{
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

        logger.info("Connect User " + this.getUserName() + " with" + user.getUserName());

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


    @Override
    public void run() {
        //logger.info("Start thread User");
        try {
            while (true) {

                String word = in.readLine();
                if (word == null){
                    continue;
                }

                if (!this.waitingConnection && !this.isConnected && word.equals("ready")){
                    this.waitingConnection = true;
                    continue;
                }

                //разрушение связи при отключении одного из абонентов
                if (word.equals("leave")) {
                    out.write(word + "\n");
                    out.flush();
                    this.unconnectedUsers(connectUser);
                } else if (word.equals("exit")){
                    out.write(word + "\n");
                    out.flush();
                    this.unconnectedUsers(connectUser);
                    UserList.dellUser(this);
                    break;
                } else {
                    Date time = new Date(); // текущая дата
                    SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
                    String dtime = dt1.format(time); // время
                    out.write("(" + dtime + ") " + userName + ": " + word + "\n"); // отправляем на сервер
                    out.flush();
                }

            }
        } catch (IOException e) {
            logger.error(e + " in thread User.");
        }
    }

}
