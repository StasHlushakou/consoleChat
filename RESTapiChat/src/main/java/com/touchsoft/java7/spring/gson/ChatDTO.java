package com.touchsoft.java7.spring.gson;

import com.touchsoft.java7.core.UserList.ChatRoom;

import java.util.ArrayList;

public class ChatDTO {

    private Integer id;
    private String clientName;
    private String agentName;

    public ChatDTO(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.clientName = chatRoom.getClient().getUserName();
        this.agentName = chatRoom.getAgent().getUserName();

    }

    public static ArrayList<ChatDTO> getChats(){
        ArrayList<ChatDTO> list = new ArrayList<>();
        for (ChatRoom chat : ChatRoom.getchatRoomList()){
            list.add(new ChatDTO(chat));
        }
        return list;
    }

    public static ChatDTO getChatsDetail(Integer chatId){
        ArrayList<ChatDTO> list = new ArrayList<>();
        for (ChatRoom chat : ChatRoom.getchatRoomList()){
            if (chat.getId() == chatId){
                return new ChatDTO(chat);
            }
        }
        return null;
    }
}
