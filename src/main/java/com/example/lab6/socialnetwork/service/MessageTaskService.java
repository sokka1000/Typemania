package com.example.lab6.socialnetwork.service;


import com.example.lab6.socialnetwork.domain.*;
import com.example.lab6.socialnetwork.repository.database.MessageDbRepository;
import com.example.lab6.socialnetwork.repository.database.PagingMessageDbRepository;
import com.example.lab6.socialnetwork.repository.paging.Page;
import com.example.lab6.socialnetwork.repository.paging.Pageable;
import com.example.lab6.socialnetwork.repository.paging.PageableImplementation;
import com.example.lab6.socialnetwork.repository.paging.PagingRepositoryMessages;
import com.example.lab6.utils.events.ChangeEventType;
import com.example.lab6.utils.events.MessageTaskChangeEvent;
import com.example.lab6.utils.observer.Observable;
import com.example.lab6.utils.observer.Observer;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MessageTaskService implements Observable<MessageTaskChangeEvent> {


    private PagingMessageDbRepository repo;

    public MessageTaskService(PagingMessageDbRepository repo) {
        this.repo = repo;

    }


    public Message addMessageTask(Message messageTask) {

        Message  task= repo.addMessage(messageTask);
        if(task!=null) {
            notifyObservers(new MessageTaskChangeEvent(ChangeEventType.ADD, task));
        }
        return task;
    }

    public Message deleteMessageTask(Message t){
        Message task=repo.delete(t.getId());
        if(task!=null) {
            notifyObservers(new MessageTaskChangeEvent(ChangeEventType.DELETE, task));
        }
        return task;
    }
    public Message deleteMessageTask(Message t,Conversation conversation){
        Message task=repo.delete(t.getId());
        updateConversationWithLastMessage(conversation);
        if(task!=null) {
            notifyObservers(new MessageTaskChangeEvent(ChangeEventType.DELETE, task));
        }
        return task;
    }

    public Message updateMessageTask(Message newTask) {

            Message res=repo.update(newTask);
            notifyObservers(new MessageTaskChangeEvent(ChangeEventType.UPDATE, newTask));
            return res;

    }
    public void updateConversationWithLastMessage(Conversation conversation)
    {

        MessageDTO messageDTOnew=repo.getLastMessageDTOfromIdConversation(conversation.getId());
       if(Objects.equals(messageDTOnew,null))
       {
           messageDTOnew=new MessageDTO("","","");
       }

        Conversation newConversation = new Conversation(conversation.getUsernames());
        newConversation.setId(conversation.getId());
        newConversation.setMessageDTO(messageDTOnew);

        updateConversation(newConversation);

    }

    public Iterable<Message> getAll(){
        return repo.findAll();
    }

    private List<Observer<MessageTaskChangeEvent>> observers=new ArrayList<>();



    private int page = 0;
    private int size = 1;

    private Pageable pageable;

    public void setPageSize(int size) {
        this.size = size;
    }

//    public void setPageable(Pageable pageable) {
//        this.pageable = pageable;
//    }


    public List<MessageDTO> getMessagesInTimeInterval(String username, LocalDate date1, LocalDate date2){
        Iterable<MessageDTO> messages = repo.findAllMessages();
        List<MessageDTO> messageDTOList = new ArrayList<>();
        String searchedUsername = username + ",";
        for(MessageDTO messageDTO: messages){
            if(messageDTO.getUsernamesTo().contains(searchedUsername)){
                LocalDate messageDate = LocalDate.parse(messageDTO.getDateTime().substring(0, 10));
                if((messageDate.isBefore(date2) && messageDate.isAfter(date1)) || messageDate.isEqual(date1) || messageDate.isEqual(date2)) {
                    messageDTOList.add(messageDTO);
                }
            }
        }
        return messageDTOList;

    }

    public List<MessageDTO> getMessagesFromFriendInTimeInterval(String username, String friendUsername, LocalDate date1, LocalDate date2){
        Iterable<MessageDTO> messages = repo.findAllMessages();
        List<MessageDTO> messageDTOList = new ArrayList<>();
        String searchedUsername = username + ",";
        for(MessageDTO messageDTO: messages){
            if(messageDTO.getUsernamesTo().contains(searchedUsername) && Objects.equals(messageDTO.getUsername(), friendUsername)){
                LocalDate messageDate = LocalDate.parse(messageDTO.getDateTime().substring(0, 10));
                if((messageDate.isBefore(date2) && messageDate.isAfter(date1)) || messageDate.isEqual(date1) || messageDate.isEqual(date2)) {
                    messageDTOList.add(messageDTO);
                }
            }
        }
        return messageDTOList;

    }


    public List<Conversation> getConversationsOnPage(int page,Iterable<Conversation> conversations) {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<Conversation> conversationPage = repo.findAllFilter(pageable,conversations);
        return conversationPage.getContent().collect(Collectors.toList());
    }


    public void addConversation(Conversation conversation){
        repo.addConversation(conversation);
    }

    public Iterable<Conversation> findAllConversations(){
        return repo.findAllConversations();
    }

    public void updateConversation(Conversation conversation){
        repo.updateConversation(conversation);
    }

    public List<MessageDTO> getMessages(int id){
        return repo.getMessageDTOsByConversationId(id);
    }

    public List<Conversation> getConversations(String username){
        return repo.getConversationsForUser(username);
    }
    public Iterable<Conversation> getAllConversations(){
        return repo.findAllConversations();
    }





    public void sendRequest(String username1, String username2,String date){
        repo.sendRequest(username1, username2,date);

    }


    public List<String> getFriendshipRequests(String username){
        return repo.getFriendshipRequests(username);
    }

    public List<RequestDTO>  requestDTOList(String username)
    {
        return repo.requestDTOList(username);
    }

    public void acceptRequest(String username, String username2) {
        repo.acceptRequest(username, username2);
    }

    public void rejectRequest(String username, String username2) {
        repo.rejectRequest(username, username2);
    }

    public void deleteRequest(String username, String username2) {
        repo.deleteRequest(username, username2);
    }

    public void cancelRequest(String username, String username2) {
        repo.cancelRequest(username, username2);
    }

    public String findStatus(String username,String username2)
    {
       return  repo.findStatus(username, username2);
    }


    @Override
    public void addObserver(Observer<MessageTaskChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageTaskChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageTaskChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }
}
