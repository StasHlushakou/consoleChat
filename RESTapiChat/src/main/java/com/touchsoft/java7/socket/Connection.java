package com.touchsoft.java7.socket;

import com.touchsoft.java7.core.user.UserSocket;
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Connection {

    private Socket socket ;
    private BufferedReader readerUserMsg;
    private BufferedWriter writerUserMsg;
    private UserSocket user;
    private SocketListenerThread socketListenerThread;

    static final Logger LOGGER = Logger.getLogger(Connection.class);

    public Connection (Socket socket){
        this.socket = socket;
        user = null;
        try {
            this.readerUserMsg = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.writerUserMsg = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException e){
            LOGGER.error(e + " in constructor Connection.");
        }
        socketListenerThread = new SocketListenerThread(this);
        LOGGER.info("Socket connection created ");
    }


    public void closeConnection(){
        try{
            if (readerUserMsg != null)
                readerUserMsg.close();
            if (writerUserMsg != null)
                writerUserMsg.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            LOGGER.error(e + " in closeConnection.");
        }
        LOGGER.info("Socket connection close");
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setReaderUserMsg(BufferedReader readerUserMsg) {
        this.readerUserMsg = readerUserMsg;
    }

    public void setWriterUserMsg(BufferedWriter writerUserMsg) {
        this.writerUserMsg = writerUserMsg;
    }

    public void setUser(UserSocket user) {
        this.user = user;
    }

    public void setSocketListenerThread(SocketListenerThread socketListenerThread) {
        this.socketListenerThread = socketListenerThread;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getReaderUserMsg() {
        return readerUserMsg;
    }

    public BufferedWriter getWriterUserMsg() {
        return writerUserMsg;
    }

    public UserSocket getUser() {
        return user;
    }

    public SocketListenerThread getSocketListenerThread() {
        return socketListenerThread;
    }
}
