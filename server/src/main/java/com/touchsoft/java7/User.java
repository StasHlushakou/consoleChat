package com.touchsoft.java7;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
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

    //добавление времени
    private Date            time;
    private String          dtime;
    private SimpleDateFormat dt1;

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

    // геттер имени
    public String getUserName (){return userName; }

    //
    public void exitUser(){
        UserList.dellUser(this);
        System.out.println("Выход " + this.getUserName());
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

            System.out.println("Создан " + this.getUserName() + " и запущена его нить ");

        } catch (IOException e){
            System.out.println(e);
        }



    }


    //создание чата двух пользователей(настройка выводных потоков,установка флага соединения и ссылки на связанного пользователя)
    public void connectUsers(User user){
        try{
            this.out = new BufferedWriter(new OutputStreamWriter(user.userSocket.getOutputStream()));
            System.out.println("this.out ");

            user.out = new BufferedWriter(new OutputStreamWriter(this.userSocket.getOutputStream()));
            System.out.println("user.out ");


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

        System.out.println("Соединение создано между " + this.getUserName() + " и " + user.getUserName() );

    }

    //разъединение чата двух пользователей (отключение выводных потоков, сброс флага соединения и ссылки на связанного пользователя)
    private void unconnectedUsers(User user){

        user.isConnected = false;
        user.connectUser = null;

        this.isConnected = false;
        this.connectUser = null;

        user.waitingConnection = true;
        this.waitingConnection = true;

        System.out.println("Разъединение " + this.getUserName() + " и " + user.getUserName() );

    }


    @Override
    public void run() {

        try {
            while (true) {

/*
                if (!this.isConnected){
                    sleep();
                    continue;
                }
*/

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
                    this.unconnectedUsers(connectUser);
                    this.exitUser();
                    break;
                } else {
                    time = new Date(); // текущая дата
                    dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
                    dtime = dt1.format(time); // время
                    out.write("(" + dtime + ") " + userName + ": " + word + "\n"); // отправляем на сервер
                    out.flush();
                }

            }
        } catch (IOException e) {
            System.err.println(e);

        }

    }


}
