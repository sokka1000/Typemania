package com.example.lab6.socialnetwork;

import com.example.lab6.socialnetwork.domain.Friendship;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.domain.validators.FriendshipValidator;
import com.example.lab6.socialnetwork.domain.validators.MessageValidator;
import com.example.lab6.socialnetwork.domain.validators.UserValidator;
import com.example.lab6.socialnetwork.repository.RepositoryFriendship;
import com.example.lab6.socialnetwork.repository.RepositoryUser;
import com.example.lab6.socialnetwork.repository.database.FriendshipDbRepository;
import com.example.lab6.socialnetwork.repository.database.MessageDbRepository;
import com.example.lab6.socialnetwork.repository.database.UserDbRepository;
import com.example.lab6.socialnetwork.repository.file.UtilizatorFile;
import com.example.lab6.socialnetwork.repository.file.UtilizatorFile0;
import com.example.lab6.socialnetwork.repository.memory.InMemoryRepository;
import com.example.lab6.socialnetwork.repository.memory.InMemoryRepository0;
import com.example.lab6.socialnetwork.service.FriendshipService;
import com.example.lab6.socialnetwork.service.MessageService;
import com.example.lab6.socialnetwork.service.UserService;
import com.example.lab6.socialnetwork.ui.UI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {


        String filenameFriendship=new String("com/example/lab6/data/friendships.csv");
        String filenameUser=new String("com/example/lab6/data/users.csv");


        //IN MEMORY
        RepositoryFriendship<Long, Friendship> friendshipInMemoriyRepository=new InMemoryRepository<>(new FriendshipValidator());
        RepositoryUser<Long, User> userInMemoryRepository=new InMemoryRepository0<>(new UserValidator());

        //IN FILE

        RepositoryFriendship<Long, Friendship> friendshipInFileRepository=new UtilizatorFile(filenameFriendship,new FriendshipValidator());
        RepositoryUser<Long,User> userInFileRepository=new UtilizatorFile0(filenameUser,new UserValidator());

        //IN DATABASE

        String url="jdbc:postgresql://localhost:5432/network";
        String username="postgres";
        String password="postgres";

        RepositoryFriendship<Long,Friendship> friendshipDbRepository=new FriendshipDbRepository(url,username,password,new FriendshipValidator());
        RepositoryUser<Long,User> userDbRepository=new UserDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "postgres",new UserValidator());
        MessageDbRepository messageDbRepository=new MessageDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "postgres",new MessageValidator());


        FriendshipService friendshipService=new FriendshipService(friendshipDbRepository);
        UserService userService=new UserService(userDbRepository);
        MessageService messageService=new MessageService(messageDbRepository);

        UI ui=new UI(userService,friendshipService,messageService);

        ui.run();



    }
}


