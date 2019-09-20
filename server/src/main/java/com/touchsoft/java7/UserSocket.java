package com.touchsoft.java7;

import org.apache.log4j.*;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSocket extends Thread {

    private static final Logger logger = Logger.getLogger(UserSocket.class);

    private Socket socket;                  // сокет
    private BufferedReader in = null;              // буфер ввода сообщений от пользователля
    private BufferedWriter out = null;             // буфер вывода сообщений  пользователю
    private User userIn;                    // Пользователь, от которого нить принимает сообщения
    private User userOut;                   // Пользователь, которому нить пересылает сообщения

    private ArrayList<String> msgList;

    Pattern pattern = Pattern.compile("/reg\\s[ac]\\s.+");
    Matcher matcher ;





    public UserSocket (Socket userSocket){
        this.socket = userSocket;
        userIn = null;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

            this.out.write("From registration enter '/reg [a/c] name'" + "\n");
            this.out.flush();
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

            user1.getUserSocket().out.write("You are connected to " + user2.getUserName() + "\n");
            user1.getUserSocket().out.flush();

            user2.getUserSocket().out.write("You are connected to " + user1.getUserName() + "\n");
            user2.getUserSocket().out.flush();


            if (!user1.getUserSocket().msgList.isEmpty()){
                for(String s : user1.getUserSocket().msgList){
                    user2.getUserSocket().out.write(s);
                    user2.getUserSocket().out.flush();
                }
                user1.getUserSocket().msgList.clear();
            }

            if (!user2.getUserSocket().msgList.isEmpty()){
                for(String s : user2.getUserSocket().msgList){
                    user1.getUserSocket().out.write(s);
                    user1.getUserSocket().out.flush();
                }
                user2.getUserSocket().msgList.clear();
            }


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
            userOut.getUserSocket().out.write(userOut.getUserName() + " left the chat " + "\n");
            userOut.getUserSocket().out.flush();
            userOut.getUserSocket().out.write(" Please wait for connection " + "\n");
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



                if (userIn == null){
                    matcher = pattern.matcher(word);
                    if (!matcher.matches()){
                        continue;
                    }
                    if (matcher.matches()){
                        Boolean isAgent;
                        if (word.substring(5,6).equals("a")){
                            isAgent = true;
                        } else {
                            isAgent = false;
                        }
                        String userName = word.substring(7);
                        userIn = new User(isAgent, userName, this);
                        UserList.addUser(userIn);
                        msgList = new ArrayList<>();

                        out.write("You are registered as " + userIn.getUserName() + "\n");
                        out.flush();

                        continue;
                    }
                }







                if ( word.equals("/l")){
                    if (userIn.getIsConnected()){
                        this.unconnectedUsers();
                    }
                    out.write("You left the chat " + "\n");
                    out.flush();
                    continue;
                }


                if (word.equals("/e")){
                    if (userOut != null){
                        this.unconnectedUsers();
                    }
                    UserList.dellUser(userIn);
                    break;
                }



                if (!userIn.getIsConnected()){

                    Date time = new Date(); // текущая дата
                    SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
                    String dtime = dt1.format(time); // время
                    msgList.add("(" + dtime + ") " + userIn.getUserName() + ": " + word + "\n");

                    userIn.setWaitingConnection(true);
                    continue;
                }

                if (userIn.getIsConnected()){
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
