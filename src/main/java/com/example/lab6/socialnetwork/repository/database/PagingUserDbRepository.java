package com.example.lab6.socialnetwork.repository.database;

import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.domain.validators.Validator;
import com.example.lab6.socialnetwork.repository.paging.Page;
import com.example.lab6.socialnetwork.repository.paging.Pageable;
import com.example.lab6.socialnetwork.repository.paging.Paginator;
import com.example.lab6.socialnetwork.repository.paging.PagingRepositoryUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class PagingUserDbRepository implements PagingRepositoryUser<Long, User> {

    private String url;
    private String username;
    private String password;
    private Validator<User> validator;
    Map<Long,User> entities;
    Iterable<User> userIterable;

    public PagingUserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.entities= new HashMap<>();

    }
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
                String imageUrl = resultSet.getString("image_url");

                User user = new User(firstName, lastName);
                user.setUsername(username);
                user.setPassword(password);
                user.setImageUrl(imageUrl);
                user.setId(id);
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
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
                String imageUrl = resultSet.getString("image_url");

                User user = new User(firstName, lastName);
                user.setUsername(username);
                user.setPassword(password);
                user.setImageUrl(imageUrl);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private boolean existsEntity(User entity){
        Iterable<User> users = findAll();
        for(User user:users) {

            if (entity.getFirstName().equals(user.getFirstName()) && entity.getLastName().equals(user.getLastName()))
            {
                return true;

            }
        }
        return false;
    }

    private Long generateID()
    {
        userIterable=findAll();
        int id = 0;
        boolean isValid=false;
        while(isValid==false)
        {
            isValid=true;
            id= Math.abs(new Random().nextInt());

           for(User user: userIterable)
           {

                if(user.getId()==Long.valueOf(id))
                    isValid=false;
            }

        }

        return Long.valueOf(id);

    }

    public String doHashing(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] resultByteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for(byte b: resultByteArray){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }



    @Override
    public User save(User entity) {
        String sql = "insert into users (first_name, last_name, username, password, image_url ) values (?, ?, ?, ?, ?)";
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
                String hashedPassword = doHashing(entity.getPassword());
                ps.setString(4, hashedPassword);
                ps.setString(5, "data/profile_pictures/default_user_profile.png");
                ps.executeUpdate();
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

                return entity;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public User update(User entity) {

        String sql = "update users set first_name = ?, last_name=? , password=?, image_url=? where id = ?";

        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);

        if(existsEntity(entity)==false)
            throw new IllegalArgumentException("User doesn't exist");


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getPassword());
            ps.setString(4, entity.getImageUrl());
            ps.setInt(5, entity.getId().intValue());



            ps.executeUpdate();

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public Map<Long, User> all() {
        return entities;
    }

    @Override
    public User existsUsernamePassword(String usernameUser, String passwordUser) {
        return null;
    }

    @Override
    public User existsUsername(String usernameUser) {

        Iterable<User> users = findAll();
        for(User user:users) {

            if (user.getUsername().equals(usernameUser))
            {
                return user;

            }
        }

        return null;
    }

    @Override
    public int size() {
        int contor=0;
        Iterable<User> users = findAll();
        for(User user:users)
            contor++;
        return contor;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Paginator<User> paginator = new Paginator<User>(pageable, this.findAll());
        return paginator.paginate();
    }

    public Page<User> findAllFilter(Pageable pageable,Iterable<User> users) {
        Paginator<User> paginator = new Paginator<User>(pageable, users);
        return paginator.paginate();
    }
}
