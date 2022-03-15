package com.example.lab6.socialnetwork.service;

import com.example.lab6.socialnetwork.domain.Friendship;
import com.example.lab6.socialnetwork.domain.Message;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.repository.RepositoryUser;
import com.example.lab6.socialnetwork.repository.paging.Page;
import com.example.lab6.socialnetwork.repository.paging.Pageable;
import com.example.lab6.socialnetwork.repository.paging.PageableImplementation;
import com.example.lab6.socialnetwork.repository.paging.PagingRepositoryUser;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserTaskService {
    private PagingRepositoryUser<Long, User> repoUsers;


    public UserTaskService(PagingRepositoryUser<Long, User> repoUsers) {
        this.repoUsers = repoUsers;
    }


    public <T> Iterable <T> filter(Iterable <T> list, Predicate<T> cond)
    {
        List<T> rez=new ArrayList<>();
        list.forEach((T x)->{if (cond.test(x)) rez.add(x);});
        return rez;
    }

    public Iterable<User> getAll(){
        return repoUsers.findAll();
    }

    private int page = 0;
    private int size = 5;

    private Pageable pageable;



    public Set<User> getUsersOnPage(int page) {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<User> usersPage = repoUsers.findAll(pageable);
        return usersPage.getContent().collect(Collectors.toSet());
    }
    public Set<User> getUsersOnPage(int page,Iterable<User> users) {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<User> usersPage = repoUsers.findAllFilter(pageable,users);
        return usersPage.getContent().collect(Collectors.toSet());
    }



    public int size()
    {
        return repoUsers.size();
    }

    public void setPageSize(int size) {
        this.size = size;
    }




    public User existsUsernamePassword(String username,String password)
    {
        return repoUsers.existsUsernamePassword(username,password);

    }
    public User existsUsername(String username)
    {
        return repoUsers.existsUsername(username);

    }

    public String doHashing(String password) {
        return repoUsers.doHashing(password);
    }

    public User addUser(User messageTask) {
        User task = repoUsers.save(messageTask);
        return task;
    }
    public User updateUser(User messageTask) {
        User task = repoUsers.update(messageTask);
        return task;
    }
    public User findUser(Long id)
    {
        User task=repoUsers.findOne(id);
        return task;
    }

    public User deleteUser(Long id)
    {
        User task=repoUsers.delete(id);
        return task;
    }

    public List<User> getUserFriends(User userSearched, Iterable<Friendship> friendships) {
        List<User> friendsForSearchedUser=new ArrayList<>();
        Iterable<User> users = this.getAll();
        Map<Long,User> userMap=new HashMap<>();

        for(User user : users)
        {
            userMap.put(user.getId(),user);
        }


        for(Friendship friendship : friendships) {
            if(Objects.equals(friendship.getID1(),userSearched.getId())) {
                friendsForSearchedUser.add(userMap.get(friendship.getID2()));
            }
            else
                friendsForSearchedUser.add(userMap.get(friendship.getID1()));
        }
        return friendsForSearchedUser;
    }

    public List<User> getUsersFromUsernames(Iterable <String> usernames)
    {
        List<User> usersFromUsernames=new ArrayList<>();
        Iterable<User> users = this.getAll();
        Map<String,User> userMap=new HashMap<>();

        for(User user : users)
        {
            userMap.put(user.getUsername(),user);
        }


        for(String username : usernames)
        {
            if(userMap.containsKey(username))
                usersFromUsernames.add(userMap.get(username));
        }

        return usersFromUsernames;
    }


    ///TO DO: add other methods
}
