package com.example.lab6.socialnetwork.repository.database;

import com.example.lab6.socialnetwork.domain.*;
import com.example.lab6.socialnetwork.domain.validators.Validator;
import com.example.lab6.socialnetwork.repository.paging.Page;
import com.example.lab6.socialnetwork.repository.paging.Pageable;
import com.example.lab6.socialnetwork.repository.paging.Paginator;
import com.example.lab6.socialnetwork.repository.paging.PagingRepositoryMessages;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PagingMessageDbRepository implements PagingRepositoryMessages<Integer, Message> {
    private String url;
    private String username;
    private String password;
    private Validator<Message> validator;
    private List<Message> messageList = new ArrayList<>();

   private List<Conversation> conversations= new ArrayList<>();



    public PagingMessageDbRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        for(Conversation conversation : findAllConversations())
            conversations.add(conversation);

    }


    @Override
    public Message findOne(Integer aLong) throws IllegalArgumentException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages where id_message=" + String.valueOf(aLong));
             ResultSet resultSet = statement.executeQuery()) {


            while (resultSet.next()) {

                Integer idMessage = resultSet.getInt("id_message");
                Integer idConversation = resultSet.getInt("id_conversation");


                Message message = new Message(null,null,null,null,null);
                message.setIdConversation(idConversation);
                message.setId(idMessage);


                return message;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterable<Message> findAll() {
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

    private int generateID() {


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

    @Override
    public Message save(Message entity) {
        return null;
    }

    @Override
    public Message delete(Integer id) {
        Message entity=findOne(id);
        if(entity!=null)  // entity exists
        {



            String sql = "delete from messages where id_message = ?";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {


                ps.setInt(1,id);
                ps.executeUpdate();

                return entity;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public Message update(Message entity) {

        String sql = "update messages set last_message = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {


            ps.setString(1, entity.getMessage());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();
            return  entity;


        } catch (SQLException e) {
            e.printStackTrace();
        }

   return  null;


    }

    public List<MessageDTO> getMessageDTOsByConversationId(int id){
        List<MessageDTO> messageDTOList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages where (id_conversation = ?) ORDER BY message_date ASC ");) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {

                String usernameFrom = resultSet.getString("from_username");
                String date = resultSet.getString("message_date");
                String message = resultSet.getString("message_string");
                Integer reply = resultSet.getInt("id_reply");
                Integer idMessage = resultSet.getInt("id_message");

                MessageDTO messageDTO = new MessageDTO(usernameFrom, date, message);
                messageDTO.setReply(reply);
                messageDTO.setId(idMessage);

                messageDTOList.add(messageDTO);

            }
            return messageDTOList;
        } catch(SQLException e)

        {
            e.printStackTrace();
        }
        return  null;
    }

    public List<Conversation>getConversationsForUser(String usernameConnected)
    {
       conversations.clear();
        Iterable<Conversation> conversationsAll = findAllConversations();
        boolean ok;
        for(Conversation conversation: conversationsAll) {
            ok =false;
            for(String username : conversation.getUsernames())
            {
                if(Objects.equals(username,usernameConnected))
                    ok=true;
            }
            if(ok==true)
            {
                conversations.add(conversation);
            }

        }

        Collections.sort(conversations, new Comparator<Conversation>() {

            @Override
            public int compare(Conversation o1, Conversation o2) {
                return o2.getMessageDTO().getDateTime().compareTo(o1.getMessageDTO().getDateTime());
            }
        });

        return conversations;

    }
    private int generateIDConversation() {
        Iterable<Conversation> conversations = findAllConversations();

        int id = 0;
        boolean isValid = false;
        while (isValid == false) {
            isValid = true;
            id = Math.abs(new Random().nextInt());


            for (Conversation conversation: conversations) {
                if (conversation.getId() == id)
                    isValid = false;
            }

        }

        return id;

    }

    public void addConversation(Conversation conversation){
        String sql = "insert into conversations (id, users, last_message, last_message_date, last_message_username) values (?,?,?,?,?)";
        if(Objects.equals(conversation.getId(),null))
        conversation.setId(generateIDConversation());


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            List<String> userList = conversation.getUsernames();
            String usernames = "";
            for(String username: userList){
                usernames += username + ",";
            }


            ps.setInt(1, conversation.getId());
            ps.setString(2, usernames);
            ps.setString(3, conversation.getMessageDTO().getMessage());
            ps.setString(4, conversation.getMessageDTO().getDateTime());
            ps.setString(5, conversation.getMessageDTO().getUsername());
            conversations.add(conversation);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public MessageDTO getLastMessageDTOfromIdConversation(int id)
    {
        try (Connection connection = DriverManager.getConnection(url, username, password);
           PreparedStatement statement = connection.prepareStatement("SELECT * from messages where (id_conversation = ?) ORDER BY message_date DESC ");) {
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();


        while (resultSet.next()) {

            String usernameFrom = resultSet.getString("from_username");
            String date = resultSet.getString("message_date");
            String message = resultSet.getString("message_string");
            Integer reply = resultSet.getInt("id_reply");
            Integer idMessage = resultSet.getInt("id_message");

            MessageDTO messageDTO = new MessageDTO(usernameFrom, date, message);
            messageDTO.setReply(reply);
            messageDTO.setId(idMessage);

           return messageDTO;


        }

    } catch(SQLException e)

    {
        e.printStackTrace();
    }
        return  null;




    }


    public void updateConversation(Conversation conversation) {

        String sql = "update conversations set last_message = ?, last_message_date=?, last_message_username=? where id = ?";




        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {


            ps.setString(1, conversation.getMessageDTO().getMessage());
            ps.setString(2,  conversation.getMessageDTO().getDateTime());
            ps.setString(3,  conversation.getMessageDTO().getUsername());
            ps.setInt(4, conversation.getId());
            ps.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    public Iterable<Conversation> findAllConversations() {
        Set<Conversation> conversations = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from conversations");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String usernames = resultSet.getString("users");
                String lastMessage = resultSet.getString("last_message");
                String date = resultSet.getString("last_message_date");
                String lastUsername = resultSet.getString("last_message_username");



                List<String> usernamesConversation = new ArrayList<>();
                for (String username : usernames.split(","))
                    usernamesConversation.add(username);
                Conversation conversation = new Conversation(usernamesConversation);
                conversation.setId(id);
                MessageDTO messageDTO = new MessageDTO(lastUsername, date, lastMessage);
                conversation.setMessageDTO(messageDTO);
                conversations.add(conversation);
            }
            return conversations;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conversations;
    }


    @Override
    public Page<Message> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Conversation> findAllFilter(Pageable pageable, Iterable<Conversation> cond) {

        Paginator<Conversation> paginator = new Paginator<Conversation>(pageable, cond);
        return paginator.paginate();
    }


    public Iterable<MessageDTO> findAllMessages() {
        Set<MessageDTO> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String fromUsername = resultSet.getString("from_username");
                String toUsernames = resultSet.getString("to_usernames");
                String message = resultSet.getString("message_string");
                String messageDate = resultSet.getString("message_date");
                if(!Objects.equals(toUsernames, null)) {
                    MessageDTO message1 = new MessageDTO(fromUsername, messageDate, message);
                    message1.setUsernamesTo(toUsernames);
                    messages.add(message1);
                }
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }





    public Message addMessage(Message message) {
        validator.validate(message);

        //verifica user si list<user>

        message.setId(generateID());

        String sql = "insert into messages (id_message, from_username, to_usernames, message_string, message_date,id_conversation) values (?,?,?,?,?,?)";


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {


            ps.setInt(1, message.getId());
            ps.setString(2, message.getFrom().getUsername());
            String usersTo = "";
            for(User user: message.getTo())
            {
                usersTo = usersTo + user.getUsername()+",";
            }
            ps.setString(3, usersTo);
            ps.setString(4, message.getMessage());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = message.getDate().format(formatter);

            ps.setString(5, formattedDateTime);
            ps.setInt(6, message.getIdConversation());


            ps.executeUpdate();

            return message;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  null;

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




    public void sendRequest(String username1, String username2, String date) {
        if (Objects.equals(findStatus(username1, username2), null)) {
            //INSERT REQUEST    null -> pending
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
        } else if (Objects.equals(findStatus(username1, username2), "rejected")) {
           //UPDATE REQUEST  rejected -> pending

            String sql = "update friendship_requests set status=?,date=? where username1 = ? AND username2=? AND status = 'rejected' ";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, "pending");
                ps.setString(2, date);
                ps.setString(3, username1);
                ps.setString(4, username2);

                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }



    public List<String> getFriendshipRequests(String username1) {
        List<String> stringList = new ArrayList<>();
        String sql = "SELECT * from friendship_requests WHERE username2 = ? AND status = 'pending'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setString(1, username1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String username2 = resultSet.getString("username1");
                stringList.add(username2);

            }
            return stringList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<RequestDTO> requestDTOList(String username1) {
        List<RequestDTO> stringList = new ArrayList<>();
        String sql = "SELECT * from friendship_requests WHERE username2 = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setString(1, username1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String username2 = resultSet.getString("username1");
                String status = resultSet.getString("status");
                String date = resultSet.getString("date");

                stringList.add(new RequestDTO(username2, status, date));


            }
            return stringList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }


    public void acceptRequest(String username1, String username2) {
        if (!Objects.equals(findStatus(username2, username1), "pending"))
            throw new IllegalArgumentException("friendship request doesn't exist");


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


    }

    public void rejectRequest(String username1, String username2) {
        if (!Objects.equals(findStatus(username2, username1), "pending"))
            throw new IllegalArgumentException("friendship request doesn't exist");


        String sql = "update friendship_requests set status=?,username1=?,username2=? where username1 = ? AND username2=? AND status = 'pending' ";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "rejected");
            ps.setString(2, username1);
            ps.setString(3, username2);
            ps.setString(4, username2);
            ps.setString(5, username1);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public String findStatus(String username1, String username2) {
        String status = null;
        String sql = "SELECT * from friendship_requests WHERE username1 = ? AND username2=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setString(1, username1);
            statement.setString(2, username2);
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


        String sql = "delete from friendship_requests WHERE (username1 = ? AND username2=?)";


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username1);
            ps.setString(2, username2);

            ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}

