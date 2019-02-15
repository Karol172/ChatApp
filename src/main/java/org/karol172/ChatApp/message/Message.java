package org.karol172.ChatApp.message;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable, Comparable<Message> {

    @NotNull
    private String author;

    private String token;

    @NotNull
    private String content;

    private LocalDateTime date;

    public Message() {
        this.date = LocalDateTime.now();
    }

    public Message(String author, String token, String content) {
        this();
        this.author = author;
        this.token = token;
        this.content = content;
    }

    public Message(String author, String content) {
        this();
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public int compareTo(Message o) {
        if (o.date.isBefore(this.date))
            return 1;
        else if (o.date.isAfter(this.date))
            return -1;
        else
            return 0;
    }
}
