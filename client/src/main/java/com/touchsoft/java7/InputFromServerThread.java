package com.touchsoft.java7;

import java.io.IOException;

public class InputFromServerThread extends Thread{

    private ClientSocketProperties csp;

    public InputFromServerThread(ClientSocketProperties csp) {
        this.csp = csp;
        start();
    }

    // Thread for reading messages from the server and writing this to console
    @Override
    public void run() {
        String msg;
        try {

            // Message waiting loop
            while (!csp.isStopClient()) {
                msg = csp.getReadFromServer().readLine();
                if (msg == null){
                    continue;
                }
                // Write msg to console
                System.out.println(msg);
            }
        } catch (IOException e) {
            csp.setStopClient(true);
        }
    }

}
