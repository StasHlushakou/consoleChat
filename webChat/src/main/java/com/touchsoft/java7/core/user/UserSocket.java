package com.touchsoft.java7.core.user;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;

public class UserSocket extends User {

    static final Logger LOGGER = Logger.getLogger(UserSocket.class);
    private BufferedWriter outMsgToUser;


    public UserSocket(Boolean isAgent, String userName, BufferedWriter out){
        super();
        this.setUserName(userName);
        this.setAgent(isAgent);
        this.setConnected(false);
        this.setWaitingConnection(true);
        this.setOutMsgToUser(out);
        this.addToUserList();
        this.sendMsg("You are registered as " + this.getUserName());
        LOGGER.info("Create " + this.getUserName());

    }

    @Override
    public void sendMsg(String msg) {
        try {
            this.getOutMsgToUser().write(msg +"\n");
            this.getOutMsgToUser().flush();
        } catch (IOException e) {
            LOGGER.error(e + " in sendMsg to " + this.getUserName() );

        }
    }

    @Override
    public void disconnect() {

    }







    public  BufferedWriter getOutMsgToUser () {
        return this.outMsgToUser;
    }

    public void setOutMsgToUser(BufferedWriter out){
        this.outMsgToUser = out;
    }


}
