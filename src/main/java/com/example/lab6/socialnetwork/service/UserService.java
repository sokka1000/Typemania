package com.example.lab6.socialnetwork.service;

import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.repository.RepositoryUser;

import java.util.Map;

public class UserService {
    private RepositoryUser<Long, User> repoUsers;


    public UserService(RepositoryUser<Long, User> repoUsers) {
        this.repoUsers = repoUsers;
    }


    public User existsUsernamePassword(String username,String password)
    {
        return repoUsers.existsUsernamePassword(username,password);

    }
    public User existsUsername(String username)
    {
        return repoUsers.existsUsername(username);

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

    public Iterable<User> getAll(){
        return repoUsers.findAll();
    }
    public Map<Long, User> all()
    {
        return repoUsers.all();
    }



    ///TO DO: add other methods
}
