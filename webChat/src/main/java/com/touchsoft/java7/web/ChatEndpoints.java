package com.touchsoft.java7.web;

import com.touchsoft.java7.core.user.UserWeb;
import com.touchsoft.java7.core.UserList.UserList;
import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Create WebSocket
@ServerEndpoint(value = "/chat")
public class ChatEndpoints {

    static final Logger LOGGER = Logger.getLogger(ChatEndpoints.class);

    private Session session ;
    private UserWeb user ;

    private Pattern pattern = Pattern.compile("/reg\\s[ac]\\s.+");
    private Matcher matcher ;


    @OnOpen
    public void onOpen (Session session){
        this.session = session;
        LOGGER.info("New WebSocket connection open.");

    }

    @OnClose
    public void onClose (Session session){
        if (user != null){
            if (user.isConnected()){
                user.getConnectUser().sendMsg(user.getUserName() + " left the chat ");
                user.getConnectUser().sendMsg("Please wait for connection ");
                user.setConnected(false);
                user.getConnectUser().setConnected(false);
                user.setWaitingConnection(false);
                user.getConnectUser().setWaitingConnection(true);
            }
            UserList.dellUser(user);
            user = null;
        }
        LOGGER.info("WebSocket connection close.");

    }

    @OnError
    public void onError (Session session,Throwable throwable){
        if (user != null){
            if (user.isConnected()){
                user.getConnectUser().sendMsg(user.getUserName() + " left the chat ");
                user.getConnectUser().sendMsg("Please wait for connection ");
                user.setConnected(false);
                user.getConnectUser().setConnected(false);
                user.setWaitingConnection(false);
                user.getConnectUser().setWaitingConnection(true);
            }
            UserList.dellUser(user);
            user = null;
        }
        LOGGER.error(throwable + "in WebSocket connection.");
    }

    @OnMessage
    public void onMessage(Session session, String msg){

        if (user == null){
            matcher = pattern.matcher(msg);
            if (!matcher.matches()){
                return;
            }
            else {
                boolean isAgent;
                if (msg.substring(5,6).equals("a")){
                    isAgent = true;
                } else {
                    isAgent = false;
                }
                String userName = msg.substring(7);
                user = new UserWeb(isAgent, userName, this.session);
                return;
            }
        }

        if ( msg.equals("/l")){
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

        if (msg.equals("/e")){
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

        Date time = new Date(); // текущая дата
        SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
        String dtime = dt1.format(time); // время
        user.inputMsg("(" + dtime + ") " + user.getUserName() + ": " + msg);
    }

}
