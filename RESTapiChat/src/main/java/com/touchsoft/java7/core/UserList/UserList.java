package com.touchsoft.java7.core.UserList;

import com.touchsoft.java7.core.user.User;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

// Class for storing users, and connecting free users
public class UserList {

    static final Logger LOGGER = Logger.getLogger(UserList.class);

    private static CopyOnWriteArrayList<User> clientList = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<User> agentList = new CopyOnWriteArrayList<>();


    // Create new user and add them to collection
    public static void addUser(User user){
        if (user.isAgent()){
            agentList.add(user);
        } else {
            clientList.add(user);
        }
        LOGGER.info(user.getUserName() + " add to UserList");
    }

    // Dell user from collection
    public static void dellUser(User user){
        if (user.isAgent()){
            agentList.remove(user);
        } else {
            clientList.remove(user);
        }
        LOGGER.info(user.getUserName() + " dell from UserList");
    }

    // Start ConnectUserThread;
    public UserList(){
        new ConnectUserThread();
        LOGGER.info(" create UserList");
    }

    public static ArrayList<User> getClientList(){
        return new ArrayList<>(clientList);
    }

    public static ArrayList<User> getAgentList(){
        return new ArrayList<>(agentList);
    }

}
