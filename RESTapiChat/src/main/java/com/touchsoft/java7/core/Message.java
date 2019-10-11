package com.touchsoft.java7.core;

import com.touchsoft.java7.core.user.User;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private String time;
    private String from;
    private String text;


    public Message(User fromUser, String text) {

        this.time = new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date());
        this.from = fromUser.getUserName();
        this.text = text;
    }

    public Message(String text) {

        this.time = new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date());
        this.from = "Server";
        this.text = text;
    }

    public String messageToString(){
        return (this.time + " " + this.from + ": " + this.text);
    }

}
