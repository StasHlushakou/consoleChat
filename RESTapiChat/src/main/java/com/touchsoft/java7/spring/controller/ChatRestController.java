package com.touchsoft.java7.spring.controller;

import com.touchsoft.java7.core.Message;
import com.touchsoft.java7.core.user.User;
import com.touchsoft.java7.core.user.UserREST;
import com.touchsoft.java7.spring.DTO.ChatDTO;
import com.touchsoft.java7.spring.DTO.UserDTO;
import com.touchsoft.java7.spring.DTO.Util;
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
    public ArrayList<UserDTO> agents(@RequestParam(value="pageNumber",defaultValue = "0") int pageNumber,
                                     @RequestParam(value="pageSize",defaultValue = "0") int pageSize) {

        LOGGER.info("getAgents.");
        return Util.getAgents(pageNumber,pageSize);

    }

    @GetMapping("/agents/free")
    public ArrayList<UserDTO> freeAgents(@RequestParam(value="pageNumber",defaultValue = "0") int pageNumber,
                                         @RequestParam(value="pageSize",defaultValue = "0") int pageSize) {

        LOGGER.info("getFreeAgents.");
        return Util.getFreeAgents(pageNumber,pageSize);

    }

    @GetMapping("/agent/detail")
    public UserDTO agentDetail(@RequestParam(value="name") String name) {

        LOGGER.info("getAgentDetail.");
        return Util.getAgentDetail(name);


    }

    @GetMapping("/agents/free/num")
    public Integer numFreeAgents() {

        LOGGER.info("getAgentsFreeNum.");
        return Util.getAgentsFreeNum();

    }

    @GetMapping("/chats")
    public ArrayList<ChatDTO> chats(@RequestParam(value="pageNumber",defaultValue = "0") int pageNumber,
                                    @RequestParam(value="pageSize",defaultValue = "0") int pageSize) {

        LOGGER.info("getChats.");
        return Util.getChats(pageNumber,pageSize);

    }

    @GetMapping("/chat/detail")
    public ChatDTO getChatsDetail(@RequestParam(value="chatId") int chatId) {

        LOGGER.info("getAgentDetail.");
        return Util.getChatsDetail(chatId);


    }

    @GetMapping("/clients/inQueue")
    public ArrayList<UserDTO> clientsInqQeue(@RequestParam(value="pageNumber",defaultValue = "0") int pageNumber,
                                             @RequestParam(value="pageSize",defaultValue = "0") int pageSize) {
        return Util.getClientsInqQueue(pageNumber,pageSize);
    }

    @GetMapping("/client/detail")
    public UserDTO clientDetail(@RequestParam(value="name") String name) {

        LOGGER.info("getClientDetail.");
        return Util.getClientDetail(name);

    }










    @PostMapping("/regAgent")
    public void regAgent( @RequestParam(value="name") String userName ) {

        if (User.UserNameIsNotFree(true, userName)) {
            return;
        }

        new UserREST(true , userName);
    }

    @PostMapping("/regClient")
    public void regClient( @RequestParam(value="name") String userName ) {

        if (User.UserNameIsNotFree(false, userName)) {
            return;
        }

        new UserREST(false , userName);
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

        boolean isAgent;
        if (type.equals("agent") || type.equals("client")){
            isAgent = type.equals("agent");
        } else {
            return null;
        }

        User user = User.findUser(isAgent , name);
        if (!(user instanceof UserREST)) {
            return null;
        }

        return ((UserREST) user).getUnreadMessage();

    }

    @PostMapping("/exit")
    public void exit(@RequestParam(value="name") String name,
                     @RequestParam(value="type") String type) {

        boolean isAgent;
        if (type.equals("agent") || type.equals("client")){
            isAgent = type.equals("agent");
        } else {
            return;
        }

        User user = User.findUser(isAgent , name);
        if (!(user instanceof UserREST)) {
            return ;
        }

        user.exit();

    }


}


