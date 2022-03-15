package com.example.lab6.socialnetwork.domain;

public class MessageDTO {
    private String username;
    private String dateTime;
    private String message;
    private int reply;
    private int id;
    private String usernamesTo;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public MessageDTO(String username, String dateTime, String message) {
        this.username = username;
        this.dateTime = dateTime;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", dateTime='" + dateTime + '\'';
    }

    public String getUsernamesTo() {
        return usernamesTo;
    }

    public void setUsernamesTo(String usernamesTo) {
        this.usernamesTo = usernamesTo;
    }
}
