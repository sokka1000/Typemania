package com.example.lab6.socialnetwork.domain.validators;

import com.example.lab6.socialnetwork.domain.User;

import java.util.Map;

public class NumberValidator implements Validator<String> {
    @Override
    public void validate(String entity) throws ValidationException {
        String msg = "";
        for (int i = 0; i < entity.length(); i++)
        {if (entity.charAt(i) < '0' || entity.charAt(i) > '9')
                msg += "value is not a number";
        break;
        }

        if(msg!="")
            throw new  ValidationException(msg);

    }

    @Override
    public void validate2(String entity,Map<Long, User> users) throws ValidationException {

    }

}
