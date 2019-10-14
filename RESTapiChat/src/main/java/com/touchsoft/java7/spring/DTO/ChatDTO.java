package com.touchsoft.java7.spring.DTO;

import com.touchsoft.java7.core.UserList.ChatRoom;

public class ChatDTO {

    private Integer id;
    private String clientName;
    private String agentName;

    public ChatDTO(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.clientName = chatRoom.getClient().getUserName();
        this.agentName = chatRoom.getAgent().getUserName();

    }


}
