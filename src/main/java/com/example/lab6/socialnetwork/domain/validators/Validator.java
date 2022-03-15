package com.example.lab6.socialnetwork.domain.validators;

import com.example.lab6.socialnetwork.domain.User;

import java.util.Map;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
    void validate2(T entity,Map<Long, User> users)  throws ValidationException;
}