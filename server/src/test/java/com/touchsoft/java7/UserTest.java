package com.touchsoft.java7;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class UserTest {

    UserSocket us;
    User agentUser;


    @Before
    public void setUp() throws Exception {
        us = Mockito.mock(UserSocket.class);
        agentUser = new User(true, "agent", us);
    }

    @Test
    public void getUserName() {
        String expected = "agent";
        String actual = agentUser.getUserName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getIsAgent() {
        boolean actual = agentUser.getIsAgent();
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getIsConnected() {
        boolean actual = agentUser.getIsConnected();
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getWaitingConnection() {
        boolean actual = agentUser.getWaitingConnection();
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getUserSocket() {
        UserSocket actual = agentUser.getUserSocket();
        UserSocket expected = us;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void setWaitingConnection() {
        agentUser.setWaitingConnection(false);
        boolean actual = agentUser.getWaitingConnection();
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void setIsConnected() {
        agentUser.setIsConnected(true);
        boolean actual = agentUser.getWaitingConnection();
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }
}