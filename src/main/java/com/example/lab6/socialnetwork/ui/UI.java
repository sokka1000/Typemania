package com.example.lab6.socialnetwork.ui;

import com.example.lab6.socialnetwork.domain.*;
import com.example.lab6.socialnetwork.domain.validators.*;
import com.example.lab6.socialnetwork.service.FriendshipService;
import com.example.lab6.socialnetwork.service.MessageService;
import com.example.lab6.socialnetwork.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.function.Predicate;

public class UI {
    private FriendshipService friendshipService;
    private UserService userService;
    private MessageService messageService;
    private  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Validator<String> nameValidator= new NameValidator();
    private Validator<String> numberValidator= new NumberValidator();


    public UI(UserService userService,FriendshipService friendshipService,MessageService messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
    }

    private void addUserUI() throws IOException {
        String firstName;
        String lastName;
        String string_id;
        /*
        Long id;

        System.out.println("ID:");
        string_id = reader.readLine();
        numberValidator.validate(string_id);
        id=Long.parseLong(string_id);
        */
        System.out.println("firstName:");
        firstName = reader.readLine();
        nameValidator.validate(firstName);

        System.out.println("lastName:");
        lastName  = reader.readLine();
        nameValidator.validate(lastName);

        System.out.println("username:");
        String username  = reader.readLine();

        System.out.println("password:");
        String password  = reader.readLine();


        User user=new User(firstName,lastName);
        user.setUsername(username);
        user.setPassword(password);
       // user.setId(id);
        User user2=userService.addUser(user);
        if(user2==user)
            System.out.println("Existent user!\n");



    }
    private void deleteUserUI() throws IOException
    {
        String string_id;
        Long id;

        System.out.println("ID:");
        string_id = reader.readLine();
        numberValidator.validate(string_id);
        id=Long.parseLong(string_id);

        User user=userService.deleteUser(id);
        if(user==null)
            System.out.println("Nonexistent  user!\n");
        else
        {
            Iterable<Friendship> friendships=friendshipService.getAll();
            Vector<Friendship> friendshipVector=new Vector<>();

             for(Friendship friendship : friendships)
             {
                    if(friendship.getID1()==id || friendship.getID2()==id)
                    {
                        friendshipVector.add(friendship);

                    }

             }

             for(Friendship friendship : friendshipVector)
             {
                 friendshipService.deleteFriendship(new Tuple<>(friendship.getID1(),friendship.getID2()));
             }


        }



    }

    private void showUsersUI() {
        Iterable<User> list= userService.getAll();

        for(User u :list)
        {
            System.out.println(u.toString());
        }


    }

    private void addFriendshipUI() throws IOException {

        String string_id1;
        Long id1;
        String string_id2;
        Long id2;

        System.out.println("ID1:");
        string_id1 = reader.readLine();
        numberValidator.validate(string_id1);
        id1=Long.parseLong(string_id1);

        System.out.println("ID2:");
        string_id2 = reader.readLine();
        numberValidator.validate(string_id2);
        id2=Long.parseLong(string_id2);


        Friendship friendship=new Friendship(id1,id2);
        friendship.setId(new Tuple<>(id1,id2));
        Friendship friendship1=friendshipService.addFriendship(friendship,userService.all());

        Friendship friendship2=new Friendship(id2,id1);
        friendship2.setId(new Tuple<>(id2,id1));
        Friendship friendship3=friendshipService.addFriendship(friendship2,userService.all());

        if(friendship1==friendship || friendship2== friendship3) {
            System.out.println("Existent friendship!\n");

        }


    }
    private void deleteFriendshipUI() throws IOException {
        String string_id1;
        Long id1;
        String string_id2;
        Long id2;

        System.out.println("ID1:");
        string_id1 = reader.readLine();
        numberValidator.validate(string_id1);
        id1=Long.parseLong(string_id1);

        System.out.println("ID2:");
        string_id2 = reader.readLine();
        numberValidator.validate(string_id2);
        id2=Long.parseLong(string_id2);


        Tuple<Long,Long> tuple1= new Tuple<>(id1,id2);
        Tuple<Long,Long> tuple2= new Tuple<>(id2,id1);

        Friendship friendship=friendshipService.deleteFriendship(tuple1);
        Friendship friendship1=friendshipService.deleteFriendship(tuple2);
        if(friendship==null || friendship1==null)
            System.out.println("Nonexistent  friendship!\n");


    }
    private void showFriendshipUI(){

        Iterable<Friendship> list= friendshipService.getAll();

        for(Friendship friendship :list)
        {
            System.out.println(friendship.toString());
        }

    }


    private void numberOfCommunitiesUI()
    {

        System.out.println("Number of communities : "+ friendshipService.numberOfCommunities(userService.getAll()));


    }

    private  void sociableCommunityUI()
    {
            long[] components=new long[100];
           components = friendshipService.sociableCommunity(userService.getAll());



        if(components[0]==0)
            System.out.println("Doesn't exist!\n");
        else {

            System.out.println("Sociable Community:");
            for (int g = 1; g <= components[0]; g++)
                System.out.println(components[g]);
        }

    }

    private void updateFriendshipUI() throws IOException {

        String string_id1;
        Long id1;
        String string_id2;
        Long id2;
        String string_id3;
        Long id3;
        String string_id4;
        Long id4;


        System.out.println("ID1:");
        string_id1 = reader.readLine();
        numberValidator.validate(string_id1);
        id1=Long.parseLong(string_id1);

        System.out.println("ID2:");
        string_id2 = reader.readLine();
        numberValidator.validate(string_id2);
        id2=Long.parseLong(string_id2);

        System.out.println("ID1_NEW:");
        string_id3 = reader.readLine();
        numberValidator.validate(string_id3);
        id3=Long.parseLong(string_id3);

        System.out.println("ID2_NEW:");
        string_id4 = reader.readLine();
        numberValidator.validate(string_id4);
        id4=Long.parseLong(string_id4);


        Friendship friendship=new Friendship(id3,id4);
        friendship.setId(new Tuple<>(id1,id2));
        Friendship friendship1=friendshipService.updateFriendship(friendship,userService.all());

        Friendship friendship2=new Friendship(id4,id3);
        friendship2.setId(new Tuple<>(id2,id1));
        Friendship friendship3=friendshipService.updateFriendship(friendship2,userService.all());

        if(friendship1==friendship || friendship2== friendship3) {
            System.out.println("Existent friendship!\n");

        }
    }

    private void updateUserUI() throws IOException {

        String firstName;
        String lastName;
        String string_id;

        Long id;

        System.out.println("ID:");
        string_id = reader.readLine();
        numberValidator.validate(string_id);
        id=Long.parseLong(string_id);

        System.out.println("firstName:");
        firstName = reader.readLine();
        nameValidator.validate(firstName);

        System.out.println("lastName:");
        lastName  = reader.readLine();
        nameValidator.validate(lastName);

        User user=new User(firstName,lastName);
        user.setId(id);

        User user2=userService.updateUser(user);
        if(user2==user)
            System.out.println("Inexistent user!\n");
    }

    private void findFriendshipUI() throws IOException {
        String string_id1;
        Long id1;
        String string_id2;
        Long id2;

        System.out.println("ID:");
        string_id1 = reader.readLine();
        numberValidator.validate(string_id1);
        id1=Long.parseLong(string_id1);

        System.out.println("ID:");
        string_id2 = reader.readLine();
        numberValidator.validate(string_id2);
        id2=Long.parseLong(string_id2);


        Friendship friendship=friendshipService.findFriendship(new Tuple<>(id1,id2));
        if(friendship==null)
            System.out.println("Friendship doesn't exist");
        else
            System.out.println(friendship);






    }

    private void findUserUI() throws IOException {
        String string_id;
        Long id;

        System.out.println("ID:");
        string_id = reader.readLine();
        numberValidator.validate(string_id);
        id=Long.parseLong(string_id);

        User user2=userService.findUser(id);
        if(user2==null)
            System.out.println("User doesn't exist");
        else
            System.out.println(user2);


    }


    private void task1UI() throws IOException {
        Long id;
        String string_id;

        System.out.println("ID:");
        string_id = reader.readLine();
        numberValidator.validate(string_id);
        id=Long.parseLong(string_id);

        User user2=userService.findUser(id);

        if(user2==null)
            System.out.println("User doesn't exist");
        else
        {

            Predicate<Friendship> byId= x->x.getID1().equals(id);
            List<Friendship> friendships=friendshipService.getListFriendships();

            friendships.stream()
                    .filter(byId)
                    .map(x-> new FriendshipDTO(userService.findUser(x.getID2()).getFirstName(),userService.findUser(x.getID2()).getLastName(),x.getDate()))
                    .forEach(x-> System.out.println(x));

        }
    }



    public void task2UI() throws IOException{
        Long id;
        String string_id;
        String month;

        System.out.println("ID:");
        string_id = reader.readLine();
        numberValidator.validate(string_id);
        id=Long.parseLong(string_id);

        System.out.println("Month:");
        month = reader.readLine();


        numberValidator.validate(month);
        int month2 = Integer.parseInt(month);

         if(month2<1 || month2>12)
             throw new IllegalArgumentException("invalid month");

        User user2=userService.findUser(id);

        if(user2==null)
            System.out.println("User doesn't exist");
        else
        {

            Predicate<Friendship> byId= x->x.getID1().equals(id);
            Predicate<Friendship> byMonth = x->x.getMonth().equals(month);
            Predicate<Friendship> filtered = byId.and(byMonth);
            List<Friendship> friendships=friendshipService.getListFriendships();

            friendships.stream()
                    .filter(filtered)
                    .map(x-> new FriendshipDTO(userService.findUser(x.getID2()).getFirstName(),userService.findUser(x.getID2()).getLastName(),x.getDate()))
                    .forEach(x-> System.out.println(x));

        }
    }



    private void task3UI() throws IOException {

        System.out.println("username1:");
       String username1 = reader.readLine();

        System.out.println("username2:");
        String username2 = reader.readLine();

        User user1=userService.existsUsername(username1);
        User user2=userService.existsUsername(username2);


        if(user1==null || user2==null)
        {

            System.out.println("username invalid");
        }
        else {

            List<MessageDTO> messageDTOList = messageService.task3(username1, username2);

            for (MessageDTO messageDTO : messageDTOList) {
                System.out.println(messageDTO);

            }
        }

    }


    private void sendMessageUI() throws IOException {

        String username;
        String password;
        System.out.println("Login:");

        System.out.println("username:");
        username = reader.readLine();
        System.out.println("password");
        password = reader.readLine();

       User user=userService.existsUsernamePassword(username,password);
        if(user!=null)
        {
            System.out.println("message:");
            String messageString= reader.readLine();

            LocalDateTime dateTime=LocalDateTime.now();

            List<String> stringList=new ArrayList<>();



            System.out.println("users:");
            String userString= reader.readLine();

            while(userString.compareTo("stop")!=0)
            {
                stringList.add(userString);
                userString= reader.readLine();
            }


            List<User> userList=new ArrayList<>();
            for(String userNewString:stringList)
            {
               User userNew=userService.existsUsername(userNewString);
               if(userNew!=null)
               userList.add(userNew);
               else
               {
                   System.out.println(userNewString + " is invalid!\n");
               }
            }


            Message message=new Message(user,userList,messageString,dateTime,null);




            messageService.sendMessage(message);



        }
        else
        {
            System.out.println("Username or password invalid!\n");
        }


    }



    private void sendReplyUI() throws IOException {

        String username;
        String password;
        System.out.println("Login:");

        System.out.println("username:");
        username = reader.readLine();
        System.out.println("password");
        password = reader.readLine();

        User user=userService.existsUsernamePassword(username,password);
        if(user!=null)
        {
            System.out.println("message:");
            String messageString= reader.readLine();

            LocalDateTime dateTime=LocalDateTime.now();

            List<String> stringList=new ArrayList<>();



            System.out.println("users:");
            String userString= reader.readLine();

            while(userString.compareTo("stop")!=0)
            {
                stringList.add(userString);
                userString= reader.readLine();
            }


            List<User> userList=new ArrayList<>();
            for(String userNewString:stringList)
            {
                User userNew=userService.existsUsername(userNewString);
                if(userNew!=null)
                    userList.add(userNew);
                else
                {
                    System.out.println(userNewString + " is invalid!\n");
                }
            }

            System.out.println("id reply:");
            String string_id = reader.readLine();
            numberValidator.validate(string_id);
            int id = Integer.parseInt(string_id);

            Message messagereply = new Message(null, null, null, null, null);
            messagereply.setId(id);



            Message message=new Message(user,userList,messageString,dateTime,messagereply);




            messageService.sendReply(message);



        }
        else
        {
            System.out.println("Username or password invalid!\n");
        }


    }

    private void sendFriendshipRequest() throws IOException {
        String username;
        String password;
        System.out.println("Login->>>");

        System.out.println("username:");
        username = reader.readLine();
        System.out.println("password");
        password = reader.readLine();

        User user=userService.existsUsernamePassword(username,password);

        if(user!=null)
        {

            System.out.println("to user->>");
            System.out.println("username:");
            String username2= reader.readLine();
            User user2=userService.existsUsername(username2);
            if(user2 != null)
                if(!Objects.equals(username, username2)) {
                    LocalDateTime dateTime=LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = dateTime.format(formatter);
                    messageService.sendRequest(username, username2,formattedDateTime);


                }
            else{
                System.out.println("invalid username");
            }

        }
        else
        {
            System.out.println("Username or password invalid!\n");

        }
    }


    private void showFriendshipRequestsUI() throws IOException {
        String username;
        String password;
        System.out.println("Login->>>");

        System.out.println("username:");
        username = reader.readLine();
        System.out.println("password");
        password = reader.readLine();

        User user=userService.existsUsernamePassword(username,password);

        if(user!=null) {
            List<String> stringList = messageService.showFriendshipRequests(username);
            System.out.println("Friendship requests:");
            for(String username2 : stringList){
                System.out.println(username2);
            }
        }else
        {
            System.out.println("Username or password invalid!\n");

        }

    }

    private void acceptRequest() throws IOException {
        String username;
        String password;
        System.out.println("Login->>>");

        System.out.println("username:");
        username = reader.readLine();
        System.out.println("password");
        password = reader.readLine();

        User user=userService.existsUsernamePassword(username,password);

        if(user!=null) {
            System.out.println("to user->>");
            System.out.println("username:");
            String username2= reader.readLine();
            User user2=userService.existsUsername(username2);
            if(user2 != null) {
                try {
                    messageService.acceptRequest(username, username2);
                    Friendship friendship = new Friendship(user2.getId(), user.getId());
                    friendship.setId(new Tuple<>(user2.getId(), user.getId()));
                    friendshipService.addFriendship(friendship, userService.all());

                    Friendship friendship2 = new Friendship(user.getId(), user2.getId());
                    friendship2.setId(new Tuple<>(user.getId(), user2.getId()));
                    friendshipService.addFriendship(friendship2, userService.all());
                } catch (IllegalArgumentException illegalArgumentException) {
                    System.out.println(illegalArgumentException.getMessage());
                }
            }
            else{
                System.out.println("invalid username");
            }
        }else
        {
            System.out.println("Username or password invalid!\n");

        }
    }

    private void rejectRequest() throws IOException {
        String username;
        String password;
        System.out.println("Login->>>");

        System.out.println("username:");
        username = reader.readLine();
        System.out.println("password");
        password = reader.readLine();

        User user=userService.existsUsernamePassword(username,password);

        if(user!=null) {
            System.out.println("to user->>");
            System.out.println("username:");
            String username2= reader.readLine();
            User user2=userService.existsUsername(username2);
            if(user2 != null)
                messageService.rejectRequest(username, username2);
            else{
                System.out.println("invalid username");
            }
        }else
        {
            System.out.println("Username or password invalid!\n");

        }
    }


    public void run() throws IOException {

        System.out.println("MENU");
        String command;
        System.out.println("add1 --- add user");
        System.out.println("add2 --- add friendship");
        System.out.println("delete1 --- delete user");
        System.out.println("delete2 --- delete friendship");
        System.out.println("3 --- number of communities");
        System.out.println("4 --- sociable community");
        System.out.println("update1---- update user");
        System.out.println("update2---- update friendship");
        System.out.println("task1 ---- all friendships of a user");
        System.out.println("task2 ---- all friendships of a user by month");
        System.out.println("task3 ---- conversations of two users");
        System.out.println("send message ---- send a message to a user");
        System.out.println("send reply ---- send a reply to a user");
        System.out.println("send request ---- send a friendship request to a user");
        System.out.println("accept request ---- accept a friendship request");
        System.out.println("reject request ---- reject a friendship request");
        System.out.println("show requests ---- show all friendship requests for a user");
        System.out.println("exit");



        while(1<2)
        {

            System.out.println(">>");
            // Reading data using readLine
            command = reader.readLine();

            try {
                if ("exit".compareTo(command)==0) {
                    System.out.println("Goodbye!");
                    break;
                }
                else if ("add1".compareTo(command)==0)
                {
                    addUserUI();

                }
                else if ("show1".compareTo(command)==0)
                {
                    showUsersUI();

                }
                else if("delete1".compareTo(command)==0)
                {
                    deleteUserUI();
                }
                else if ("add2".compareTo(command)==0)
                {
                    addFriendshipUI();

                }
                else if ("show2".compareTo(command)==0)
                {
                    showFriendshipUI();

                }
                else if("delete2".compareTo(command)==0)
                {
                    deleteFriendshipUI();
                }
                else if("3".compareTo(command)==0)
                {
                    numberOfCommunitiesUI();
                }
                else if("4".compareTo(command)==0)
                {
                    sociableCommunityUI();
                }
                else if("update1".compareTo(command)==0)
                {
                    updateUserUI();
                }
                else if("update2".compareTo(command)==0)
                {
                    updateFriendshipUI();
                }
                else if("find1".compareTo(command)==0)
                {
                    findUserUI();
                }
                else if("find2".compareTo(command)==0)
                {
                    findFriendshipUI();
                }
                else if("task1".compareTo(command)==0)
                {
                    task1UI();
                }
                else if("task2".compareTo(command)==0)
                {
                    task2UI();
                }
                else if("task3".compareTo(command)==0)
                {
                    task3UI();
                }
                else if("send message".compareTo(command)==0)
                {
                    sendMessageUI();
                }
                else if("send reply".compareTo(command)==0)
                {
                    sendReplyUI();
                }
                else if("send request".compareTo(command)==0)
                {
                    sendFriendshipRequest();
                }
                else if("show requests".compareTo(command)==0)
                {
                    showFriendshipRequestsUI();
                }
                else if("accept request".compareTo(command)==0)
                {
                    acceptRequest();
                }
                else if("reject request".compareTo(command)==0)
                {
                   rejectRequest();
                }
                else
                    System.out.println("invalid command!\n");

            }
            catch (ValidationException validationException) {
                System.out.println(validationException.getMessage());
            }
            catch (IOException ioException)
            {
                System.out.println(ioException.getMessage());
            }
            catch (IllegalArgumentException illegalArgumentException)
            {
                System.out.println(illegalArgumentException.getMessage());
            }

        }


    }



}
