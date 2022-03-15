package com.example.lab6.socialnetwork.domain.validators;

import com.example.lab6.socialnetwork.domain.User;

import java.util.Map;

public class UserValidator  implements Validator<User> {


    public void validate(User entity) throws ValidationException {

        String errMsg="";
        if(entity.getId() == null || "".equals(entity.getId()))
            errMsg+="ID error!\n";
        if(entity.getFirstName()== null || "".equals(entity.getFirstName()))
            errMsg+="First name error!\n";
        if(entity.getLastName()==null || "".equals(entity.getLastName()))
            errMsg+="Last name error!\n";
        if(errMsg!="")
            throw new ValidationException(errMsg);
    }

    @Override
    public void validate2(User entity,Map<Long, User> users) throws ValidationException {

    }
}
