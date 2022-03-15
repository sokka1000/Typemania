package com.example.lab6.socialnetwork.service;

import com.example.lab6.socialnetwork.domain.Conversation;
import com.example.lab6.socialnetwork.domain.Event;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.repository.database.PagingEventDbRepository;
import com.example.lab6.socialnetwork.repository.database.PagingMessageDbRepository;
import com.example.lab6.socialnetwork.repository.paging.Page;
import com.example.lab6.socialnetwork.repository.paging.Pageable;
import com.example.lab6.socialnetwork.repository.paging.PageableImplementation;
import com.example.lab6.socialnetwork.repository.paging.PagingRepositoryEvent;
import com.example.lab6.utils.events.ChangeEventType;
import com.example.lab6.utils.events.MessageTaskChangeEvent;
import com.example.lab6.utils.events.TaskStatusEvent;
import com.example.lab6.utils.observer.Observable;
import com.example.lab6.utils.observer.Observer;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class EventTaskService implements Observable<MessageTaskChangeEvent> {

    private PagingEventDbRepository repo;

    public EventTaskService(PagingEventDbRepository repo) {
        this.repo = repo;

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

    public List<Event> getEventsOnPage(int page) {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<Event> eventPage = repo.findAll(pageable);
        return eventPage.getContent().collect(Collectors.toList());
    }



    public List<Event> getEventsOnPage(int page, Iterable<Event> events) {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<Event> eventPage = repo.findAllFilter(pageable,events);
        return eventPage.getContent().collect(Collectors.toList());
    }

    public List<Event> getAll()
    {

         return repo.findAll();

    }

    public List<Event> getAllEventsForGivenUsername(String username) throws ParseException {
        return repo.findAll(username);


    }

    public boolean isEventComingSoon(Event event) throws ParseException {
        return repo.isEventComingSoon(event);
    }



    public void  addParticipantAtEvent(Event event,String username)
    {
        repo.addParticipantAtEvent(event,username);
        notifyObservers(new MessageTaskChangeEvent(ChangeEventType.SUBSCRIBE));
    }
    public void  removeParticipantAtEvent(Event event,String username)
    {
        repo.removeParticipantAtEvent(event,username);
        notifyObservers(new MessageTaskChangeEvent(ChangeEventType.UNSUBSCRIBE));
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
