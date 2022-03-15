package com.example.lab6.socialnetwork.repository.database;

import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.domain.validators.Validator;
import com.example.lab6.socialnetwork.repository.RepositoryUser;

import java.sql.*;
import java.util.*;

public class UserDbRepository implements RepositoryUser<Long, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;
    Map<Long,User> entities;

    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.entities= new HashMap<>();
        loadData();
    }

    @Override
    public User existsUsernamePassword(String usernameUser,String passwordUser)
    {
        Iterable<User> users = findAll();
        for(User user:users) {

            if (user.getUsername().equals(usernameUser) && user.getPassword().equals(passwordUser))
            {
                return user;

            }

        }

        return null;
    }
    @Override
    public User existsUsername(String usernameUser)
    {
        Iterable<User> users = findAll();
        for(User user:users) {

            if (user.getUsername().equals(usernameUser))
            {
                return user;

            }
        }

        return null;
    }


    public boolean existsEntity(User entity){
        Iterable<User> users = findAll();
        for(User user:users) {

            if (entity.getFirstName().equals(user.getFirstName()) && entity.getLastName().equals(user.getLastName()))
            {
                return true;

            }
        }
        return false;
    }
    private boolean existsID(Long aLong)
    {
        Iterable<User> users = findAll();
        for(User user:users) {

            if (aLong == user.getId())
            {
                return true;
            }
        }
        return false;

    }
/*
    public User findOne(Long aLong) {
        User user = null;
        String sql = "SELECT * from users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();

            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            user = new User(firstName, lastName);
            user.setId(id);
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    */



                @Override

    public User findOne(Long aLong) {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users where id=" + String.valueOf(aLong));
             ResultSet resultSet = statement.executeQuery()) {


            while (resultSet.next()) {

                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(firstName, lastName);
                user.setUsername(username);
                user.setPassword(password);
                user.setId(id);
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;


    }



    public void loadData()
    {
        if(entities!=null)
        entities.clear();

        Iterable<User> users=findAll();
        for(User user:users)
        {
            entities.put(user.getId(),user);
        }


    }
    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(firstName, lastName);
                user.setUsername(username);
                user.setPassword(password);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public Long generateID()
    {
        int id = 0;
        boolean isValid=false;
        while(isValid==false)
        {
            isValid=true;
            id= Math.abs(new Random().nextInt());

            for (Map.Entry<Long, User> entry : entities.entrySet())
            {
                if(entry.getKey()==Long.valueOf(id))
                    isValid=false;
            }

        }

        return Long.valueOf(id);

    }



    @Override
    public User save(User entity) {

        String sql = "insert into users (first_name, last_name, username, password ) values (?, ?, ?, ?)";

        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        if(entity.getId()==null)
            entity.setId(generateID());

        validator.validate(entity);



        if(existsEntity(entity)==false) {

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, entity.getFirstName());
                ps.setString(2, entity.getLastName());
                ps.setString(3, entity.getUsername());
                ps.setString(4, entity.getPassword());


                ps.executeUpdate();
                loadData();

                return null;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    @Override
    public User delete(Long aLong) {
        User entity=findOne(aLong);
        if(entity!=null)  // entity exists
        {

            String sql = "delete from users where id = ?";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {


                ps.setInt(1,aLong.intValue());
                ps.executeUpdate();
                loadData();
                return entity;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public User update(User entity) {

        String sql = "update users set first_name = ?, last_name=? where id = ?";


        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);

        if(existsEntity(entity)==true)
            throw new IllegalArgumentException("Existent new user");


        if(existsID(entity.getId())==true) {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, entity.getFirstName());
                ps.setString(2, entity.getLastName());
                ps.setInt(3, entity.getId().intValue());


                ps.executeUpdate();
                loadData();
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    @Override
    public Map<Long, User> all() {
        return entities;
    }

    @Override
    public int size() {
        int contor=0;
        Iterable<User> users = findAll();
        for(User user:users)
            contor++;
        return contor;
    }
}
