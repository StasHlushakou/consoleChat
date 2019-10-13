package com.touchsoft.java7.spring.gson;

import com.touchsoft.java7.core.UserList.ChatRoom;
import com.touchsoft.java7.core.UserList.UserList;
import com.touchsoft.java7.core.user.User;
import com.touchsoft.java7.core.user.UserREST;
import com.touchsoft.java7.core.user.UserWeb;

import java.util.ArrayList;

public class UserDTO {
    private String name;
    private String state;
    private String connectUserName;
    private Integer chatId;
    private String connectionType;

    private UserDTO(User user){
        this.name = user.getUserName();
    }

    private UserDTO(User user, String detail){
        this.name = user.getUserName();
        if (user.isConnected()){
            this.state = "Connected";
            this.connectUserName = user.getConnectUser().getUserName();
            this.chatId = user.getChatRoom().getId();
        } else if (user.isWaitingConnection()){
            this.state = "WaitingConnection";
            this.connectUserName = null;
        } else {
            this.state = "Leave";
            this.connectUserName = null;
        }
        if (user instanceof UserREST){
            this.connectionType = "REST";
        } else if (user instanceof UserWeb){
            this.connectionType = "WebSocket";
        } else {
            this.connectionType = "Socket";
        }
    }

    public static ArrayList<UserDTO> getAgents(){
        ArrayList<UserDTO> list = new ArrayList<>();
        for (User user : UserList.getAgentList()){
            list.add(new UserDTO(user));
        }
        return list;
    }

    public static ArrayList<UserDTO> getFreeAgents(){
        ArrayList<UserDTO> list = new ArrayList<>();
        for (User user : UserList.getAgentList()){
            if (!user.isConnected() && user.isWaitingConnection()) {
                list.add(new UserDTO(user));
            }
        }
        return list;
    }

    public static UserDTO getAgentDetail(String name){
        for (User user : UserList.getAgentList()){
            if (user.getUserName().equals(name)){
                return new UserDTO(user,"detail");
            }
        }
        return null;
    }

    public static Integer getAgentsFreeNum(){
        int countFreeAgent = 0;
        for (User user : UserList.getAgentList()){
            if (!user.isConnected() && user.isWaitingConnection()) {
                countFreeAgent++;
            }
        }
        return countFreeAgent;
    }

    public static ArrayList<UserDTO> getClientsInqQueue(){
        ArrayList<UserDTO> list = new ArrayList<>();
        for (User user : UserList.getClientList()){
            if (!user.isConnected() && user.isWaitingConnection()) {
                list.add(new UserDTO(user));
            }
        }
        return list;
    }

    public static UserDTO getClientDetail(String name){
        for (User user : UserList.getClientList()){
            if (user.getUserName().equals(name)){
                return new UserDTO(user,"detail");
            }
        }
        return null;
    }

}
