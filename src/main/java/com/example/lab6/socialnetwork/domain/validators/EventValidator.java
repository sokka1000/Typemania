package com.example.lab6.socialnetwork.domain.validators;

import com.example.lab6.socialnetwork.domain.Event;
import com.example.lab6.socialnetwork.domain.User;

import java.util.Map;

public class EventValidator implements  Validator<Event>{


    @Override
    public void validate(Event entity) throws ValidationException {

    }

    @Override
    public void validate2(Event entity, Map<Long, User> users) throws ValidationException {

    }
}
