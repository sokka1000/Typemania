package com.example.lab6.socialnetwork.repository.database;

import com.example.lab6.socialnetwork.domain.*;
import com.example.lab6.socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MessageDbRepository {
    private String url;
    private String username;
    private String password;
    private Validator<Message> validator;
    private List<Message> messageList = new ArrayList<>();

    public MessageDbRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    /**
     * @param id * @return an - null if the message doesn't exist
     *           - the entity (id already exists)
     */
    public Message findOne(int id) {
        for (Message message : messageList) {
            if (message.getId() == id) {
                return message;

            }
        }
        return null;
    }


    public Message findOneFromDB(String username1, String username2, int id) {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages where username1=? AND username2=? AND id_message=?");) {
            statement.setString(1, username1);
            statement.setString(2, username2);
            statement.setInt(3, id);

            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {


                String messageString = resultSet.getString("message");
                String date = resultSet.getString("date");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

                Message message = new Message(null, null, messageString, dateTime, null);
                message.setId(id);
                return message;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;


    }

    public int generateID() {


        int id = 0;
        boolean isValid = false;
        while (isValid == false) {
            isValid = true;
            id = Math.abs(new Random().nextInt());


            for (Message message : messageList) {
                if (message.getId() == id)
                    isValid = false;
            }

        }

        return id;

    }

    public void addMessage(Message message) {
        validator.validate(message);

        //verifica user si list<user>


        message.setId(generateID());


        messageList.add(message);


        String sql = "insert into messages (id_message, from_username, to_usernames, message_string, message_date) values (?,?,?,?,?)";


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            List<User> userList = message.getTo();

            ps.setInt(1, message.getId());
            ps.setString(2, message.getFrom().getUsername());
            ps.setString(3, message.getUsersString());
            ps.setString(4, message.getMessage());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = message.getDate().format(formatter);

            ps.setString(5, formattedDateTime);



            ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public String findListById(int id) {
        String to = "";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages where id_message=?");) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();


           if(!resultSet.next()) {


                return null;
           }

            to = resultSet.getString("to_usernames");

            return to;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return to;


    }




    public void addReply(Message message) {
        validator.validate(message);

        //verifica user si list<user>


        message.setId(generateID());


        messageList.add(message);


        String sql = "insert into messages (id_message, from_username, to_usernames, message_string, message_date, id_reply) values (?,?,?,?,?,?)";


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            List<User> userList = message.getTo();

            ps.setString(2, message.getFrom().getUsername());
            ps.setString(3, message.getUsersString());
            ps.setString(4, message.getMessage());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = message.getDate().format(formatter);

            ps.setString(5, formattedDateTime);

            ps.setInt(6, message.getReply().getId());
            ps.setInt(1, message.getId());


            ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public List<MessageDTO> task3(String username1, String username2) {
        List<MessageDTO> messageDTOList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages where (from_username=? OR from_username=?) ORDER BY message_date");) {
            statement.setString(1, username1);
            statement.setString(2, username2);


            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {

                String usernameFrom = resultSet.getString("from_username");
                String usernamesTo = resultSet.getString("to_usernames");
                String date = resultSet.getString("message_date");
                String message = resultSet.getString("message_string");

                if (Objects.equals(usernameFrom, username1)) {
                    String[] list = usernamesTo.split(" ");
                    for (String username : list) {
                        if (Objects.equals(username, username2)) {
                            MessageDTO messageDTO = new MessageDTO(usernameFrom, date, message);
                            messageDTOList.add(messageDTO);
                        }

                    }
                }

                if (Objects.equals(usernameFrom, username2)) {
                    String[] list = usernamesTo.split(" ");
                    for (String username : list) {
                        if (Objects.equals(username, username1)) {
                            MessageDTO messageDTO = new MessageDTO(usernameFrom, date, message);
                            messageDTOList.add(messageDTO);
                        }

                    }
                }


            }

            return messageDTOList;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }


    public void sendRequest(String username1, String username2,String date) {
        if(!findUserInRequests(username1, username2)) {
            String sql = "insert into friendship_requests (username1, username2, status,date ) values (?, ?, ?,?)";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username1);
                ps.setString(2, username2);
                ps.setString(3, "pending");
                ps.setString(4, date);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            throw new IllegalArgumentException("friendship request already exists");
        }

    }

    public List<String> showFriendshipRequests(String username1){
        List<String> stringList = new ArrayList<>();
        String sql = "SELECT * from friendship_requests WHERE username2 = ? AND status = 'pending'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username1);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                String username2 = resultSet.getString("username1");
                stringList.add(username2);

            }
            return stringList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<RequestDTO> requestDTOList(String username1){
        List<RequestDTO> stringList = new ArrayList<>();
        String sql = "SELECT * from friendship_requests WHERE username2 = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username1);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                String username2 = resultSet.getString("username1");
                String status = resultSet.getString("status");
                String date=resultSet.getString("date");

                stringList.add(new RequestDTO(username2,status,date));


            }
            return stringList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }


    public void acceptRequest(String username1, String username2) {
        if(!Objects.equals(findStatus(username1, username2), "pending"))
            throw new IllegalArgumentException("friendship request doesn't exist");
        if(findUserInRequests(username1,username2)) {
            String sql = "update friendship_requests set status=? where username1 = ? AND username2=? AND status = 'pending' ";


            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {


                ps.setString(1, "approved");
                ps.setString(2, username2);
                ps.setString(3, username1);

                ps.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            throw new IllegalArgumentException("friendship request doesn't exist");
        }

    }

    public boolean findUserInRequests(String username1, String username2){
        String sql = "SELECT * from friendship_requests WHERE (username1 = ? AND username2=?) OR (username2 = ? AND username1=?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username2);
            statement.setString(2, username1);
            statement.setString(3, username2);
            statement.setString(4, username1);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next() == false) {
                return false;
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void rejectRequest(String username1, String username2) {
        if(!Objects.equals(findStatus(username1, username2), "pending"))
            throw new IllegalArgumentException("friendship request doesn't exist");
        if(findUserInRequests(username1,username2)) {
            String sql = "update friendship_requests set status=? where username1 = ? AND username2=? AND status = 'pending' ";


            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, "rejected");
                ps.setString(2, username2);
                ps.setString(3, username1);

                ps.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
        else {
            throw new IllegalArgumentException("friendship request doesn't exist");
        }
    }

    public String findStatus(String username1, String username2)
    {
        String status = null;
        String sql = "SELECT * from friendship_requests WHERE (username1 = ? AND username2=?) OR (username2 = ? AND username1=?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username2);
            statement.setString(2, username1);
            statement.setString(3, username2);
            statement.setString(4, username1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
               status = resultSet.getString("status");

            return status;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void deleteRequest(String username1, String username2) {


            String sql = "DELETE FROM friendship_requests WHERE (username1 = ? AND username2=?) OR (username2 = ? AND username1=?) ";


            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, username1);
                ps.setString(2, username2);
                ps.setString(3, username1);
                ps.setString(4, username2);

                ps.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();
            }


        }


    public void cancelRequest(String username1, String username2) {
        if(Objects.equals(findStatus(username1, username2), "approved"))
            throw new IllegalArgumentException("you can't cancel approved request");
        if(Objects.equals(findStatus(username1, username2), "pending"))
            throw new IllegalArgumentException("you can't cancel pending request");
        if(findUserInRequests(username1,username2)) {
            String sql = "update friendship_requests set status=? where username1 = ? AND username2=? AND status='rejected'";


            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, "pending");
                ps.setString(2, username2);
                ps.setString(3, username1);

                ps.executeUpdate();



            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
        else {
            throw new IllegalArgumentException("friendship request doesn't exist");
        }


    }



}
