package com.example.lab6.socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Conversation {
    private int id;
    private List<String> usernames;
    private MessageDTO lastMessage;


    public Conversation(List<String> usernames) {
        this.usernames = usernames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public MessageDTO getMessageDTO() {
        return lastMessage;
    }

    public void setMessageDTO(MessageDTO messageDTO) {
        this.lastMessage = messageDTO;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", usernames=" + usernames +
                ", lastMessage=" + lastMessage +
                '}';
    }

    public List<String> getUsernamesWithoutUsername(String username)
    {
        List<String> usernamesWithoutUsername= new ArrayList<>();
        for(String user:usernames)
        {
            if(!Objects.equals(user,username))
                    usernamesWithoutUsername.add(user);
        }
        return usernamesWithoutUsername;
    }



}
