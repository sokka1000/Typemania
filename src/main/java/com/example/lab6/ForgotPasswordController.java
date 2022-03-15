package com.example.lab6;

import com.example.lab6.Controller;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.domain.validators.UserValidator;
import com.example.lab6.socialnetwork.repository.database.PagingUserDbRepository;
import com.example.lab6.socialnetwork.service.UserTaskService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Objects;


public class ForgotPasswordController extends Controller {

    PagingUserDbRepository userDbRepository = new PagingUserDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres", "postgres",new UserValidator());
    UserTaskService userService = new UserTaskService(userDbRepository);

    @FXML
    private TextField usernameReset;

    @FXML
    private TextField passwordReset;

    @FXML
    private Label resetError;

    @FXML
    public void onResetPasswordButtonClicked(ActionEvent actionEvent) throws IOException {
        String textUsername = usernameReset.getText();
        String textPassword = passwordReset.getText();
        String hashedPassword = userService.doHashing(textPassword);
        User user = userService.existsUsername(textUsername);
        if(textUsername.equals("") || textPassword.equals(""))
            resetError.setText("Enter Correct Username/Password");
        else {
            if (Objects.equals(user, null))
                resetError.setText("Enter Correct Username");
            else {
                if (Objects.equals(user.getPassword(), hashedPassword))
                    resetError.setText("New password can not be the old password");
                else
                {
                    user.setPassword(hashedPassword);
                    userService.updateUser(user);
                    resetError.setText("Successful password reset");
                }
            }
        }
    }

    @FXML
    public void onBackToSignInButtonClicked(MouseEvent mouseEvent) throws IOException {
        changeScene("login.fxml");
    }

}
