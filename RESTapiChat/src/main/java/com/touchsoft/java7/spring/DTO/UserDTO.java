package com.touchsoft.java7.spring.DTO;

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

    public UserDTO(User user, boolean detail){
        if (!detail){
            this.name = user.getUserName();
            return;
        } else {
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
    }


}
