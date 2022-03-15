package com.example.lab6.socialnetwork.repository.file;

import com.example.lab6.socialnetwork.domain.Friendship;
import com.example.lab6.socialnetwork.domain.Tuple;
import com.example.lab6.socialnetwork.domain.validators.Validator;

import java.util.List;

public class UtilizatorFile extends AbstractFileRepository<Long, Friendship> {



    public UtilizatorFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship friendship=new Friendship( Long.parseLong(attributes.get(0)),Long.parseLong(attributes.get(1)));
        Tuple<Long,Long> tuple = new Tuple<>(Long.parseLong(attributes.get(0)),Long.parseLong(attributes.get(1)));
        friendship.setId(tuple);
        return friendship;
    }


    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getID1()+";"+entity.getID2();

    }
}
