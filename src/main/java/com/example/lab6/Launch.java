package com.example.lab6;

import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.domain.validators.FriendshipValidator;
import com.example.lab6.socialnetwork.domain.validators.MessageTaskValidator;
import com.example.lab6.socialnetwork.domain.validators.UserValidator;
import com.example.lab6.socialnetwork.repository.database.PagingFriendshipDbRepository;
import com.example.lab6.socialnetwork.repository.database.PagingMessageDbRepository;
import com.example.lab6.socialnetwork.repository.database.PagingUserDbRepository;
import com.example.lab6.socialnetwork.service.FriendshipTaskService;
import com.example.lab6.socialnetwork.service.MessageTaskService;
import com.example.lab6.socialnetwork.service.UserTaskService;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Launch extends Application {
    public static Stage stage=null;

    private double xOffset =0;
    private double yOffset =0;


    @Override
    public void start(Stage stage) throws IOException, ParseException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
       Scene scene = new Scene(root, 800, 600);



        //UNDECORATED

        stage.initStyle(StageStyle.DECORATED);
       // stage.getIcons().add(new Image("C:\\Games\\lab6_proiect\\map224-catalindiana-lab6\\src\\main\\resources\\com\\example\\lab6\\data\\invers.png"));
        stage.setTitle("Typemania");
        stage.setScene(scene);
        stage.setMinHeight(637);
        stage.setMinWidth(820);
        this.stage=stage;
        stage.setMaxHeight(637);
        stage.setMaxWidth(820);

        stage.show();

    }


    public static void main(String[] args) {
        launch();


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



    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/network", "postgres", "postgres");
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



}