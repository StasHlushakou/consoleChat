package com.touchsoft.java7;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class ClientSomthing {

    private String          addr; // ip адрес клиента
    private int             port; // порт соединения
    private Socket          socket;
    private BufferedReader  in; // поток чтения из сокета
    private BufferedWriter  out; // поток чтения в сокет
    private BufferedReader  inputUser; // поток чтения с консоли

    private String          name; // имя клиента

    private boolean isConected;     //

    /*
    private Date            time;
    private String          dtime;
    private SimpleDateFormat dt1;

    */


    public static ArrayList<String> messagesBuffer; //буфер сообщений пользователя



    public ClientSomthing(String addr, int port) {
        this.addr = addr;
        this.port = port;
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            registrationUser();

            new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
            new WriteMsg().start(); // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
        } catch (IOException e) {
            ClientSomthing.this.downService();
        }
    }



    public void registrationUser () {

        System.out.println("Are you an agent or client?");
        while (true) {
            String userWord;
            try {
                userWord = inputUser.readLine(); // сообщения с консоли
                if (userWord.equals("agent") || userWord.equals("client")) {
                    out.write(userWord + "\n");
                    out.flush();
                    break; // выходим из цикла если верно введён тип пользователя
                } else {
                    System.out.println("Are you an agent or client?");
                }
            } catch (IOException e){
                System.out.println(e);
            }
        }

        System.out.print("Enter your name: ");
        try {
            name = inputUser.readLine();
            out.write(name + "\n");
            out.flush();


/*            out.write("ready" + "\n");
            out.flush();*/
        } catch (IOException ignored) { }

        messagesBuffer = new ArrayList<>();

    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
            messagesBuffer.clear();
        } catch (IOException ignored) {}
    }

    // нить чтения сообщений с сервера
    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (true) {
                    str = in.readLine(); // ждем сообщения с сервера

                    if (str.equals("isConnected")){
                        isConected = true;
                        System.out.println("Соединение создано");
                        continue;
                    }

                    if (str.equals("exit") || str.equals("leave")) {
                        System.out.println("Собеседник покинул чат, ожидайте одключения к другому собеседнику");
                        isConected = false;
                    } else {
                        System.out.println(str); // пишем сообщение с сервера на консоль
                    }

                }
            } catch (IOException e) {
                ClientSomthing.this.downService();
            }
        }
    }


    // нить отправляющая сообщения приходящие с консоли на сервер
    public class WriteMsg extends Thread {

        @Override
        public void run() {
            try {


                // цикл чтения сообщений с консоли и их отправка на сервер/сохранения в буфер
                while (true) {
                    String userWord = inputUser.readLine(); // сообщения с консоли

                    if (!isConected){
                        /*
                        if (inputUser.equals("exit")){
                            out.write("exit" + "\n");
                            out.flush();
                            break;
                        }
                        */
                        messagesBuffer.add(userWord);
                        continue;
                    }


                    if (!messagesBuffer.isEmpty()){
                        for (String message : messagesBuffer){
                            out.write(message + "\n");
                            out.flush();
                        }
                        messagesBuffer.clear();
                    }


                    if (userWord.equals("exit")) {
                        out.write("exit" + "\n");
                        out.flush(); // чистим
                        break;
                    } else if(userWord.equals("leave")){
                        out.write("leave" + "\n");
                        out.flush(); // чистим
                        out.write("ready" + "\n");
                        out.flush();
                        isConected = false;

                    }else {
                        out.write(userWord + "\n"); // отправляем на сервер
                        out.flush();
                    }

                }
                ClientSomthing.this.downService(); // харакири

            } catch (IOException e) {
                ClientSomthing.this.downService(); // в случае исключения тоже харакири

            }
        }
    }
}


/*
                        time = new Date(); // текущая дата
                        dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
                        dtime = dt1.format(time); // время
                        out.write("(" + dtime + ") " + name + ": " + userWord + "\n"); // отправляем на сервер
                        */