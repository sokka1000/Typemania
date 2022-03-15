package com.example.lab6;

import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.domain.validators.UserValidator;
import com.example.lab6.socialnetwork.repository.database.PagingUserDbRepository;
import com.example.lab6.socialnetwork.repository.database.UserDbRepository;
import com.example.lab6.socialnetwork.service.UserService;
import com.example.lab6.socialnetwork.service.UserTaskService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegistrationController extends Controller implements Initializable {
    PagingUserDbRepository userDbRepository = new PagingUserDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres", "postgres",new UserValidator());
    UserTaskService userService = new UserTaskService(userDbRepository);

    @FXML
    private TextField fieldFirstName;
    @FXML
    private TextField fieldLastName;
    @FXML
    private TextField fieldUsername;
    @FXML
    private TextField fieldPassword;
    @FXML
    private TextField fieldConfirmPassword;

    @FXML
    private AnchorPane pane;

    @FXML
    public void closeApp(javafx.scene.input.MouseEvent actionEvent)  {
        Platform.exit();


    }


    @FXML
    public void backLogin(MouseEvent actionEvent) throws IOException {
            changeScene("login.fxml");
    }

    public void onRegistrationButtonClick(ActionEvent actionEvent)
    {
        String textFirstName = fieldFirstName.getText();
        String textLastName = fieldLastName.getText();
        String textUsername = fieldUsername.getText();
        String textPassword = fieldPassword.getText();
        String textConfirmPassword = fieldConfirmPassword.getText();


        System.out.println(textFirstName + textLastName + textUsername + textPassword + textConfirmPassword);
        if(textFirstName.equals("") || textLastName.equals("") || textUsername.equals("") || textPassword.equals("") || textConfirmPassword.equals("") )
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Error");
            alert.setHeaderText("");
            alert.setContentText("Please fill every field!");
            alert.showAndWait();

        }
        else {

            if (!textPassword.equals(textConfirmPassword)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Error");
                alert.setHeaderText("");
                alert.setContentText("Password and Confirm Password Field do not match!");
                alert.showAndWait();

            }
            else {


                User user = userService.existsUsername(textUsername);
                if (Objects.equals(user, null)) {
                    //add new user
                    User user1 = new User(textFirstName, textLastName);
                    user1.setUsername(textUsername);
                    user1.setPassword(textPassword);
                    userService.addUser(user1);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Welcome");
                    alert.setHeaderText("");
                    alert.setContentText("You have successfully registered");
                    alert.showAndWait();


                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Registration Error");
                    alert.setHeaderText("");
                    alert.setContentText("This username already exists!");
                    alert.showAndWait();

                }
            }

        }




    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TO DO

    }
}
