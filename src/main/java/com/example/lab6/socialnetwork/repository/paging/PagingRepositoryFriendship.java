package com.example.lab6.socialnetwork.repository.paging;

import com.example.lab6.socialnetwork.domain.Entity;
import com.example.lab6.socialnetwork.domain.Friendship;
import com.example.lab6.socialnetwork.domain.Tuple;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.repository.RepositoryFriendship;

import java.util.Map;
import java.util.function.Predicate;


public interface PagingRepositoryFriendship<ID, E extends Entity<Tuple<ID,ID>>> extends RepositoryFriendship<ID, E> {

    Page<E> findAll(Pageable pageable);   // Pageable e un fel de paginator
    Page<Friendship> findAllFilter(Pageable pageable,Iterable<Friendship> friendships);
    public Map<String, String> allFriends(String username);

}
