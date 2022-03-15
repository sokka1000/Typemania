package com.example.lab6.socialnetwork.domain.validators;

import com.example.lab6.socialnetwork.domain.Friendship;
import com.example.lab6.socialnetwork.domain.User;

import java.util.Map;

public class FriendshipValidator implements Validator<Friendship> {

    private Map<Long,User> users;

    @Override
    public void validate(Friendship entity) throws ValidationException {
 ///nothing
    }

    @Override
    public void validate2(Friendship entity,Map<Long,User> users1) throws ValidationException {

        this.users=users1;
        String msg="";
        if(users==null)
            msg+="DOESNT EXIST USERS!\n";
        else {
            if (users.get(entity.getID1()) == null)  // users1 doesnt exist
            {
                msg += "user1 doesn't exist!\n";
            }
            if (users.get(entity.getID2()) == null) // user2 deosnt exist
            {
                msg += "user2 doesn't exist!\n";
            }
            if(users.get(entity.getID1()) == users.get(entity.getID2()))
            {
                msg+= "same user!\n";
            }

        }
        if(msg!="")
            throw new ValidationException(msg);
    }



}
