package com.example.lab6.socialnetwork.service;

import com.example.lab6.socialnetwork.domain.Message;
import com.example.lab6.socialnetwork.domain.MessageDTO;
import com.example.lab6.socialnetwork.domain.RequestDTO;
import com.example.lab6.socialnetwork.repository.database.MessageDbRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class MessageService {
    private MessageDbRepository repoMessage;

    public void sendMessage(Message message)
    {
        repoMessage.addMessage(message);
    }

    public MessageService(MessageDbRepository repoMessage) {
        this.repoMessage = repoMessage;
    }

    public Message findOneFromDB(String username1,String username2, int id)
    {

        return repoMessage.findOneFromDB(username1,username2,id);
    }

    public void sendReply(Message reply)
    {
        int ok = 0;
        String to = repoMessage.findListById(reply.getReply().getId());
        if(to!=null) {
            String[] list = to.split(" ");
            for (String username : list) {
                if (Objects.equals(username, reply.getFrom().getUsername())) {
                    this.repoMessage.addReply(reply);
                    ok = 1;

                }
            }
            if (ok == 0)
                throw new IllegalArgumentException("message doesn't exist");
        }
        else
            throw new IllegalArgumentException("message doesn't exist");
    }



    private boolean comp(MessageDTO a, MessageDTO b)
    {
        return a.getDateTime().compareTo(b.getDateTime())<0;
    }
    public List<MessageDTO> task3(String username1, String username2)
    {

        List<MessageDTO> messageDTOList= repoMessage.task3(username1,username2);

        Collections.sort(messageDTOList, new Comparator<MessageDTO>() {
            @Override
            public int compare(MessageDTO p1, MessageDTO p2) {
                if(p1.getDateTime().compareTo(p2.getDateTime())<0)
                    return -1;
                else
                    return 1;
            }
        });




        return messageDTOList;

    }


    public void sendRequest(String username1, String username2,String date){
        repoMessage.sendRequest(username1, username2,date);
    }

    public List<String> showFriendshipRequests(String username){
        return repoMessage.showFriendshipRequests(username);
    }
    public List<RequestDTO>  requestDTOList(String username)
    {
        return repoMessage.requestDTOList(username);
    }


    public void acceptRequest(String username, String username2) {
        repoMessage.acceptRequest(username, username2);
    }

    public void rejectRequest(String username, String username2) {
        repoMessage.rejectRequest(username, username2);
    }

    public void deleteRequest(String username, String username2) {
        repoMessage.deleteRequest(username, username2);
    }

    public void cancelRequest(String username, String username2) {
        repoMessage.cancelRequest(username, username2);
    }

}
