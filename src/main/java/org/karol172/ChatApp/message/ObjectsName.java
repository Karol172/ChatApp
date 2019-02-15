package org.karol172.ChatApp.message;

import java.io.Serializable;

public class ObjectsName implements Serializable {

    private String username;

    private String roomName;

    public ObjectsName() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
