package com.touchsoft.java7.core.user;

import com.touchsoft.java7.core.Message;
import com.touchsoft.java7.core.UserList.ChatRoom;
import com.touchsoft.java7.core.UserList.UserList;
import org.apache.log4j.Logger;
import java.util.ArrayList;

public abstract class User {

    static final Logger LOGGER = Logger.getLogger(User.class);

    private String userName;
    private boolean isAgent;
    private boolean isConnected;
    private boolean waitingConnection;
    private User connectUser;
    private ArrayList<Message> outputBufferOfUnsentMessages = new ArrayList<>();
    private ChatRoom chatRoom;


    public abstract void sendMsg (Message msg);


    public void leave(){
        if (this.isConnected()){
            this.getConnectUser().sendMsg(new Message(this.getUserName() + " left the chat."));
            this.getConnectUser().sendMsg(new Message("Please wait for connection "));
            this.setConnected(false);
            this.getConnectUser().setConnected(false);
            this.setWaitingConnection(false);
            this.getConnectUser().setWaitingConnection(true);
            chatRoom.dellChatRoom();
        }
        return;
    }

    public void exit(){
        this.leave();
        UserList.dellUser(this);

    }

    public void addToUserList(){
        UserList.addUser(this);
    }


    public void inputMsg(Message msg) {
        if (this.isConnected()){
            this.getConnectUser().sendMsg(msg);
        } else {
            this.setWaitingConnection(true);
            if (!isAgent){
                this.outputBufferOfUnsentMessages.add(msg);
            }
        }
    }

    public void flushOutputBufferOfUnsentMessages() {
        if (!this.outputBufferOfUnsentMessages.isEmpty()) {
            for (Message msg : this.outputBufferOfUnsentMessages) {
                this.getConnectUser().sendMsg(msg);
            }
            this.outputBufferOfUnsentMessages.clear();
        }
    }


    public static void connectUsers (User user1, User user2){
        LOGGER.info("Connected " + user1.getUserName() + " with " + user2.getUserName());
        ChatRoom chatRoom = new ChatRoom(user1 , user2);
        user1.setConnected(true);
        user2.setConnected(true);
        user1.setWaitingConnection(false);
        user2.setWaitingConnection(false);
        user1.setConnectUser(user2);
        user2.setConnectUser(user1);
        user1.sendMsg(new Message("You are connected to " + user2.getUserName()));
        user2.sendMsg(new Message("You are connected to " + user1.getUserName()));
        user1.flushOutputBufferOfUnsentMessages();
        user2.flushOutputBufferOfUnsentMessages();
    }

    public static User findUser(Boolean isAgent, String userName){
        User user = null;
        if (isAgent){
            for (User u : UserList.getAgentList()){
                if (u.getUserName().equals(userName)){
                    user = u;
                    break;
                }
            }
        } else {
            for (User u : UserList.getClientList()){
                if (u.getUserName().equals(userName)){
                    user = u;
                    break;
                }
            }
        }
        return user;
    }

    public static boolean UserNameIsNotFree(Boolean isAgent, String userName){
        if (isAgent){
            for (User u : UserList.getAgentList()){
                if (u.getUserName().equals(userName)){
                    return true;
                }
            }
        } else {
            for (User u : UserList.getClientList()){
                if (u.getUserName().equals(userName)){
                    return true;
                }
            }
        }
        return false;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAgent() {
        return isAgent;
    }

    public void setAgent(boolean agent) {
        isAgent = agent;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isWaitingConnection() {
        return waitingConnection;
    }

    public void setWaitingConnection(boolean waitingConnection) {
        this.waitingConnection = waitingConnection;
    }

    public User getConnectUser() {
        return connectUser;
    }

    public void setConnectUser(User connectUser) {
        this.connectUser = connectUser;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

}