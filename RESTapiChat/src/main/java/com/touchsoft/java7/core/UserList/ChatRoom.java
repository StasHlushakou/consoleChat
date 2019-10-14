package com.touchsoft.java7.core.UserList;

import com.touchsoft.java7.core.user.User;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatRoom {

    static final Logger LOGGER = Logger.getLogger(ChatRoom.class);


    private static CopyOnWriteArrayList<ChatRoom> chatRoomList = new CopyOnWriteArrayList<>();
    private static int idCount = 1;

    private int id;
    private User client;
    private User agent;


    public ChatRoom(User user1, User user2) {
        this.id = idCount;
        idCount++;
        if (user1.isAgent()){
            this.client = user2;
            this.agent = user1;
        } else {
            this.client = user1;
            this.agent = user2;
        }

        user1.setChatRoom(this);
        user2.setChatRoom(this);
        chatRoomList.add(this);
        LOGGER.info("New chat room with id " + this.id + " created.");

    }

    public void dellChatRoom() {
        this.client.setChatRoom(null);
        this.agent.setChatRoom(null);
        chatRoomList.remove(this);
        LOGGER.info("Chat room with id " + this.id + " deleted.");
    }

    public static ArrayList<ChatRoom> getchatRoomList (){
        return new ArrayList<>(chatRoomList);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }
}
