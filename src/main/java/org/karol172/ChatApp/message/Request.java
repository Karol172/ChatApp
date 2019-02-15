package org.karol172.ChatApp.message;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Request <T> implements Serializable {

    @NotNull
    private MessageType messageType;

    @NotNull
    private T content;

    public Request(MessageType messageType, T content) {
        this.messageType = messageType;
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
