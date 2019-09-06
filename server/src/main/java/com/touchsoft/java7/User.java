package com.touchsoft.java7;

import java.io.*;
import java.net.*;
import java.util.*;


public class User extends Thread {

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

    // геттер getIsConect
    public Boolean getIsConnected(){
        return isConnected;
    }

    // геттер waitingConnection
    public  Boolean getWaitingConnection () {
        return waitingConnection;
    }

    //
    public void exitUser(){
        UserList.dellUser(this);
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
            System.out.println(isAgent + " " + userName + " " + isConnected + " " + waitingConnection);
            start();

        } catch (IOException e){
            System.out.println(e);
        }

    }


    //создание чата двух пользователей(настройка выводных потоков,установка флага соединения и ссылки на связанного пользователя)
    public void connectUsers(User user){
        try{
            user.out = new BufferedWriter(new OutputStreamWriter(this.userSocket.getOutputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(user.userSocket.getOutputStream()));
            user.connectUser = this;
            this.connectUser = user;

            user.out.write("isConnected" + "\n");
            user.out.flush();

            this.out.write("isConnected" + "\n");
            this.out.flush();

            user.isConnected = true;
            this.isConnected = true;

        } catch (IOException e){
            System.out.println(e);
        }

        System.out.println("Соединение создано ");

    }

    //разъединение чата двух пользователей (отключение выводных потоков, сброс флага соединения и ссылки на связанного пользователя)
    private void unconnectUsers(User user){

        try {

            user.out.close();
            user.isConnected = false;
            user.connectUser = null;
            this.out.close();
            this.isConnected = false;
            this.connectUser = null;
            System.out.println("Разъединение");
        } catch (IOException e){
            System.out.println(e +"при разъединении");
        }


    }


    @Override
    public void run() {
        try {
            while (true) {
                String word = in.readLine();
                if (word == null){
                    continue;
                }

                System.out.println(word);

                //разрушение связи при отключении одного из абонентов
                if (word.equals("leave") || word.equals("exit")) {
                    out.write(word + "\n");
                    out.flush();
                    unconnectUsers(connectUser);
                } else {
                    out.write(word + "\n");
                    out.flush();
                }

                if (word.equals("exit")) {
                    this.exitUser();
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }


}
