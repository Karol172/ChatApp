package org.karol172.ChatApp.service;

import org.karol172.ChatApp.exception.NotFoundException;
import org.karol172.ChatApp.model.User;
import org.karol172.ChatApp.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService {

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean createUser (String nick, String token) {
        if (!userDao.userIsExist(nick)) {
            userDao.addUsername(nick);
            try {
                userDao.setTokenForUser(nick, token);
            } catch (NotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    public void removeUser (String username) {
        userDao.removeUser(username);
    }

    public boolean authenticateUser (String username, String token) {
        try {
            return userDao.compareToken(username, token);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeInactiveUser () {
        userDao.removeInactiveUsername();
    }

    public void setAllUsersAsInactive () {
        userDao.setActiveForAll(false);
    }

    public void setActiveForUser (String username, boolean active) {
        try {
            userDao.setActiveFor(username, active);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
}
