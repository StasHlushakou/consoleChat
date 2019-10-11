package com.touchsoft.java7.core.user;

import com.touchsoft.java7.core.Message;
import org.apache.log4j.Logger;
import javax.websocket.Session;
import java.io.IOException;

public class UserWeb extends User {

    static final Logger LOGGER = Logger.getLogger(UserWeb.class);
    Session session;


    public UserWeb(Boolean isAgent, String userName, Session session){
        this.setUserName(userName);
        this.setAgent(isAgent);
        this.setConnected(false);
        this.setWaitingConnection(true);
        this.session = session;
        this.addToUserList();
        this.sendMsg(new Message("You are registered as " + this.getUserName()));
        LOGGER.info("Create " + this.getUserName());

    }

    @Override
    public void sendMsg(Message msg) {
        try {
            session.getBasicRemote().sendText(msg.messageToString());
        } catch (IOException e) {
            LOGGER.error(e + " in sendMsg to " + this.getUserName() );
        }
    }


}
