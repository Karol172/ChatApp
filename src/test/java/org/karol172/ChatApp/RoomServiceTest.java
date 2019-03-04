package org.karol172.ChatApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.karol172.ChatApp.message.Message;
import org.karol172.ChatApp.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @Before
    public void setUp () {
        roomService.setAllRoomsAsInactive();
        roomService.removeInactiveRooms();
    }


    @Test
    public void shouldCreateRoom() {
        roomService.createRoom("exampleRoom");
        assertTrue(roomService.getRooms().stream().anyMatch(room -> room.getNameRoom().equals("exampleRoom")));
    }

    @Test
    public void shouldSetRoomActivationState () {
        roomService.createRoom("exampleRoom");
        roomService.setActiveForRoom("exampleRoom", false);
        assertTrue(roomService.getRooms().stream().anyMatch(room -> room.getNameRoom().equals("exampleRoom") && !room.isActive()));
        roomService.setActiveForRoom("exampleRoom", true);
        assertTrue(roomService.getRooms().stream().anyMatch(room -> room.getNameRoom().equals("exampleRoom") && room.isActive()));
    }

    public void shouldRemoveAllInactiveRooms () {
        roomService.createRoom("exampleRoom");
        roomService.createRoom("exampleRoom2");
        roomService.setActiveForRoom("exampleRoom", false);
        assertEquals(1, roomService.removeInactiveRooms().size());
        roomService.createRoom("exampleRoom");
        roomService.setAllRoomsAsInactive();
        assertEquals(2, roomService.removeInactiveRooms());
    }

    public void shouldCreateMessagesForRoom () {
        roomService.createRoom("exampleRoom");
        roomService.addMessage("exampleRoom", new Message("Unknown Author", "ExampleMessage"));
        roomService.addMessage("exampleRoom", new Message("Another Unknown Author", "ExampleMessage"));
        assertEquals(2, roomService.getMessagesFromRoom("exampleRoom").size());
    }

}
