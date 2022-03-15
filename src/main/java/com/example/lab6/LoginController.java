package com.example.lab6;

import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.domain.validators.UserValidator;
import com.example.lab6.socialnetwork.repository.database.PagingUserDbRepository;
import com.example.lab6.socialnetwork.repository.database.UserDbRepository;
import com.example.lab6.socialnetwork.service.FriendshipTaskService;
import com.example.lab6.socialnetwork.service.MessageTaskService;
import com.example.lab6.socialnetwork.service.UserService;
import com.example.lab6.socialnetwork.service.UserTaskService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable  {


        PagingUserDbRepository userDbRepository = new PagingUserDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "postgres",new UserValidator());
        UserTaskService userService = new UserTaskService(userDbRepository);



/*
        public void setService(UserTaskService userTaskService,FriendshipTaskService friendshipTaskService,MessageTaskService messageTaskService) {
                userService = userTaskService;
                friendshipService = friendshipTaskService;
                messageService=messageTaskService;

              //  service.addObserver(this);
              //  initModel();
        }
*/

        @FXML
        private Label labelPasswordError;
        @FXML
        private Button btnLogin;
        @FXML
        private Button btnExit;
        @FXML
        private Button btnSignIn;


        @FXML
        private TextField fieldUsername;

        @FXML
        private TextField fieldPassword;

        @FXML
        private AnchorPane loginScene;

        @FXML
        private Pane contentArea;




        @FXML
        public void closeApp(javafx.scene.input.MouseEvent actionEvent)  {
                Platform.exit();


        }

        @FXML
        public void openRegistration(MouseEvent mouseEvent) throws IOException {
               Parent fxml= FXMLLoader.load(getClass().getResource("registration.fxml" ));

               Data.contentAreaOLD=contentArea;
                contentArea.getChildren().removeAll();
                contentArea.getChildren().setAll(fxml);


        }

        @FXML
        public void onForgotPasswordClick(MouseEvent mouseEvent) throws IOException {

                changeScene("forgotPassword.fxml");
        }


        @FXML
        public void onLoginButtonClick(ActionEvent actionEvent) throws IOException {

                String textUsername = fieldUsername.getText();
                String textPassword = fieldPassword.getText();
                String hashedPassword = userService.doHashing(textPassword);
                if(textUsername.equals("") || textPassword.equals(""))
                        labelPasswordError.setText("Enter Correct Username/Password");
                else
                {
                        User user = userService.existsUsername(textUsername);

                        if(Objects.equals(user, null))
                                labelPasswordError.setText("Enter Correct Username/Password");
                        else
                        {
                                if(!Objects.equals(user.getPassword(),hashedPassword))
                                {
                                        labelPasswordError.setText("Enter Correct Username/Password");
                                }
                                else {

                                        Data.connectedUser = user;
                                        Data.username=null;
                                        Data.currentUser=null;
                                        Data.task=null;



                                        changeScene("MainUser.fxml");


                                }



                        }
                }
        }


        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

        }
}




