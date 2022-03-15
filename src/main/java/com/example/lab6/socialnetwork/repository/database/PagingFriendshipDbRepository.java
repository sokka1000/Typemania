package com.example.lab6.socialnetwork.repository.database;

import com.example.lab6.socialnetwork.domain.Friendship;
import com.example.lab6.socialnetwork.domain.MessageDTO;
import com.example.lab6.socialnetwork.domain.Tuple;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.domain.validators.Validator;
import com.example.lab6.socialnetwork.repository.paging.Page;
import com.example.lab6.socialnetwork.repository.paging.Pageable;
import com.example.lab6.socialnetwork.repository.paging.Paginator;
import com.example.lab6.socialnetwork.repository.paging.PagingRepositoryFriendship;

import java.sql.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PagingFriendshipDbRepository implements PagingRepositoryFriendship<Long, Friendship> {
    private String url;
    private String username;
    private String password;
    private Validator<Friendship> validator;
    Map<Tuple<Long,Long>,Friendship> entities;

    public PagingFriendshipDbRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.entities= new HashMap<>();

    }


    @Override
    public Friendship findOne(Tuple<Long, Long> id) {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships where ((id1="+ String.valueOf(id.getLeft().intValue())+" and id2="+ String.valueOf(id.getRight().intValue())+") or (id1="+ String.valueOf(id.getRight().intValue())+" and id2="+ String.valueOf(id.getLeft().intValue())+"))");
             ResultSet resultSet = statement.executeQuery()) {


            while (resultSet.next()) {

                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String data=resultSet.getString("data");



                Friendship friendship=new Friendship( id1,id2);
                Tuple<Long,Long> tuple = new Tuple<>(id1,id2);
                friendship.setId(tuple);
                friendship.setDate(data);
                return  friendship;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Friendship> getListFriendships() {
        Iterable<Friendship> friendships  = findAll();
        List<Friendship> list=new ArrayList<>();
        for (var entry : friendships) {
            list.add(entry);
        }

        return list;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships  = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String data=resultSet.getString("data");



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

                return null;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    @Override
    public Friendship delete(Tuple<Long, Long> id) {
        Friendship entity=findOne(id);
        if(entity!=null)  // entity exists
        {

            String sql = "delete from friendships where (id1 = ? and id2= ?) or (id2 = ? and id1= ?)";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {


                ps.setInt(1,id.getLeft().intValue());
                ps.setInt(2,id.getRight().intValue());
                ps.setInt(3,id.getLeft().intValue());
                ps.setInt(4,id.getRight().intValue());
                ps.executeUpdate();

                return entity;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return null;
    }


    private boolean existsEntity(Friendship entity){
        Iterable<Friendship> friendships = findAll();
        for(Friendship friendship:friendships) {

            if (entity.getID1() == friendship.getID1() && entity.getID2()==friendship.getID2())
            {
                return true;

            }
        }
        return false;
    }
    private boolean existsID(Tuple<Long,Long> id)
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

                return null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    @Override
    public Map<Tuple<Long, Long>, Friendship> all() {
        return null;
    }

    @Override
    public int size() {
        int contor=0;
        Iterable<Friendship> friendships = findAll();
        for(Friendship friendship:friendships)
            contor++;
        return contor;
    }

    @Override
    public Page<Friendship> findAll(Pageable pageable) {
        Paginator<Friendship> paginator = new Paginator<Friendship>(pageable, this.findAll());
        return paginator.paginate();
    }

    public Iterable<Friendship> findAllFilter(Predicate<Friendship> cond)
    {
        Iterable<Friendship> friendshipIterable=this.findAll();
        List<Friendship> friendshipList=new ArrayList<>();
        for(Friendship friendship : friendshipIterable)
            friendshipList.add(friendship);

        return   friendshipList.stream()
                .filter(cond)
                .collect(Collectors.toSet());

    }

    public Page<Friendship> findAllFilter(Pageable pageable,Iterable<Friendship> friendships) {
        Paginator<Friendship> paginator = new Paginator<Friendship>(pageable, friendships);
        return paginator.paginate();
    }

    public Map<String, String> allFriends(String usernameUser) {
        Map<String, String> friends = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship_requests where (username1=? OR username2=?) AND status= 'approved' ");) {
            statement.setString(1, usernameUser);
            statement.setString(2, usernameUser);


            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {

                String username1 = resultSet.getString("username1");
                String username2 = resultSet.getString("username2");
                String date = resultSet.getString("date");

                if (Objects.equals(username1, usernameUser))
                    friends.put(username2, date);
                if (Objects.equals(username2, usernameUser))
                    friends.put(username1, date);


            }

            return friends;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}
