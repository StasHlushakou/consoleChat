package com.touchsoft.java7;


import org.apache.log4j.Logger;

import java.net.Socket;
import java.util.ArrayList;

public class UserList extends Thread {
    private static Logger logger = Logger.getLogger(UserList.class);

    private static ArrayList<User> clientList;
    private static ArrayList<User> agentList;


    // Создаёт нового пользователя и добавляет его в нужную коллекцию
    public static void addUser(User user){

        if (user.getIsAgent()){
            agentList.add(user);
        } else {
            clientList.add(user);
        }
        logger.info(user.getUserName() + " and add to List");
    }

    // Удаляет пользователя из нужной коллекции
    public static void dellUser(User user){
        if (user.getIsAgent()){
            agentList.remove(user);
        } else {
            clientList.remove(user);
        }
        logger.info("dellUser " + user.getUserName());
    }

    // Конструктор, инициализирует коллекции и запускает нить проверки пользователей, готовых к подключению
    public UserList(){
        clientList = new ArrayList<>();
        agentList = new ArrayList<>();
        //logger.info("Create UserList");
        start();
    }


    @Override
    public void run(){
        //logger.info("Start thread UserList");

        while (true) {

            for (User userClient : clientList) {
                if (!(userClient.getIsConnected()) && userClient.getWaitingConnection()) {
                    for (User userAgent : agentList) {
                        if ((!userAgent.getIsConnected()) && userAgent.getWaitingConnection()) {
                            userClient.connectUsers(userAgent);
                            break;
                        }
                    }
                }
            }
            try {
                sleep(500);
            }catch (InterruptedException e){
                logger.error(e + " in thread UserList.");
            }
        }
    }
}
