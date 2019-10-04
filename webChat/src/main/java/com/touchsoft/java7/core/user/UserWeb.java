package com.touchsoft.java7.core.user;

import org.apache.log4j.Logger;

import javax.websocket.Session;
import java.io.IOException;

public class UserWeb extends User {

    static final Logger LOGGER = Logger.getLogger(UserWeb.class);
    Session session;


    public UserWeb(Boolean isAgent, String userName, Session session){
        super();
        this.setUserName(userName);
        this.setAgent(isAgent);
        this.setConnected(false);
        this.setWaitingConnection(true);
        this.session = session;
        this.addToUserList();
        this.sendMsg("You are registered as " + this.getUserName());
        LOGGER.info("Create " + this.getUserName());

    }

    @Override
    public void sendMsg(String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {

    }





    public Session getSession () {
        return session;
    }

    public void setSession(Session session){
        this.session = session;
    }


}
