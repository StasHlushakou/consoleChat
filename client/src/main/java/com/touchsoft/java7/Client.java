package com.touchsoft.java7;

import java.io.IOException;

// Start client class
public class Client {

    private static String ipAddress = "localhost";
    private static int port = 4045;

    //Start console client
    public static void main(String[] args) {
        ClientSocketProperties csp = new ClientSocketProperties(ipAddress, port);

        try {
            // Reading cycle of messages from the console and sending them to the server
            while (!csp.isStopClient()) {
                String userWord = csp.getReadFromConsole().readLine();

                // Check exit
                if (userWord.equals("/e") || userWord.equals("/exit")) {
                    csp.sendMsgToServer(userWord);
                    break;
                }
                // Send msg
                csp.sendMsgToServer(userWord);
            }
            csp.setStopClient(true);
        } catch (IOException e) {
            csp.setStopClient(true);
        }

        csp.clean();

    }
}
