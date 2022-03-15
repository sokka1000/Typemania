package com.example.lab6.utils;


import com.example.lab6.Data;

import com.example.lab6.socialnetwork.domain.Friendship;
import com.example.lab6.socialnetwork.domain.Tuple;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.service.FriendshipTaskService;
import com.example.lab6.socialnetwork.service.UserTaskService;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.RecursiveTask;
import java.util.function.Predicate;

public class CustomRecursiveAction extends RecursiveTask<List<User>>
{

    private UserTaskService userService;
    private  Label labelNameUser;
    private  FriendshipTaskService friendshipService;
    private List<User> friendsForSearchedUser=new ArrayList<>();
    private   List<User> friendsCommonForSearchedUser=new ArrayList<>();

    public List<User> getFriendsForSearchedUser() {
        return friendsForSearchedUser;
    }

    public List<User> getFriendsCommonForSearchedUser() {
        return friendsCommonForSearchedUser;
    }

    public CustomRecursiveAction(UserTaskService userService1, Label labelNameUser1, FriendshipTaskService friendshipTaskService1) {
        userService=userService1;
        labelNameUser=labelNameUser1;
        friendshipService=friendshipTaskService1;

    }

    @Override
        protected List<User> compute(){

        User userSearched=userService.existsUsername(Data.username);
        User userConnected= Data.connectedUser;

        //calculate number of friends and put this in label
        Predicate<Friendship> byID1= x->x.getID1().equals(userSearched.getId());
        Predicate<Friendship> byID2= x->x.getID2().equals(userSearched.getId());

        Predicate<Friendship> filteredFriendship = byID1.or(byID2);


        Iterable<Friendship>  friendshipsForSearchedUser= friendshipService.filter(friendshipService.getAll(),filteredFriendship);


        for(Friendship friendship : friendshipsForSearchedUser) {

            if(friendship.getID1()==userSearched.getId())
                friendsForSearchedUser.add(userService.findUser(friendship.getID2()));
            else
                friendsForSearchedUser.add(userService.findUser(friendship.getID1()));

        }

        //sorted by names
        Collections.sort(friendsForSearchedUser, new SortByNames());



        //calculate number of common friends and put this in label


        for(User user : friendsForSearchedUser) {
            if(!Objects.equals(friendshipService.findOne(new Tuple<>(userConnected.getId(),user.getId())),null)) {
                friendsCommonForSearchedUser.add(user);

            }
        }

        return friendsForSearchedUser;

    }
}