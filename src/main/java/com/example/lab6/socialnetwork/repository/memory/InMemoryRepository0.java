package com.example.lab6.socialnetwork.repository.memory;

import com.example.lab6.socialnetwork.domain.Entity;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.domain.validators.Validator;
import com.example.lab6.socialnetwork.repository.RepositoryUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class InMemoryRepository0<ID, E extends Entity<ID>> implements RepositoryUser<ID,E> {

    private Validator<E> validator;
    Map<ID,E> entities;

    public Long generateID()
    {
        int id = 0;
        boolean isValid=false;
        while(isValid==false)
        {
            isValid=true;
            id= Math.abs(new Random().nextInt());

            for (Map.Entry<ID, E> entry : entities.entrySet())
            {
                    if(entry.getKey()==Long.valueOf(id))
                        isValid=false;
            }

        }

        return Long.valueOf(id);

    }

    public InMemoryRepository0(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public E findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");

        if(entity.getId()==null)
        entity.setId((ID) generateID());
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            return entity;
        }
        else entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        else if(entities.get(id) != null)
        {
            E entity=entities.get(id);
            entities.remove(id);
            return entity;

        }
        else
            return null;
    }

    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;

    }

    @Override
    public Map<ID, E> all() {
        return entities;
    }

    @Override
    public User existsUsernamePassword(String usernameUser, String passwordUser) {
        return null;
    }

    @Override
    public User existsUsername(String usernameUser) {
        return null;
    }

    @Override
    public int size()
    {
        return entities.size();
    }

}
