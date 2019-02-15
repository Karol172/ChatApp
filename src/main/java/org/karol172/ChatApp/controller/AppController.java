package org.karol172.ChatApp.controller;

import org.karol172.ChatApp.component.TokenGenerator;
import org.karol172.ChatApp.message.*;
import org.karol172.ChatApp.service.RoomService;
import org.karol172.ChatApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;

@Controller
public class AppController {

    private UserService userService;

    private RoomService roomService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public AppController(UserService userService, RoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    @GetMapping("/login/{username}")
    @ResponseBody
    public Reply createUser (@PathVariable("username") String username, @Autowired TokenGenerator tokenGenerator) {
        String token = tokenGenerator.generateToken();
            if (userService.createUser(username, token))
                return new Reply<>(MessageType.CREATE_USER, token);
        return new Reply<>(MessageType.NICK_IS_TAKEN, username);
    }

    @DeleteMapping("/login/{username}/{token}")
    public void deleteUser (@PathVariable("username") String username, @PathVariable("token") String token) {
        if (userService.authenticateUser(username, token))
            userService.removeUser(username);
    }

    @GetMapping("/rooms")
    @ResponseBody
    public Reply getRooms () {
        return new Reply<>(MessageType.GET_ROOMS, roomService.getRooms());
    }

    @GetMapping("/messages/{roomName}")
    @ResponseBody
    public Reply getMessages (@PathVariable("roomName") String roomName) {
        roomService.setActiveForRoom(roomName, true);
        return new Reply<>(MessageType.GET_MESSAGES, roomService.getMessagesFromRoom(roomName));
    }

    @MessageMapping("/room")
    @SendTo("/topic/room")
    public Reply<String> rooms (@NotNull Request<ObjectsName> request){
        switch (request.getMessageType()) {
            case CREATE_ROOM:
                if (roomService.createRoom(request.getContent().getRoomName()))
                    return new Reply<>(MessageType.CREATE_ROOM, request.getContent().getRoomName());
                else
                    return new Reply<>(MessageType.ROOM_NAME_IS_TAKEN, request.getContent().getRoomName());
            case CHECK_CONNECTION:
                if (request.getContent().getUsername() != null)
                    userService.setActiveForUser(request.getContent().getUsername(), true);
                if (request.getContent().getRoomName() != null)
                    roomService.setActiveForRoom(request.getContent().getRoomName(), true);
                break;
        }

        return new Reply<>(MessageType.BAD_REQUEST, null);
    }

    @MessageMapping("/conversation/{roomName}")
    @SendTo("/topic/conversation/{roomName}")
    public Reply conversation (@DestinationVariable String roomName, @NotNull Request<Message> request) {
        if (request.getMessageType() == MessageType.MESSAGE
                && userService.authenticateUser(request.getContent().getAuthor(), request.getContent().getToken())) {
            Message message = new Message(request.getContent().getAuthor(), request.getContent().getContent());
            roomService.addMessage(roomName, message);
            userService.setActiveForUser(request.getContent().getAuthor(), true);
            return new Reply<>(MessageType.MESSAGE, message);
        }
        return new Reply<>(MessageType.BAD_REQUEST, null);
    }

    @Scheduled(fixedRate = 15000)
    public void checkConnectionRequest() {
        userService.removeInactiveUser();
        userService.setAllUsersAsInactive();

        roomService.removeInactiveRooms().forEach(room -> simpMessagingTemplate.convertAndSend("/topic/room",
                new Request<>(MessageType.DELETE_ROOM, room.getNameRoom())));
        roomService.setAllRoomsAsInactive();

        simpMessagingTemplate.convertAndSend("/topic/room", new Request<>(MessageType.CHECK_CONNECTION, null));

    }
}