package com.example.lab6.socialnetwork.repository.file;

import com.example.lab6.socialnetwork.domain.Entity;
import com.example.lab6.socialnetwork.domain.Tuple;
import com.example.lab6.socialnetwork.domain.validators.Validator;
import com.example.lab6.socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;


///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui solutia propusa cu un Factori (vezi mai jos)
public abstract class AbstractFileRepository<ID, E extends Entity<Tuple<ID,ID>>> extends InMemoryRepository<ID,E> {
    String fileName;
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
        loadData();


    }
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie = br.readLine()) != null){
                //System.out.println(linie);
                List <String> attributes= Arrays.asList(linie.split(";"));
                E entity=extractEntity(attributes);
                super.save(entity);

            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }



    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);


    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity){
        E e=super.save(entity);
        if(e==null)
            writeToFile();
        return e;

    }

    @Override
    public E delete(Tuple<ID,ID> id)
    {
        E e=super.delete(id);
        if(e!=null)
            writeToFile();
        return e;
    }


    protected void writeToFile(){

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

            Iterable<E> friendships = super.findAll();
            for (E friendship : friendships)
            {bw.write(createEntityAsString(friendship));
                bw.newLine();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    }



