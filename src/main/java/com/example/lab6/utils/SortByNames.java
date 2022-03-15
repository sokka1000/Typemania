package com.example.lab6.utils;


import com.example.lab6.socialnetwork.domain.User;

import java.util.Comparator;

public class SortByNames implements Comparator<User>
{

    public int compare(User a, User b)
    {
        if(a.getFirstName().compareTo(b.getFirstName()) <0)
            return -1;
        if((a.getFirstName().compareTo(b.getFirstName()) ==0) &&  (a.getLastName().compareTo(b.getLastName())<0))
            return -1;
        return 1;
    }
}