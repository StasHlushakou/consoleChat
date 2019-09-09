package com.touchsoft.java7;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.Socket;

import static org.junit.Assert.*;

public class UserTest {


    @Before
    public void setUp() throws Exception {
        //Socket socketMock = Mockito.mock(Socket.class);
        //User userMock = Mockito.mock(User.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void exitUser() {
        UserList userListMock = Mockito.mock(UserList.class);
        User userMock = Mockito.mock(User.class);


        //Mockito.doReturn(userListMock.dellUser(userMock)).when(userMock.exitUser()).void;
    }

    @Test
    public void connectUsers() {
    }

    @Test
    public void unconnectedUsers() {
    }
}