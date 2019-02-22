var username = null;
var token = null;

function setLogged(connected) {
    if (connected) {
        $("#login-panel").hide();
        $("#communication-panel").show();
    }
    else {
        $("#login-panel").show();
        $("#communication-panel").hide();
    }
}


function logIn () {
    username = $("#username").val();
    $.get( "/login/"+username, function( data ) {
        if (data.messageType == "CREATE_USER") {
            token = data.content;
            $("#incorrect-name").hide();
            setLogged(true);
            getRooms();
            connect();
        }
        else
            $("#incorrect-name").show();
    }, "json" );
}

function logOut () {
    $.delete("/login/"+username+"/"+token);
    if (stompRoomClient != null)
        stompRoomClient.disconnect();
    if (stompConversationClient != null)
        stompConversationClient.disconnect();
}

$(function () {
    $("#incorrect-name").hide();
    setLogged(false);
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#login").click(function() { logIn(); });
});

$(window).bind('beforeunload',function(){
    return 'are you sure you want to leave?';
});
