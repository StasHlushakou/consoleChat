package com.touchsoft.java7;

import java.io.*;
import java.net.Socket;

// Class from reading and writing user msg
public class ClientSocketProperties {

    private Socket          socket;
    private BufferedReader  readFromServer;
    private BufferedWriter  WriteToServer;
    private BufferedReader  readFromConsole;
    private boolean         stopClient;


    // Constructor
    public ClientSocketProperties(String addr, int port) {

        // Connectivity wait loop
        while (this.socket == null){
            try {
                this.socket = new Socket(addr, port);
            }catch (IOException e){}
        }

        // Creating I/O-stream
        try {
            readFromConsole = new BufferedReader(new InputStreamReader(System.in));
            readFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            WriteToServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Starting InputFromServerThread
            new InputFromServerThread(this);
        } catch (IOException e) {
            System.out.println("Exception in constructor ClientSomething");
        }
    }

    // Resource cleaning
    public void clean() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                readFromServer.close();
                WriteToServer.close();
            }
        } catch (IOException ignored) {}
    }




    public void sendMsgToServer(String msg){
        try {
            WriteToServer.write(msg + "\n");
            WriteToServer.flush();
        } catch (IOException e) {
            System.out.println("Error sending message");
        }

    }





    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getReadFromServer() {
        return readFromServer;
    }

    public void setReadFromServer(BufferedReader readFromServer) {
        this.readFromServer = readFromServer;
    }

    public BufferedWriter getWriteToServer() {
        return WriteToServer;
    }

    public void setWriteToServer(BufferedWriter writeToServer) {
        WriteToServer = writeToServer;
    }

    public BufferedReader getReadFromConsole() {
        return readFromConsole;
    }

    public void setReadFromConsole(BufferedReader readFromConsole) {
        this.readFromConsole = readFromConsole;
    }

    public boolean isStopClient() {
        return stopClient;
    }

    public void setStopClient(boolean stopClient) {
        this.stopClient = stopClient;
    }

}