package com.touchsoft.java7.web;

import com.touchsoft.java7.core.Message;
import com.touchsoft.java7.core.user.User;
import com.touchsoft.java7.core.user.UserWeb;
import org.apache.log4j.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
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


    public void sendServiceMsg(Message msg){
        try {
            this.session.getBasicRemote().sendText(msg.messageToString());
        } catch (IOException e) {
            LOGGER.error(e + " in sendMsg to WebSocket");
        }
    }


    @OnOpen
    public void onOpen (Session session){
        this.session = session;
        this.sendServiceMsg(new Message("From registration enter '/reg [a/c] name'"));
        LOGGER.info("New WebSocket connection open.");

    }

    @OnClose
    public void onClose (Session session){
        if (user != null){
            user.exit();
            user = null;
        }
        LOGGER.info("WebSocket connection close.");

    }

    @OnError
    public void onError (Session session,Throwable throwable){
        if (user != null){
            user.exit();
            user = null;
        }
        LOGGER.error(throwable + "in WebSocket connection.");
    }

    @OnMessage
    public void onMessage(Session session, String msg){

        if (msg.equals("/e") || msg.equals("/exit")){
            if (user != null){
                user.exit();
                user = null;
            }
            return;
        }

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
                if (User.UserNameIsNotFree(isAgent, userName)){
                    this.sendServiceMsg(new Message("This name is already taken. Choose another."));
                    this.sendServiceMsg(new Message("From registration enter '/reg [a/c] name'"));
                    return;
                }
                user = new UserWeb(isAgent, userName, this.session);
                return;
            }
        }


        if ( msg.equals("/l") || msg.equals("/leave")){
            if (user.isConnected()){
                user.sendMsg(new Message("You left the chat."));
                user.leave();
            }
            return;
        }

        user.inputMsg(new Message(user, msg));
    }

}
