package org.karol172.ChatApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.karol172.ChatApp.exception.NotFoundException;
import org.karol172.ChatApp.message.Message;
import org.karol172.ChatApp.model.Room;
import org.karol172.ChatApp.repository.RoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RoomDaoTest {

    @Autowired
    private RoomDao roomDao;

    @Before
    public void setUp () {
        roomDao.getAll().forEach(room -> roomDao.removeRoom(room.getNameRoom()));
    }

    @Test
    public void roomDaoShouldAddRoom () {
        Room room = Room.valueOf("ExampleTestRoom");
        roomDao.addRoom(room.getNameRoom());
        assertTrue(roomDao.roomIsExist(room.getNameRoom()));
    }

    @Test
    public void roomDaoShouldRemoveRoom () {
        Room room = Room.valueOf("ExampleTestRoom");
        roomDao.addRoom(room.getNameRoom());
        assertTrue(roomDao.roomIsExist(room.getNameRoom()));
        roomDao.removeRoom(room.getNameRoom());
        assertFalse(roomDao.roomIsExist(room.getNameRoom()));
    }

    @Test
    public void roomDaoShouldSetActivationState () throws NotFoundException {
        Room room = Room.valueOf("ExampleTestRoom");
        roomDao.addRoom(room.getNameRoom());
        roomDao.setActiveForRoom(room.getNameRoom(), true);
        assertTrue(roomDao.roomIsActive(room.getNameRoom()));
        roomDao.setActiveForRoom(room.getNameRoom(), false);
        assertFalse(roomDao.roomIsActive(room.getNameRoom()));
    }

    @Test
    public void roomDaoShouldSetActivationStateForAllRooms () throws NotFoundException {
        Room room = Room.valueOf("ExampleTestRoom");
        Room room2 = Room.valueOf("ExampleTestRoom2");
        roomDao.addRoom(room.getNameRoom());
        roomDao.addRoom(room2.getNameRoom());
        roomDao.setActiveForAllRoom(true);
        assertTrue(roomDao.roomIsActive(room.getNameRoom()));
        assertTrue(roomDao.roomIsActive(room2.getNameRoom()));
        roomDao.setActiveForAllRoom(false);
        assertFalse(roomDao.roomIsActive(room.getNameRoom()));
        assertFalse(roomDao.roomIsActive(room2.getNameRoom()));
    }

    @Test
    public void roomDaoShouldAddMessage () throws NotFoundException {
        Room room = Room.valueOf("ExampleTestRoom");
        Message message = new Message("Unknown author", "Example content message");
        roomDao.addRoom(room.getNameRoom());
        roomDao.addMessage(room.getNameRoom(), message);
        assertEquals(1,roomDao.getMesages(room.getNameRoom()).size());
        assertTrue(roomDao.getMesages(room.getNameRoom()).contains(message));
    }

    @Test
    public void roomDaoShouldThrowNotFoundException () {
        try {
            roomDao.roomIsActive("Example Room");
        } catch (NotFoundException e) {
            assertEquals(NotFoundException.class, e.getClass());
        }
    }




}
