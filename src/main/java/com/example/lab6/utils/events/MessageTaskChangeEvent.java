package com.example.lab6.utils.events;


import com.example.lab6.socialnetwork.domain.Message;

public class MessageTaskChangeEvent implements Event {
    private ChangeEventType type;
    private Message data, oldData;

    public MessageTaskChangeEvent(ChangeEventType type) {
        this.type = type;
    }
    public MessageTaskChangeEvent(ChangeEventType type, Message data) {
        this.type = type;
        this.data = data;
    }
    public MessageTaskChangeEvent(ChangeEventType type, Message data, Message oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Message getData() {
        return data;
    }

    public Message getOldData() {
        return oldData;
    }
}