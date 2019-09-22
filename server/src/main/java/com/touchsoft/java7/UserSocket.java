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


    private void sendMsg(String msg){
        try {
            this.out.write(msg + "\n");
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public UserSocket (Socket userSocket){
        this.socket = userSocket;
        userIn = null;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

            this.sendMsg("From registration enter '/reg [a/c] name'" );
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
        user1.getUserSocket().sendMsg("You are connected to " + user2.getUserName());
        user2.getUserSocket().sendMsg("You are connected to " + user1.getUserName());

        if (!user1.getUserSocket().msgList.isEmpty()){
            for(String s : user1.getUserSocket().msgList){
                user2.getUserSocket().sendMsg(s);
            }
            user1.getUserSocket().msgList.clear();
        }
        if (!user2.getUserSocket().msgList.isEmpty()){
            for(String s : user2.getUserSocket().msgList){
                user1.getUserSocket().sendMsg(s);
            }
            user2.getUserSocket().msgList.clear();
        }
        user1.setIsConnected(true);
        user2.setIsConnected(true);
        user1.setWaitingConnection(false);
        user2.setWaitingConnection(false);
        logger.info("Connect User " + user1.getUserName() + " with " + user2.getUserName());

    }

    //разъединение чата двух пользователей (отключение выводных потоков, сброс флага соединения и ссылки на связанного пользователя)
    public void unconnectedUsers(){

        logger.info("Unconnected users " + userIn.getUserName() + " with" + userOut.getUserName());

        userOut.getUserSocket().sendMsg(userOut.getUserName() + " left the chat ");
        userOut.getUserSocket().sendMsg(" Please wait for connection ");

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
                        this.sendMsg("You are registered as " + userIn.getUserName());
                        continue;
                    }
                }

                if ( word.equals("/l")){
                    if (userIn.getIsConnected()){
                        this.unconnectedUsers();
                    }
                    this.sendMsg("You left the chat ");
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
                    msgList.add("(" + dtime + ") " + userIn.getUserName() + ": " + word);

                    userIn.setWaitingConnection(true);
                    continue;
                }

                if (userIn.getIsConnected()){
                    Date time = new Date(); // текущая дата
                    SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
                    String dtime = dt1.format(time); // время
                    userOut.getUserSocket().sendMsg("(" + dtime + ") " + userIn.getUserName() + ": " + word);
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
