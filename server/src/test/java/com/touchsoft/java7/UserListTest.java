package com.touchsoft.java7;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class UserListTest {


    @Before

    public void setUp() throws Exception {
        UserList userListMock = Mockito.mock(UserList.class);
        User userMock = Mockito.mock(User.class);
        userMock.setIsAgent(true);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addUser() {

    }

    @Test
    public void dellUser() {

        //veryfy().method_call;
    }
}