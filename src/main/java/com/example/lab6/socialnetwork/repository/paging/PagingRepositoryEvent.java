package com.example.lab6.socialnetwork.repository.paging;

import com.example.lab6.socialnetwork.domain.Conversation;
import com.example.lab6.socialnetwork.repository.RepositoryEvent;
import com.example.lab6.socialnetwork.repository.RepositoryMessages;

public interface PagingRepositoryEvent <ID , E > extends RepositoryEvent<ID, E> {
    Page<E> findAll(Pageable pageable);   // Pageable e un fel de paginator
    Page<E> findAllFilter(Pageable pageable, Iterable<E> cond);
}
