package com.touchsoft.java7;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserSocket extends Thread {

    private static Logger logger = Logger.getLogger(UserSocket.class);

    private User user;                      // Пользователь, от которого нить принимает сообщения

    private Socket userSocket;              // сокет
    private BufferedReader in;              // буфер ввода сообщений от пользователля
    private BufferedWriter out;             // буфер вывода сообщений  пользователю
    private BufferedWriter outToConnected;  // буфер вывода сообщений связанному пользователю


    public UserSocket (Socket userSocket){
        this.userSocket = userSocket;

        try {
            this.in = new BufferedReader(new InputStreamReader(this.userSocket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.userSocket.getOutputStream()));
        } catch (IOException e){
            logger.error(e + " in constructor.");
        }
        logger.info("Socket connection created ");
    }

//----------------------------------------------------------------------------------------------------------------------

    public void connect(User user){
        outToConnected = new BufferedWriter(new OutputStreamWriter(user.getConnectUser().userSocket.getOutputStream()));
    }




//----------------------------------------------------------------------------------------------------------------------

    private void closeSocket(){
        try{
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (outToConnected != null)
                outToConnected.close();
            if (userSocket != null)
                userSocket.close();
        } catch (IOException e) {
            logger.error(e + " in thread User.");
        }
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



                if (user == null && !word.equals("/reg" )){
                    continue;
                }
                if (user == null && word.equals("/reg" )){
                    Boolean isAgent;
                    String userName;
                    word = in.readLine();
                    if (word.equals("agent")){
                        isAgent = true;
                    } else {
                        isAgent = false;
                    }
                    word = in.readLine();
                    userName = word;
                    user = new User(isAgent, userName, this);
                    UserList.addUser(user);
                    continue;
                }


                if (user.getWaitingConnection() && !user.getIsConnected() && word.equals("/ready")){
                    user.setWaitingConnection(true);
                    continue;
                }




                if ( word.equals("/l")){
                    if (user.getIsConnected()){
                        outToConnected.write(word + "\n");
                        outToConnected.flush();
                        user.unconnectedUsers(user.getConnectUser());
                    }
                    user.setWaitingConnection(false);
                }


                if (word.equals("/e")){
                    if (user.getIsConnected()){
                        outToConnected.write(word + "\n");
                        outToConnected.flush();
                        user.unconnectedUsers(user.getConnectUser());
                    }


                    UserList.dellUser(user);
                    break;
                }

                if (user.getIsConnected()){
                    Date time = new Date(); // текущая дата
                    SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
                    String dtime = dt1.format(time); // время
                    outToConnected.write("(" + dtime + ") " + user.getUserName() + ": " + word + "\n"); // отправляем на сервер
                    outToConnected.flush();
                }





            }

            //Бллок очистки ресурсов при выходе из потока
            this.closeSocket();

        } catch (IOException e) {
            logger.error(e + " in thread User.");
            UserList.dellUser(user);
            this.closeSocket();
        }
    }
}
