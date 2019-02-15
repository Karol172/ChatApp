package org.karol172.ChatApp.model;


public class User implements Comparable<User> {

    private String name;

    private String token;

    private boolean active;

    public User(String name) {
        this.name = name;
        this.active = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static User valueOf (String username) {
        return new User(username);
    }

    @Override
    public int compareTo(User o) {
        if (o != null)
            return this.name.compareTo(o.getName());
        return 1;
    }
}