package com.touchsoft.java7;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

class ClientSomthing {

    private Socket socket;
    private BufferedReader  in; // поток чтения из сокета
    private BufferedWriter  out; // поток чтения в сокет
    private BufferedReader  inputUser; // поток чтения с консоли
    private String          addr; // ip адрес клиента
    private int             port; // порт соединения
    private String          nickname; // имя клиента
    private Date            time;
    private String          dtime;
    private SimpleDateFormat dt1;



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
            //this.pressNickname(); // перед началом необходимо спросит имя
            new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
            new WriteMsg().start(); // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
        } catch (IOException e) {
            ClientSomthing.this.downService();
        }
    }



    private void pressNickname() {
        System.out.print("Press your nick: ");
        try {
            nickname = inputUser.readLine();
            out.write("Hello " + nickname + "\n");
            out.flush();
        } catch (IOException ignored) {
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
                    if (str.equals("exit")) {
                        ClientSomthing.this.downService(); // харакири
                        break; // выходим из цикла если пришло "stop"
                    }
                    System.out.println(str); // пишем сообщение с сервера на консоль
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


                // проверка типа пользователя и его отправка на сервер
                String userWord;
                System.out.println("Are you an agent or client?");
                while (true) {
                    userWord = inputUser.readLine(); // сообщения с консоли
                    if (userWord.equals("agent") || userWord.equals("client")) {
                        out.write(userWord + "\n");
                        out.flush();
                        break; // выходим из цикла если пришло "stop"
                    } else {
                        System.out.println("Are you an agent or client?");
                    }
                }


                // имя пользователя и его отправка на сервер
                System.out.println("Enter your name");
                nickname = inputUser.readLine();
                out.write(userWord + "\n");
                out.flush();


                // цикл чтения сообщений с консоли и их отправка на сервер
                while (true) {
                    userWord = inputUser.readLine(); // сообщения с консоли


                    if (userWord.equals("leave")) {
                        out.write("leave" + "\n");
                        out.flush(); // чистим
                    } else if(userWord.equals("exit")){
                        out.write("exit" + "\n");
                        out.flush(); // чистим
                        break; // выходим из цикла если пришло "exit"
                    }else {
                        time = new Date(); // текущая дата
                        dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
                        dtime = dt1.format(time); // время
                        out.write("(" + dtime + ") " + nickname + ": " + userWord + "\n"); // отправляем на сервер
                        out.flush();
                    }

                    ClientSomthing.this.downService(); // харакири
                }


            } catch (IOException e) {
                ClientSomthing.this.downService(); // в случае исключения тоже харакири

            }
        }
    }
}
