package com.touchsoft.java7.core;

import com.touchsoft.java7.core.UserList.UserList;
import com.touchsoft.java7.core.user.User;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputMsgHandler {

    private static Pattern pattern = Pattern.compile("/reg\\s[ac]\\s.+");
    private  static Matcher matcher ;

    public static void inputMsgHandler(User user, String msg){


        if ( msg.equals("/l") || msg.equals("/leave")){
            InputMsgHandler.leave(user);
            return;
        }
        if (msg.equals("/e") || msg.equals("/exit")){
            InputMsgHandler.exit(user);
            return;
        }

        Date time = new Date(); // текущая дата
        SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
        String dtime = dt1.format(time); // время
        user.inputMsg("(" + dtime + ") " + user.getUserName() + ": " + msg);
    }

    private static void leave(User user){
        if (user.isConnected()){
            user.getConnectUser().sendMsg(user.getUserName() + " left the chat ");
            user.getConnectUser().sendMsg("Please wait for connection ");
            user.setConnected(false);
            user.getConnectUser().setConnected(false);
            user.setWaitingConnection(false);
            user.getConnectUser().setWaitingConnection(true);
                /*user.dellConnection();
                user.getConectUser().dellConnection();*/
        }
        user.sendMsg("You left the chat ");
        return;
    }

    private static void exit(User user){
        if (user.isConnected()){
            user.getConnectUser().sendMsg(user.getUserName() + " left the chat ");
            user.getConnectUser().sendMsg("Please wait for connection ");
            user.setConnected(false);
            user.getConnectUser().setConnected(false);
            user.setWaitingConnection(false);
            user.getConnectUser().setWaitingConnection(true);
                /*user.dellConnection();
                user.getConectUser().dellConnection();*/
        }
        user.sendMsg("Goodbye");
        UserList.dellUser(user);
        user = null;
        return;
    }
}
