package org.karol172.ChatApp.model;

import org.karol172.ChatApp.message.Message;

import java.util.*;

public class Room implements Comparable<Room>{

    private String nameRoom;

    private boolean active;

    private List<Message> messages;

    public Room(String nameRoom) {
        this.nameRoom = nameRoom;
        this.active = true;
        this.messages = new ArrayList<>();
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Message> getMessages() {
        Collections.sort(messages);
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public static Room valueOf (String roomName) {
        return new Room(roomName);
    }

    @Override
    public int compareTo(Room o) {
        if (o != null)
            return this.nameRoom.compareTo(o.getNameRoom());
        return 1;
    }
}