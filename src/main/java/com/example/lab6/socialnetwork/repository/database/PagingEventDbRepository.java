package com.example.lab6.socialnetwork.repository.database;

import com.example.lab6.socialnetwork.domain.Event;
import com.example.lab6.socialnetwork.domain.validators.Validator;
import com.example.lab6.socialnetwork.repository.paging.Page;
import com.example.lab6.socialnetwork.repository.paging.Pageable;
import com.example.lab6.socialnetwork.repository.paging.Paginator;
import com.example.lab6.socialnetwork.repository.paging.PagingRepositoryEvent;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;


public class PagingEventDbRepository implements PagingRepositoryEvent<Integer, Event> {

    private String url;
    private String username;
    private String password;
    private Validator<Event> validator;



    public PagingEventDbRepository(String url, String username, String password, Validator<Event> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;

    }



    @Override
    public Event findOne(Integer integer) throws IllegalArgumentException {
        return null;
    }

    @Override
    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from events");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String participants = resultSet.getString("participants");
                String date = resultSet.getString("date");
                String imageUrl=resultSet.getString("imageUrl");



                List<String> usernamesEvent = new ArrayList<>();
                for (String username : participants.split(","))
                    usernamesEvent.add(username);
                Event event = new Event(title);
                event.setDescription(description);
                event.setParticipants(usernamesEvent);
                event.setDate(date);
                event.setImageUrl(imageUrl);
                event.setId(id);


                events.add(event);
            }

            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });




            return events;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    private String convertListToString(List<String> list)
    {

        String finalString="";
        for(String string : list)
            finalString+=string+",";
        return  finalString;

    }

    public void  addParticipantAtEvent(Event event,String participantUsername)
    {
        String sql = "update events set participants=? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            List<String> usernames= event.getParticipants();
            usernames.add(participantUsername);




            ps.setString(1,convertListToString(usernames));
            ps.setInt(2,event.getId());



            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }





    }
    public void  removeParticipantAtEvent(Event event,String participantUsername)
    {

        String sql = "update events set participants=? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            List<String> usernames= event.getParticipants();
            usernames.remove(participantUsername);




            ps.setString(1,convertListToString(usernames));
            ps.setInt(2,event.getId());



            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }



    }




    @Override
    public Event save(Event entity) {
        return null;
    }

    @Override
    public Event delete(Integer integer) {
        return null;
    }

    @Override
    public Event update(Event entity) {
        return null;
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        Paginator<Event> paginator = new Paginator<Event>(pageable,findAll());
        return paginator.paginate();
    }


    private boolean existStringInList(String giveString,List<String> list)
    {

        for(String string : list)
        {
            if(Objects.equals(string,giveString))
                return  true;
        }
        return false;

    }


    public List<Event> findAll(String usernameParticipant) throws ParseException {
        List<Event> events=new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);


        for(Event event : findAll())
        {

            Date dateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(event.getDate());
            Date dateTime2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedDateTime);
            Long diff =dateTime.getTime()-dateTime2.getTime();


            if(existStringInList(usernameParticipant,event.getParticipants()) && diff <604800000 && diff>0)
            {
                events.add(event);
            }
        }

            return events;
    }

    public boolean isEventComingSoon(Event event) throws ParseException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        Date dateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(event.getDate());
        Date dateTime2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedDateTime);
        Long diff =dateTime.getTime()-dateTime2.getTime();


        if(diff <604800000 && diff>0)
        {
            return true;
        }
        return  false;


    }




    @Override
    public Page<Event> findAllFilter(Pageable pageable, Iterable<Event> cond) {
        Paginator<Event> paginator = new Paginator<Event>(pageable, cond);
        return paginator.paginate();
    }



}
