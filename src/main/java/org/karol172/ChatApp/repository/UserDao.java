package org.karol172.ChatApp.repository;

import org.karol172.ChatApp.exception.NotFoundException;
import org.karol172.ChatApp.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.TreeSet;

@Repository
public class UserDao {

    private Collection<User> users;

    public UserDao() {
        this.users = new TreeSet<>();
    }

    public boolean userIsExist (String username) {
        return users.contains(User.valueOf(username));
    }

    public void addUsername (String username) {
        users.add(User.valueOf(username));
    }

    public void removeUser (String username) {
        users.remove(User.valueOf(username));
    }

    public void removeInactiveUsername () {
        users.removeIf(user -> !user.isActive());
    }

    public void setActiveFor (String username, boolean active) throws NotFoundException {
        findUser(username).setActive(active);
    }

    public void setActiveForAll (boolean active) {
        users.forEach(user -> user.setActive(active));
    }

    public boolean compareToken (String username, String token) throws NotFoundException {
        return findUser(username).getToken().equals(token);
    }

    public void setTokenForUser (String username, String token) throws NotFoundException {
        findUser(username).setToken(token);
    }

    private User findUser (String username) throws NotFoundException{
        Optional<User> user = users.stream().filter(u -> u.getName().equals(username)).findFirst();
        if (user.isPresent())
            return user.get();
        throw new NotFoundException("User " + username + " doesn't exist!");
    }

    public Collection<User> getAll () {
        return new ArrayList<>(users);
    }
}
