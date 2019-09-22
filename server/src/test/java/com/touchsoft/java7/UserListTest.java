package com.touchsoft.java7;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;


public class UserListTest {

    UserSocket us;
    User agentUser;
    User clientUser;
    UserList userList;

    @Before
    public void setUp() throws Exception {

        us = Mockito.mock(UserSocket.class);
        agentUser = new User(true, "agent", us);
        clientUser = new User(false, "client", us);
        userList = new UserList();

    }

    @Test
    public void userInListReturn() {
        UserList.addUser(clientUser);
        UserList.addUser(agentUser);
        ArrayList<User> expected = new ArrayList<>();
        expected.add(clientUser);
        expected.add(agentUser);
        ArrayList<User> actual = UserList.userInListReturn();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void addUserAgent() {
        UserList.addUser(agentUser);
        ArrayList<User> expected = new ArrayList<>();
        ArrayList<User> actual = UserList.userInListReturn();
        expected.add(agentUser);

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void addUserClient() {
        UserList.addUser(clientUser);
        ArrayList<User> expected = new ArrayList<>();
        ArrayList<User> actual = UserList.userInListReturn();
        expected.add(clientUser);

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void dellUserAgent() {
        UserList.addUser(agentUser);
        UserList.addUser(clientUser);
        UserList.dellUser(agentUser);
        ArrayList<User> expected = new ArrayList<>();
        ArrayList<User> actual = UserList.userInListReturn();
        expected.add(clientUser);

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void dellUserClient() {
        UserList.addUser(agentUser);
        UserList.addUser(clientUser);
        UserList.dellUser(clientUser);
        ArrayList<User> expected = new ArrayList<>();
        ArrayList<User> actual = UserList.userInListReturn();
        expected.add(agentUser);

        Assert.assertEquals(expected,actual);
    }

}