package com.example.lab6.socialnetwork.repository.database;

import com.example.lab6.socialnetwork.domain.Friendship;
import com.example.lab6.socialnetwork.domain.Tuple;
import com.example.lab6.socialnetwork.domain.validators.Validator;
import com.example.lab6.socialnetwork.repository.RepositoryFriendship;

import java.sql.*;
import java.util.*;

public class FriendshipDbRepository implements RepositoryFriendship<Long, Friendship> {
    private String url;
    private String username;
    private String password;
    private Validator<Friendship> validator;
    Map<Tuple<Long,Long>,Friendship> entities;

    public FriendshipDbRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.entities= new HashMap<>();
        loadData();
    }

    @Override
    public List<Friendship> getListFriendships()
    {
        List<Friendship> list=new ArrayList<>();
        for (var entry : entities.entrySet()) {
            list.add(entry.getValue());
        }

        return list;
    }


    public boolean existsEntity(Friendship entity){
        Iterable<Friendship> friendships = findAll();
        for(Friendship friendship:friendships) {

            if (entity.getID1() == friendship.getID1() && entity.getID2()==friendship.getID2())
            {
                return true;

            }
        }
        return false;
    }
    public boolean existsID(Tuple<Long,Long> id)
    {
        Iterable<Friendship> friendships = findAll();
        for(Friendship friendship:friendships) {

            if (id.getLeft() == friendship.getId().getLeft() && id.getRight()==friendship.getId().getRight())
            {
                return true;
            }
        }
        return false;

    }


    @Override
    public Friendship findOne(Tuple<Long,Long> id) {
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        if(entities.get(id) != null) {
            return entities.get(id);

        }
        else
            return null;

    }

    public void loadData()
    {
        if(entities!=null)
            entities.clear();

        Iterable<Friendship> friendships=findAll();
        for(Friendship friendship:friendships)
        {
            entities.put(friendship.getId(),friendship);
        }


    }
    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship>friendships  = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String data=resultSet.getString("com/example/lab6/data");



                Friendship friendship=new Friendship( id1,id2);
                Tuple<Long,Long> tuple = new Tuple<>(id1,id2);
                friendship.setId(tuple);
                friendship.setDate(data);


                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) {

        String sql = "insert into friendships (id1, id2, data ) values (?, ?, ?)";

        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);

        if(existsEntity(entity)==false) {

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1,(entity.getID1()).intValue());
                ps.setInt(2,(entity.getID2()).intValue());
                ps.setString(3, (entity.getDate()));
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
    public Friendship delete(Tuple<Long,Long> id) {
        Friendship entity=findOne(id);
        if(entity!=null)  // entity exists
        {

            String sql = "delete from friendships where id1 = ? and id2= ?";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {


                ps.setInt(1,id.getLeft().intValue());
                ps.setInt(2,id.getRight().intValue());
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
    public Friendship update(Friendship entity) {

        String sql = "update friendships set id1 = ?, id2=? where id1 = ? and id2=?";




        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);

        Tuple<Long,Long> tuple = new Tuple<>(entity.getID1(),entity.getID2());


        if(existsID(tuple)==true) //cant update this entity because this new entity already exist
        {

            throw new IllegalArgumentException("new friendship already exists");

        }

        if(existsID(entity.getId())==true) {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1,entity.getID1().intValue());
                ps.setInt(2,entity.getID2().intValue());
                ps.setInt(3,entity.getId().getLeft().intValue());
                ps.setInt(4,entity.getId().getRight().intValue());



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
    public Map<Tuple<Long,Long>, Friendship> all() {
        return entities;
    }

    @Override
    public int size() {
        int contor=0;
        Iterable<Friendship> friendships = findAll();
        for(Friendship friendship:friendships)
            contor++;
        return contor;
    }
}
