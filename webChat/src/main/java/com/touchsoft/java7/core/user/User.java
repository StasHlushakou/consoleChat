package com.touchsoft.java7.core.user;

import com.touchsoft.java7.core.UserList.UserList;
import org.apache.log4j.Logger;
import java.util.ArrayList;

public abstract class User {

    static final Logger LOGGER = Logger.getLogger(User.class);
    private String userName;            //имя пользователя
    private boolean isAgent;            //флаг агент
    private boolean isConnected;        //флаг соединентя
    private boolean waitingConnection;  //флаг ожидания подключения
    private User connectUser;
    private ArrayList<String> msgList;

    public abstract void sendMsg (String msg);

    public abstract void disconnect ();


    public void addToUserList(){
        UserList.addUser(this);
    }

    public void unconnectedUsers (){

        this.getConnectUser().sendMsg(this.getUserName() + " left the chat ");
        this.getConnectUser().sendMsg("Please wait for connection ");
        LOGGER.info("Unconnected users " + this.getUserName() + " with" + this.getConnectUser().getUserName());
        this.setConnected(false);
        this.getConnectUser().setConnected(false);
        this.setWaitingConnection(false);
        this.getConnectUser().setWaitingConnection(true);
        this.disconnect();
        this.getConnectUser().disconnect();
    }

    public void inputMsg(String msg) {
        if (this.isConnected()){
            this.getConnectUser().sendMsg(msg);
        } else {
            this.msgList.add(msg);
            this.setWaitingConnection(true);
        }
    }

    public void flushMsgList() {
        if (!this.msgList.isEmpty()) {
            for (String s : this.msgList) {
                this.getConnectUser().sendMsg(s);
            }
            this.msgList.clear();
        }
    }

    public User (){
        msgList = new ArrayList<>();
    }

    public static void connectUsers (User user1, User user2){
        user1.setConnectUser(user2);
        user2.setConnectUser(user1);
        user1.sendMsg("You are connected to " + user2.getUserName());
        user2.sendMsg("You are connected to " + user1.getUserName());
        user1.flushMsgList();
        user2.flushMsgList();
        user1.setConnected(true);
        user2.setConnected(true);
        user1.setWaitingConnection(false);
        user2.setWaitingConnection(false);
        LOGGER.info("Connected users " + user1.getUserName() + " with" + user2.getUserName());
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

}