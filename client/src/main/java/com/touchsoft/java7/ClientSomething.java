package com.touchsoft.java7;

import java.io.*;
import java.net.Socket;

// Class from reading and writing user msg
public class ClientSomething {

    private Socket          socket;
    private BufferedReader  in;
    private BufferedWriter  out;
    private BufferedReader  inputUser;

    // Constructor
    public ClientSomething(String addr, int port) {

        // Connectivity wait loop
        this.socket = null;
        while (this.socket == null){
            try {
                this.socket = new Socket(addr, port);
            }catch (IOException e){}
        }

        // Creating I/O-stream
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Starting threads
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            ClientSomething.this.downService();
            System.out.println("Exception in constructor ClientSomthing");
        }
    }

    // Resource cleaning
    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }

    // Thread for reading messages from the server and writing this to console
    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String msg;
            try {

                // Message waiting loop
                while (true) {
                    msg = in.readLine();
                    if (msg == null){
                        continue;
                    }
                    // Write msg to console
                    System.out.println(msg);
                }
            } catch (IOException e) {
                ClientSomething.this.downService();
            }
        }
    }

    // Thread sending messages from the console to server
    public class WriteMsg extends Thread {
        @Override
        public void run() {
            try {

                // Reading cycle of messages from the console and sending them to the server
                while (true) {
                    String userWord = inputUser.readLine();

                    // Check exit
                    if (userWord.equals("/e")) {
                        out.write(userWord + "\n");
                        out.flush();
                        break;
                    }

                    // Send msg
                    out.write(userWord + "\n");
                    out.flush();
                }
                ClientSomething.this.downService();
            } catch (IOException e) {
                ClientSomething.this.downService();
            }
        }
    }
}