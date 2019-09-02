package com.touchsoft.java7;

import java.io.*;
import java.net.*;
import java.util.*;


public class User extends Thread{

    //инициализируются конструктором
    private BufferedReader in;      //буфер ввода сообщений от пользователля
    private Socket userSocket;      //сокет
    private Boolean isAgent;        //флаг агент
    private String userName;        //имя пользователя

    //инициализируются в ходе работы
    private BufferedWriter out;     //буфер отправки сообщений связанному пользователю
    private Boolean isConect;       //флаг соединентя
    private User connectUser;       //ссылка на связанного пользователя

    public static ArrayList<String> messagesBufer; //буфер сообщений пользователя


//----------------------------------------------------------------------------------------------------------------------

    //геттер isAgent
    public Boolean getIsAgent(){
        return isAgent;
    }

    //геттер getIsConect
    public Boolean getIsConect(){
        return isConect;
    }

    //геттер in
    public BufferedReader getIn(){
        return in;
    }

    //геттер out
    public BufferedWriter getOut(){
        return out;
    }

    //геттер connectUser
    public User getConnectUser(){
        return connectUser;
    }

    //конструктор User, получает на вход сокет, инициализирует имя и класс пользователя, запускает нить
    public User(Socket userSocket){
        this.userSocket = userSocket;
        try{
            this.in = new BufferedReader(new InputStreamReader(this.userSocket.getInputStream()));
            String word = in.readLine();
            if (word.equals("agent")){
                isAgent = true;
            } else {
                isAgent = false;
            }

            word = in.readLine();
            userName = word;
        } catch (IOException e){
            System.out.println(e);
        }
        this.messagesBufer = new ArrayList<>();
        this.isConect = false;
        start();
    }


    //создание чата двух пользователей(настройка выводных потоков,установка флага соединения и ссылки на связанного пользователя)
    public void connectUsers(User user){
        try{
            user.out = new BufferedWriter(new OutputStreamWriter(this.userSocket.getOutputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(user.userSocket.getOutputStream()));
        } catch (IOException e){
            System.out.println(e);
        }

        user.isConect = true;
        this.isConect = true;

        user.connectUser = this;
        this.connectUser = user;



    }

    //разъединение чата двух пользователей (отключение выводных потоков, сброс флага соединения и ссылки на связанного пользователя)
    private void unconnectUsers(User user){
        user.out = null;
        user.isConect = false;
        user.connectUser = null;
        this.out = null;
        this.isConect = false;
        this.connectUser = null;

    }





    @Override
    public void run() {
        try {
            while (true) {
                String word = getIn().readLine();

                // занесение в буфер сообщений, пока пользователь не подключён к другому пользователю
                // и очистка буфера при подключении
                if (!isConect){
                    messagesBufer.add(word);
                    continue;
                } else if (!messagesBufer.isEmpty()) {
                    for (String str : messagesBufer){
                        getOut().write(str + "\n");
                        messagesBufer.remove(str);
                    }
                }

                //разрушение связи при отключении одного из абонентов
                if (word.equals("leave") || word.equals("exit")) {
                    getOut().write(word + "\n");
                    getOut().flush();
                    this.unconnectUsers(connectUser);
                } else {
                    getOut().write(word + "\n");
                    getOut().flush();
                }

                if (word.equals("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println(e);

        }
    }
}
