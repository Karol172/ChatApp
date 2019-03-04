package org.karol172.ChatApp.repository;

import org.karol172.ChatApp.exception.NotFoundException;
import org.karol172.ChatApp.message.Message;
import org.karol172.ChatApp.model.Room;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RoomDao {

    private Collection<Room> rooms;

    public RoomDao() {
        this.rooms = new TreeSet<>();
    }

    public void addRoom (String roomName) {
        rooms.add(Room.valueOf(roomName));
    }

    public void removeRoom (String roomName) {
        rooms.remove(Room.valueOf(roomName));
    }

    public boolean roomIsExist (String roomName) {
        return rooms.stream().anyMatch(room -> room.getNameRoom().equals(roomName));
    }

    public boolean roomIsActive (String roomName) throws NotFoundException {
        return findRoom(roomName).isActive();
    }

    public void setActiveForRoom (String roomName, boolean active) throws NotFoundException {
        findRoom(roomName).setActive(active);
    }

    public void setActiveForAllRoom (boolean active) {
        rooms.forEach(room -> room.setActive(active));
    }

    public void addMessage (String roomName, Message message) throws NotFoundException {
        findRoom(roomName).getMessages().add(message);
    }

    public Collection<Message> getMesages (String roomName) throws NotFoundException {
        return findRoom(roomName).getMessages();
    }

    public List<Room> getAll () {
        return new ArrayList<>(rooms);
    }

    private Room findRoom (String roomName) throws NotFoundException {
        Optional<Room> room = rooms.stream().filter(r -> r.getNameRoom().equals(roomName)).findFirst();
        if (room.isPresent())
            return room.get();

        throw new NotFoundException("Room " + roomName + " doesn't exist!");
    }
}