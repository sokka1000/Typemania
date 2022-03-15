package com.example.lab6.socialnetwork.repository.paging;


import com.example.lab6.socialnetwork.domain.Conversation;
import com.example.lab6.socialnetwork.domain.MessageDTO;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.repository.RepositoryMessages;


public interface PagingRepositoryMessages<ID , E > extends RepositoryMessages<ID, E> {

    Page<E> findAll(Pageable pageable);   // Pageable e un fel de paginator
    Page<Conversation> findAllFilter(Pageable pageable, Iterable<Conversation> cond);
    Iterable<MessageDTO> findAllMessages();

}
