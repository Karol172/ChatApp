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
            console.log(token);
            console.log(data);
            $("#incorrect-name").visibility = 'visible';
            setLogged(true);
            getRooms();
            connect();
        }
        else
            $("#incorrect-name").visibility = 'visible';
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
    setLogged(false);
    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $("#login").click(function() { logIn(); });
});

$(window).bind('beforeunload',function(){

    //TODO: to implement

    return 'are you sure you want to leave?';

});
