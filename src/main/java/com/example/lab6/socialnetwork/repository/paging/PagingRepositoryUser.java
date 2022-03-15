package com.example.lab6.socialnetwork.repository.paging;

import com.example.lab6.socialnetwork.domain.Entity;
import com.example.lab6.socialnetwork.domain.Tuple;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.repository.RepositoryFriendship;
import com.example.lab6.socialnetwork.repository.RepositoryUser;

import java.util.function.Predicate;


public interface PagingRepositoryUser<ID, E extends Entity<ID>> extends RepositoryUser<ID, E> {

    Page<E> findAll(Pageable pageable);   // Pageable e un fel de paginator
    Page<User> findAllFilter(Pageable pageable,Iterable<User> cond);

    String doHashing(String password);

}
