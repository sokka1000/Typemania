package com.example.lab6.socialnetwork.repository.memory;

import com.example.lab6.socialnetwork.domain.Entity;
import com.example.lab6.socialnetwork.domain.Tuple;
import com.example.lab6.socialnetwork.domain.validators.Validator;
import com.example.lab6.socialnetwork.repository.RepositoryFriendship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<Tuple<ID,ID>>> implements RepositoryFriendship<ID,E> {

    private Validator<E> validator;
    Map<Tuple<ID,ID>,E> entities;


    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<Tuple<ID,ID>,E>();
    }



    @Override
    public E findOne(Tuple<ID,ID> id) {
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        if(entities.get(id) != null) {
            return entities.get(id);

        }
        else
             return null;
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();

    }
    public Map<Tuple<ID,ID>,E> all()
    {
        return entities;
    }

    @Override
    public List<E> getListFriendships()
    {
        List<E> list=new ArrayList<>();
        for (var entry : entities.entrySet()) {
           list.add(entry.getValue());
        }

        return list;
    }


    @Override
    public E save(E entity)  {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            return entity;
        }
        else entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(Tuple<ID,ID> id) {
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
    public int size()
    {
        return entities.size();
    }




}
