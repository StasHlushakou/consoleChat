package com.touchsoft.java7.core.user;

import com.touchsoft.java7.core.Message;
import org.apache.log4j.Logger;
import java.util.ArrayList;

public class UserREST extends User {


    static final Logger LOGGER = Logger.getLogger(UserREST.class);


    private ArrayList<Message> unreadMessageInputBuffer = new ArrayList<>();

    public UserREST(Boolean isAgent, String userName) {
        this.setUserName(userName);
        this.setAgent(isAgent);
        this.setConnected(false);
        this.setWaitingConnection(true);
        this.addToUserList();
        this.sendMsg(new Message("You are registered as " + this.getUserName()));
        LOGGER.info("Create " + (isAgent ? "agent " : "client ") + this.getUserName());

    }

    public ArrayList<Message> getUnreadMessage(){
        ArrayList<Message> list = new ArrayList<>(unreadMessageInputBuffer);
        unreadMessageInputBuffer.clear();
        return list;
    }


    @Override
    public void sendMsg(Message msg) {
        unreadMessageInputBuffer.add(msg);
    }

}
