package com.touchsoft.java7.core.user;

import com.touchsoft.java7.core.Message;
import org.apache.log4j.Logger;
import java.io.BufferedWriter;
import java.io.IOException;

public class UserSocket extends User {

    static final Logger LOGGER = Logger.getLogger(UserSocket.class);
    private BufferedWriter outMsgToUser;


    public UserSocket(Boolean isAgent, String userName, BufferedWriter out){
        this.setUserName(userName);
        this.setAgent(isAgent);
        this.setConnected(false);
        this.setWaitingConnection(true);
        this.setOutMsgToUser(out);
        this.addToUserList();
        this.sendMsg(new Message("You are registered as " + this.getUserName()));
        LOGGER.info("Create " + (isAgent ? "agent " : "client ") + this.getUserName());

    }

    @Override
    public void sendMsg(Message msg) {
        try {
            this.getOutMsgToUser().write(msg.messageToString() +"\n");
            this.getOutMsgToUser().flush();
        } catch (IOException e) {
            LOGGER.error(e + " in sendMsg to " + this.getUserName() );

        }
    }








    public  BufferedWriter getOutMsgToUser () {
        return this.outMsgToUser;
    }

    public void setOutMsgToUser(BufferedWriter out){
        this.outMsgToUser = out;
    }


}
