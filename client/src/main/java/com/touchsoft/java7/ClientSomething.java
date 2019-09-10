package com.touchsoft.java7;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClientSomething {

    private String          addr; // ip адрес клиента
    private int             port; // порт соединения
    private Socket          socket;
    private BufferedReader  in; // поток чтения из сокета
    private BufferedWriter  out; // поток чтения в сокет
    private BufferedReader  inputUser; // поток чтения с консоли


    private boolean isConnected;     //
    private boolean isRegistration;


    public static ArrayList<String> messagesBuffer; //буфер сообщений пользователя



    public ClientSomething(String addr, int port) {
        this.addr = addr;
        this.port = port;
        try {
            this.socket = new Socket(addr, port);
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            registrationUser();

            new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
            new WriteMsg().start(); // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
        } catch (IOException e) {
            ClientSomething.this.downService();
            System.out.println("Exception in constructor ClientSomthing");
        }

    }



    public void registrationUser () {


        while (true) {
            System.out.println("Are you agent('a') or client('c')?");
            String userWord;
            try {
                userWord = inputUser.readLine();

                if (userWord.equals("a") || userWord.equals("c")) {
                    out.write("/reg" + "\n");
                    out.flush();
                    out.write(userWord + "\n");
                    out.flush();
                    break; // выходим из цикла если верно введён тип пользователя
                }else {
                    continue; // в противном случае повторяем вопрос про тип клиента
                }
            } catch (IOException e){
                System.out.println(e);
            }
        }

        System.out.print("Enter your name: ");
        try {
            String name = inputUser.readLine();
            out.write(name + "\n");
            out.flush();

            out.write("/r" + "\n");
            out.flush();
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

        //System.out.println("Ломаем соединение");
    }

    // нить чтения сообщений с сервера
    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (true) {
                    str = in.readLine(); // ждем сообщения с сервера
                    if (str == null){
                        continue;
                    }


                    if (str.equals("/c")){
                        isConnected = true;
                        System.out.println("Соединение создано");
                        if (!messagesBuffer.isEmpty()){
                            for (String message : messagesBuffer){
                                out.write(message + "\n");
                                out.flush();
                            }
                            messagesBuffer.clear();
                        }
                        continue;
                    }

                    if (str.equals("/l")) {
                        System.out.println("Собеседник покинул чат, ожидайте одключения к другому собеседнику");
                        isConnected = false;
                        out.write("/r" + "\n");
                        out.flush();
                    } else {
                        System.out.println(str); // пишем сообщение с сервера на консоль
                    }

                }
            } catch (IOException e) {
                ClientSomething.this.downService();
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


                    if (userWord.equals("/e")) {
                        out.write(userWord + "\n");
                        out.flush(); // чистим
                        break;
                    }


                    if (!isConnected){
                        if (userWord.equals("/e")){
                            out.write(userWord + "\n");
                            out.flush();
                            break;
                        }
                        messagesBuffer.add(userWord);
                        out.write("/r" + "\n");
                        out.flush();
                        continue;
                    }






                    if(userWord.equals("/l")){
                        out.write(userWord + "\n");
                        out.flush(); // чистим
                        isConnected = false;
                    }else {
                        out.write(userWord + "\n"); // отправляем на сервер
                        out.flush();
                    }

                }
                ClientSomething.this.downService(); // харакири

            } catch (IOException e) {
                ClientSomething.this.downService(); // в случае исключения тоже харакири

            }
        }
    }
}
