package com.touchsoft.java7;


import java.net.Socket;
import java.util.ArrayList;

public class UserList extends Thread {

    private static ArrayList<User> clientList;
    private static ArrayList<User> agentList;


    // Создаёт нового пользователя и добавляет его в нужную коллекцию
    public void addUser(Socket userSocket){
        User user = new User(userSocket);
        if (user.getIsAgent()){
            agentList.add(user);
        } else {
            clientList.add(user);
        }
        System.out.println("Добавлен " + user.getName());
    }

    // Удаляет пользователя из нужной коллекции
    public static void dellUser(User user){
        if (user.getIsAgent()){
            agentList.remove(user);
        } else {
            clientList.remove(user);
        }
        System.out.println("Удалён " + user.getName());

    }

    // Конструктор, инициализирует коллекции и запускает нить проверки пользователей, готовых к подключению
    public UserList(){
        clientList = new ArrayList<>();
        agentList = new ArrayList<>();
        start();
        System.out.println("Запущен UserList");
    }


    @Override
    public void run(){

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
                sleep(1000);
            }catch (InterruptedException e){
                System.out.println(e);
            }

        }
    }


}
