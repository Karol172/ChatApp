package org.karol172.ChatApp.service;

import org.karol172.ChatApp.exception.NotFoundException;
import org.karol172.ChatApp.message.Message;
import org.karol172.ChatApp.model.Room;
import org.karol172.ChatApp.repository.RoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private RoomDao roomDao;

    @Autowired
    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public boolean createRoom (String roomName) {
        if (!roomDao.roomIsExist(roomName)) {
            roomDao.addRoom(roomName);
            return true;
        }
        return false;
    }

    public Collection<Room> removeInactiveRooms () {
        List<Room> roomsToRemove = roomDao.getAll().stream().filter(room -> !room.isActive()).collect(Collectors.toList());
        roomsToRemove.forEach(room -> roomDao.removeRoom(room.getNameRoom()));
        return roomsToRemove;
    }

    public void setActiveForRoom (String roomName, boolean active) {
        try {
            roomDao.setActiveForRoom(roomName, active);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAllRoomsAsInactive () {
        roomDao.setActiveForAllRoom(false);
    }

    public Collection<Room> getRooms () {
        return roomDao.getAll();
    }

    public void addMessage (String roomName, Message message) {
        try {
            roomDao.addMessage(roomName, message);
            roomDao.setActiveForRoom(roomName, true);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public Collection<Message> getMessagesFromRoom (String roomName) {
        try {
            return roomDao.getMesages(roomName);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
