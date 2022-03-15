package com.example.lab6.socialnetwork.domain.validators;


import com.example.lab6.socialnetwork.domain.Message;
import com.example.lab6.socialnetwork.domain.User;

import java.util.Map;

public class MessageTaskValidator implements Validator<Message> {

    @Override
    public void validate(Message entity){
        String err="";
        //validate entity
        if (!err.equals(""))
            throw new ValidationException(err);

    }

    @Override
    public void validate2(Message entity,Map<Long, User> users) throws ValidationException {

    }

}
