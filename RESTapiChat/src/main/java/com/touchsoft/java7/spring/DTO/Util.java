package com.touchsoft.java7.spring.DTO;

import com.touchsoft.java7.core.UserList.ChatRoom;
import com.touchsoft.java7.core.UserList.UserList;
import com.touchsoft.java7.core.user.User;
import java.util.ArrayList;

public class Util {

    public static ArrayList<ChatDTO> getChats(int pageNumber, int pageSize){
        ArrayList<ChatDTO> list = new ArrayList<>();

        int pageNumberCount = 0;
        int pageSizeCount = 0;
        for (ChatRoom chat : ChatRoom.getchatRoomList()){
            if (pageNumberCount == pageNumber || pageSize == 0){
                list.add(new ChatDTO(chat));
            }
            pageSizeCount++;
            if (pageSizeCount == pageSize){
                pageNumberCount++;
                pageSizeCount = 0;
            }
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

    public static ArrayList<UserDTO> getAgents(int pageNumber, int pageSize){
        ArrayList<UserDTO> list = new ArrayList<>();

        int pageNumberCount = 0;
        int pageSizeCount = 0;
        for (User user : UserList.getAgentList()){
            if (pageNumberCount == pageNumber || pageSize == 0){
                list.add(new UserDTO(user, false));
            }
            pageSizeCount++;
            if (pageSizeCount == pageSize){
                pageNumberCount++;
                pageSizeCount = 0;
            }
        }

        return list;
    }

    public static ArrayList<UserDTO> getFreeAgents(int pageNumber, int pageSize){
        ArrayList<UserDTO> list = new ArrayList<>();

        int pageNumberCount = 0;
        int pageSizeCount = 0;
        for (User user : UserList.getAgentList()){
            if ((!user.isConnected() && user.isWaitingConnection())
                    &&(pageNumberCount == pageNumber || pageSize == 0)){
                list.add(new UserDTO(user, false));
            }
            pageSizeCount++;
            if (pageSizeCount == pageSize){
                pageNumberCount++;
                pageSizeCount = 0;
            }

        }

        return list;
    }

    public static UserDTO getAgentDetail(String name){
        for (User user : UserList.getAgentList()){
            if (user.getUserName().equals(name)){
                return new UserDTO(user,true);
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

    public static ArrayList<UserDTO> getClientsInqQueue(int pageNumber, int pageSize){
        ArrayList<UserDTO> list = new ArrayList<>();

        int pageNumberCount = 0;
        int pageSizeCount = 0;
        for (User user : UserList.getClientList()){
            if (pageNumberCount == pageNumber || pageSize == 0){
                list.add(new UserDTO(user,false));
            }
            pageSizeCount++;
            if (pageSizeCount == pageSize){
                pageNumberCount++;
                pageSizeCount = 0;
            }

        }

        return list;
    }

    public static UserDTO getClientDetail(String name){
        for (User user : UserList.getClientList()){
            if (user.getUserName().equals(name)){
                return new UserDTO(user,true);
            }
        }
        return null;
    }

}
