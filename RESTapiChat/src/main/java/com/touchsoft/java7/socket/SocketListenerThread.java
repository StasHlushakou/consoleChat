package com.touchsoft.java7.socket;

import com.touchsoft.java7.core.Message;
import com.touchsoft.java7.core.user.UserSocket;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketListenerThread extends Thread {

    static final Logger LOGGER = Logger.getLogger(SocketListenerThread.class);

    private Connection connection;

    Pattern pattern = Pattern.compile("/reg\\s[ac]\\s.+");
    Matcher matcher ;

    public SocketListenerThread(Connection connection){
        this.connection = connection;
        start();
    }



    @Override
    public void run() {
        LOGGER.info("Start SocketListenerThread");
        try {
            while (true) {

                String msg = connection.getReaderUserMsg().readLine();
                if (msg == null){
                    continue;
                }

                if (msg.equals("/e") || msg.equals("/exit")){
                    if (connection.getUser() != null){
                        connection.getUser().exit();
                    }if (connection.getUser() != null){
                        connection.getUser().exit();
                    }
                    break;
                }

                if (connection.getUser() == null){
                    matcher = pattern.matcher(msg);
                    if (!matcher.matches()){
                        continue;
                    }
                    else {
                        Boolean isAgent;
                        if (msg.substring(5,6).equals("a")){
                            isAgent = true;
                        } else {
                            isAgent = false;
                        }
                        String userName = msg.substring(7);
                        connection.setUser(new UserSocket(isAgent, userName, connection.getWriterUserMsg()));
                        continue;
                    }
                }

                if ( msg.equals("/l") || msg.equals("/leave")){
                    if (connection.getUser().isConnected()){
                        connection.getUser().sendMsg(new Message("You left the chat."));
                        connection.getUser().leave();
                    }
                    continue;
                }

                connection.getUser().inputMsg(new Message(connection.getUser(),msg));
            }

            connection.closeConnection();
        } catch (IOException e) {
            LOGGER.error(e + " in SocketListenerThread.");
            connection.getUser().exit();
            connection.closeConnection();
        }
    }
}