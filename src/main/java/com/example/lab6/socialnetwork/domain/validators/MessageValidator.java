package com.example.lab6.socialnetwork.domain.validators;

import com.example.lab6.socialnetwork.domain.Message;
import com.example.lab6.socialnetwork.domain.User;

import java.util.Map;

public class MessageValidator implements  Validator<Message>{
    @Override
    public void validate(Message entity) throws ValidationException {

    }

    @Override
    public void validate2(Message entity, Map<Long, User> users) throws ValidationException {

    }
}
