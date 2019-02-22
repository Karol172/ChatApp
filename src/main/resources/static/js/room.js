var stompRoomClient = null;

function createRoom () {
    var roomName = $("#roomNameInput").val();
    console.log(roomName);
    if (roomName != null && roomName != "")
        sendMessage('/app/room', 'CREATE_ROOM',{'username': ' ', 'roomName': roomName});
}

function connect() {
    var socket = new SockJS('/chatApp');
    stompRoomClient = Stomp.over(socket);
    stompRoomClient.connect({}, function (frame) {
        stompRoomClient.subscribe('/topic/room', function (reply) {
            var object = JSON.parse(reply.body);
            switch (object.messageType) {
                case 'CREATE_ROOM':
                    addRoomToList(object.content);
                    break;
                case 'DELETE_ROOM':
                    deleteRoomFromList(object.content);
                    break;
                case 'ROOM_NAME_IS_TAKEN':
                    $("#createRoomAlert").show();
                    break;
                case 'CHECK_CONNECTION':
                    confirmConnection();
                    break;
            }
        });
    });
}

function sendMessage (destination, message_type, content) {
    var sender = stompConversationClient;
    if (destination == "/app/room")
        sender = stompRoomClient;
    sender.send(destination, {}, JSON.stringify({'messageType': message_type, 'content': content}))
}

function addRoomToList(roomName) {
    console.log(roomName);
    $("#rooms").append("<div class='roomLink' onclick=pickRoom(\""+new String(roomName.replace(' ', '&#32;'))+"\")>"+roomName+"<hr /></div>")
    $("#createRoomPanel").modal('hide');
}

function deleteRoomFromList(roomName) {
    var list = $(".roomLink");
    for (var i = 0; i < list.length; i++)
        if (list[i].innerText == roomName)
            list[i].remove();
}

function confirmConnection () {
    sendMessage('/app/room', 'CHECK_CONNECTION',{'username': username, 'roomName': roomName});
}

function getRooms() {
    $.get( "/rooms", function( data ) {
        if (data.messageType == 'GET_ROOMS') {
            for (var i = 0; i < data.content.length; i++)
                addRoomToList(data.content[i].nameRoom);
        }
    }, "json" );
}

$(function() {
    $("#createRoomPanelButton").click(function () {
        $("#createRoomAlert").hide();
        $("#roomNameInput").val("");
    })
    $("#createRoom").click(function () {
        createRoom();
    })
});