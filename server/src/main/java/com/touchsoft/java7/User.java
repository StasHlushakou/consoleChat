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
        System.out.println("Выход " + this.getName());
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

            System.out.println("Создан " + this.getName() + " и запущена его нить ");

        } catch (IOException e){
            System.out.println(e);
        }



    }


    //создание чата двух пользователей(настройка выводных потоков,установка флага соединения и ссылки на связанного пользователя)
    public void connectUsers(User user){
        try{
            user.out = new BufferedWriter(new OutputStreamWriter(this.userSocket.getOutputStream()));
            System.out.println("user.out ");

            this.out = new BufferedWriter(new OutputStreamWriter(user.userSocket.getOutputStream()));
            System.out.println("this.out ");

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
            System.out.println(e + " исключение при connectUsers");
        }

        System.out.println("Соединение создано между " + this.getName() + " и " + user.getName() );

    }

    //разъединение чата двух пользователей (отключение выводных потоков, сброс флага соединения и ссылки на связанного пользователя)
    private void unconnectedUsers(User user){

        try {
            user.out.close();
            user.isConnected = false;
            user.connectUser = null;
            this.out.close();
            this.isConnected = false;
            this.connectUser = null;

            user.waitingConnection = true;
            this.waitingConnection = true;

            System.out.println("Разъединение " + this.getName() + " и " + user.getName() );
        } catch (IOException e){
            System.out.println(e +" исключение при unconnectedUsers");
        }


    }


    @Override
    public void run() {


        try {
            while (true) {

                if (!this.isConnected){
                    sleep(1000);
                    continue;
                }

                String word = in.readLine();
                if (word == null){
                    continue;
                }

                System.out.println(word);

                //разрушение связи при отключении одного из абонентов
                if (word.equals("leave")) {
                    out.write(word + "\n");
                    out.flush();
                    this.unconnectedUsers(connectUser);
                } else if (word.equals("exit")){
                    out.write(word + "\n");
                    out.flush();
                    this.exitUser();
                    this.unconnectedUsers(connectUser);
                    break;
                } else {
                    out.write(word + "\n");
                    out.flush();
                }

            }
        } catch (IOException e) {
            System.err.println(e);
        }catch (InterruptedException e){
            System.out.println(e);
        }


    }


}
