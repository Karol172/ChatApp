var stompConversationClient = null;
var roomName = null;

function setConnected(connected) {
    $("#send").prop("disabled", !connected);
    $("#message").prop("disabled", !connected)
    if (!connected)
        eraseMessageField();
}

function eraseMessageField () {
    $("#messages").empty();
}

function pickRoom (name) {
    roomName = name;
    connectWithRoom();
    if (stompConversationClient != null)
        setConnected(true);
    $("#currentRoomName").text(roomName);
    eraseMessageField();
    getMessages(name);
}

function getMessages(name) {
    $.get( "/messages/"+name, function( data ) {
        if (data.messageType == "GET_MESSAGES") {
            for (var i = 0; i < data.content.length; i++)
                showMessage(data.content[i]);
        }
    }, "json" );
    $("#messages").animate({ scrollTop: $('#messages').prop("scrollHeight")}, 1000);
}

function connectWithRoom() {
    disconnectWithRoom();
    var socket = new SockJS('/chatApp');
    stompConversationClient = Stomp.over(socket);
    stompConversationClient.connect({}, function (frame) {
        stompConversationClient.subscribe('/topic/conversation/'+roomName, function (reply) {
            var object = JSON.parse(reply.body);
            if (object.messageType == 'MESSAGE')
                showMessage(object.content);
        });
    });
}

function disconnectWithRoom () {
    if (stompConversationClient != null)
        stompConversationClient.disconnect();
}

function showMessage(message) {
    $("#messages").append("<div><small>"+message.author+" " + formatDate(new Date(message.date))+"</small><br />"+message.content+"<hr /></div>")
}

function formatDate (date) {
    return date.getFullYear() + '-' + date.getMonth() + '-' + date.getDay() + ' ' + date.getHours() + ':' + date.getMinutes() +
        ":" + date.getSeconds();
}

$(function() {
    setConnected(false);
    $("#send").click(function () {
        if ($("#message").val() != "") {
            sendMessage("/app/conversation/" + roomName, 'MESSAGE',
                {'author': username, 'token': token, 'content': $("#message").val()});
            $("#message").val("");
            $("#messages").animate({ scrollTop: $('#messages').prop("scrollHeight")}, 1000);
        }
    });
});