package com.touchsoft.java7.spring.controller;

import com.touchsoft.java7.core.Message;
import com.touchsoft.java7.core.user.User;
import com.touchsoft.java7.core.user.UserREST;
import com.touchsoft.java7.spring.gson.ChatDTO;
import com.touchsoft.java7.spring.gson.UserDTO;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;




@RestController
public class ChatRestController {
    static final Logger LOGGER = Logger.getLogger(ChatRestController.class);


    @GetMapping("/agents")
    public ArrayList agents() {
        LOGGER.info("getAgents.");
        return UserDTO.getAgents();
    }

    @GetMapping("/agents/free")
    public ArrayList freeAgents() {
        LOGGER.info("getFreeAgents.");
        return UserDTO.getFreeAgents();
    }

    @GetMapping("/agent/detail")
    public UserDTO agentDetail(@RequestParam(value="name") String name) {
        LOGGER.info("getAgentDetail.");
        return UserDTO.getAgentDetail(name);

    }

    @GetMapping("/agents/free/num")
    public Integer numFreeAgents() {
        LOGGER.info("getAgentsFreeNum.");
        return UserDTO.getAgentsFreeNum();
    }

    @GetMapping("/chats")
    public ArrayList chats() {
        LOGGER.info("getChats.");
        return ChatDTO.getChats();
    }

    @GetMapping("/chat/detail")
    public ChatDTO getChatsDetail(@RequestParam(value="chatId") Integer chatId) {
        LOGGER.info("getAgentDetail.");
        return ChatDTO.getChatsDetail(chatId);

    }

    @GetMapping("/clients/inQueue")
    public ArrayList clientsInqQeue() {
        return UserDTO.getClientsInqQueue();
    }

    @GetMapping("/client/detail")
    public UserDTO clientDetail(@RequestParam(value="name") String name) {
        LOGGER.info("getClientDetail.");
        return UserDTO.getClientDetail(name);
    }















    @PostMapping("/regAgent")
    public void regAgent( @RequestParam(value="name") String userNamename ) {
        new UserREST(true , userNamename);
    }

    @PostMapping("/regClient")
    public void regClient( @RequestParam(value="name") String userNamename ) {
        new UserREST(false , userNamename);
    }

    @PostMapping("/sendMsgToAgent")
    public void sendMsgToAgent(@RequestParam(value="name") String name,
                               @RequestParam(value="message") String message) {

        User client = User.findUser(false, name);

        if (!(client instanceof UserREST)) {
            return;
        }

        client.inputMsg(new Message(client,message));

    }

    @PostMapping("/sendMsgToClient")
    public void sendMsgToClient(@RequestParam(value="name") String name,
                                @RequestParam(value="message") String message) {

        User agent = User.findUser(true, name);

        if (!(agent instanceof UserREST)) {
            return;
        }

        agent.inputMsg(new Message(agent,message));
    }

    @PostMapping("/getNewMessages")
    public ArrayList getNewMessages(@RequestParam(value="name") String name,
                                    @RequestParam(value="type") String type) {

        boolean isAgent = type.equals("agent");
        User user = User.findUser(isAgent , name);

        if (!(user instanceof UserREST)) {
            return null;
        }

        return ((UserREST) user).getUnreadMessage();

    }

    @PostMapping("/exit")
    public void exit(@RequestParam(value="name") String name,
                     @RequestParam(value="type") String type) {

        boolean isAgent = type.equals("agent");
        User user = User.findUser(isAgent , name);

        if (!(user instanceof UserREST)) {
            return ;
        }

        user.exit();

    }


}


