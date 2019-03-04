package org.karol172.ChatApp;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.karol172.ChatApp.component.TokenGenerator;
import org.karol172.ChatApp.model.User;
import org.karol172.ChatApp.repository.UserDao;
import org.karol172.ChatApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @After
    public void setUp () {
        userService.setAllUsersAsInactive();
        userService.removeInactiveUser();
    }

    @Test
    public void shouldCreateUser () {
        String token = new TokenGenerator().generateToken();
        userService.createUser("exampleNick", token);
        assertTrue(userService.authenticateUser("exampleNick", token));
    }

    @Test
    public void shouldRemoveUser () {
        String token = new TokenGenerator().generateToken();
        userService.createUser("exampleNick", token);
        assertTrue(userService.authenticateUser("exampleNick", token));
        userService.removeUser("exampleNick");
        assertFalse(userService.authenticateUser("exampleNick", token));
    }

    @Test
    public void shouldRemoveInactiveUser () {
        String token = new TokenGenerator().generateToken();
        String token2 = new TokenGenerator().generateToken();
        userService.createUser("exampleNick", token);
        userService.createUser("exampleNick2", token2);

        assertTrue(userService.authenticateUser("exampleNick", token));
        assertTrue(userService.authenticateUser("exampleNick2", token2));

        userService.setActiveForUser("exampleNick", false);
        userService.removeInactiveUser();
        assertFalse(userService.authenticateUser("exampleNick", token));

        userService.createUser("exampleNick", token);
        userService.setAllUsersAsInactive();
        userService.removeInactiveUser();

        assertFalse(userService.authenticateUser("exampleNick", token));
        assertFalse(userService.authenticateUser("exampleNick2", token2));

    }


}
