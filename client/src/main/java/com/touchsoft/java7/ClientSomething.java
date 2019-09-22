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

    //private boolean isConnected;

    //public static ArrayList<String> messagesBuffer; //буфер сообщений пользователя



    public ClientSomething(String addr, int port) {
        this.addr = addr;
        this.port = port;
        this.socket = null;

        while (this.socket == null){
            try {
                this.socket = new Socket(addr, port);
            }catch (IOException e){}
        }

        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
            new WriteMsg().start(); // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
        } catch (IOException e) {
            ClientSomething.this.downService();
            System.out.println("Exception in constructor ClientSomthing");
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
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
                    if (str == null){
                        continue;
                    }
                    System.out.println(str); // пишем сообщение с сервера на консоль

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

                // цикл чтения сообщений с консоли и их отправка на сервер
                while (true) {
                    String userWord = inputUser.readLine(); // сообщения с консоли


                    if (userWord.equals("/e")) {
                        out.write(userWord + "\n");
                        out.flush(); // чистим
                        break;
                    }

                    out.write(userWord + "\n");
                    out.flush();

                }
                ClientSomething.this.downService();

            } catch (IOException e) {
                ClientSomething.this.downService();

            }
        }
    }
}