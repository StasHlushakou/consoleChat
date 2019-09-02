package com.touchsoft.java7;

import java.util.ArrayList;

public class CreateConnection extends Thread {

    private static ArrayList<User> clientList;
    private static ArrayList<User> agentList;

    public void addClient(User user){
        clientList.add(user);
        for (User u : agentList){
            if (!u.getIsConect()){
                user.connectUsers(u);
                System.out.println("connecting");
            }
        }
    }

    public void addAgent(User user){
        agentList.add(user);
        for (User u : clientList){
            if (!u.getIsConect()){
                user.connectUsers(u);
                System.out.println("connecting");
            }
        }
    }


    public CreateConnection(){
        clientList = new ArrayList<>();
        agentList = new ArrayList<>();
        //start();
    }


    /*
    @Override
    public void run(){

        while (true) {
            for (User uClient : clientList) {
                if (!uClient.getIsConect()) {
                    for (User uAgent : agentList) {
                        if (!uAgent.getIsConect()) {
                            uClient.connectUsers(uAgent);
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

    */
}
