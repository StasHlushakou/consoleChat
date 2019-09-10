package com.touchsoft.java7;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserSocket extends Thread {

    private static Logger logger = Logger.getLogger(UserSocket.class);



    private Socket socket;                  // сокет
    private BufferedReader in;              // буфер ввода сообщений от пользователля
    private BufferedWriter out;             // буфер вывода сообщений  пользователю
    private User userIn;                    // Пользователь, от которого нить принимает сообщения

    private User userOut;                   // Пользователь, которому нить пересылает сообщения



    public UserSocket (Socket userSocket){
        this.socket = userSocket;
        userIn = null;

        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException e){
            logger.error(e + " in constructor.");
        }
        start();
        logger.info("Socket connection created ");
    }



    //создание чата двух пользователей(настройка выводных потоков,установка флага соединения и ссылки на связанного пользователя)
    public static void connectUsers(User user1, User user2){

        user1.getUserSocket().userOut = user2;
        user2.getUserSocket().userOut = user1;


        try{
            user1.getUserSocket().out.write("/c" + "\n");
            user1.getUserSocket().out.flush();

            user2.getUserSocket().out.write("/c" + "\n");
            user2.getUserSocket().out.flush();

            user1.setIsConnected(true);
            user2.setIsConnected(true);

            user1.setWaitingConnection(false);
            user2.setWaitingConnection(false);

        } catch (IOException e){
            logger.error(e + " in connectUsers.");
        }

        logger.info("Connect User " + user1.getUserName() + " with " + user2.getUserName());

    }



    //разъединение чата двух пользователей (отключение выводных потоков, сброс флага соединения и ссылки на связанного пользователя)
    public void unconnectedUsers(){


        try {
            userOut.getUserSocket().out.write("/l" + "\n");
            userOut.getUserSocket().out.flush();
        } catch (IOException e){
            logger.error(e + " in unconnectedUsers.");
        }

        logger.info("Unconnected users " + userIn.getUserName() + " with" + userOut.getUserName());

        userIn.setIsConnected(false);
        userOut.setIsConnected(false);

        userIn.setWaitingConnection(false);
        userOut.setWaitingConnection(true);


        userOut.getUserSocket().userOut = null;
        userOut = null;
    }



//----------------------------------------------------------------------------------------------------------------------

    private void closeSocket(){
        try{
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            logger.error(e + " in thread User.");
        }

        logger.info("Socket connection close");
    }



    @Override
    public void run() {
        //logger.info("Start thread");
        try {
            while (true) {

                String word = in.readLine();
                if (word == null){
                    continue;
                }

                System.out.println(word);

                if (userIn == null && !word.equals("/reg" )){
                    continue;
                }
                if (userIn == null && word.equals("/reg" )){
                    Boolean isAgent;
                    String userName;
                    word = in.readLine();
                    if (word.equals("a")){
                        isAgent = true;
                    } else {
                        isAgent = false;
                    }
                    userName = in.readLine();
                    userIn = new User(isAgent, userName, this);
                    UserList.addUser(userIn);
                    continue;
                }


                if (!userIn.getWaitingConnection() && !userIn.getIsConnected() && word.equals("/r")){
                    userIn.setWaitingConnection(true);
                    continue;
                }


                if ( word.equals("/l")){
                    if (userIn.getIsConnected()){
                        this.unconnectedUsers();
                    }
                }


                if (word.equals("/e")){
                    if (userOut != null){
                        this.unconnectedUsers();
                    }
                    UserList.dellUser(userIn);
                    break;
                }


                if (userIn.getIsConnected() && !word.equals("/r")){
                    Date time = new Date(); // текущая дата
                    SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
                    String dtime = dt1.format(time); // время
                    userOut.getUserSocket().out.write("(" + dtime + ") " + userIn.getUserName() + ": " + word + "\n"); // отправляем на сервер
                    userOut.getUserSocket().out.flush();
                }


            }

            //Бллок очистки ресурсов при выходе из потока
            this.closeSocket();

        } catch (IOException e) {
            logger.error(e + " in thread User.");
            this.unconnectedUsers();
            UserList.dellUser(userIn);
            this.closeSocket();
        }
    }
}
