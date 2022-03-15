package com.example.lab6;

import com.example.lab6.socialnetwork.domain.*;
import com.example.lab6.socialnetwork.domain.validators.FriendshipValidator;
import com.example.lab6.socialnetwork.domain.validators.MessageValidator;
import com.example.lab6.socialnetwork.domain.validators.UserValidator;
import com.example.lab6.socialnetwork.domain.validators.ValidationException;
import com.example.lab6.socialnetwork.repository.database.FriendshipDbRepository;
import com.example.lab6.socialnetwork.repository.database.MessageDbRepository;
import com.example.lab6.socialnetwork.repository.database.UserDbRepository;
import com.example.lab6.socialnetwork.service.FriendshipService;
import com.example.lab6.socialnetwork.service.MessageService;
import com.example.lab6.socialnetwork.service.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserController  extends  Controller implements Initializable {
    UserDbRepository userDbRepository = new UserDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres", "postgres",new UserValidator());
    UserService userService = new UserService(userDbRepository);
    FriendshipDbRepository friendshipDbRepository = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres", "postgres", new FriendshipValidator());
    FriendshipService friendshipService = new FriendshipService(friendshipDbRepository);
    MessageDbRepository messageDbRepository = new MessageDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres", "postgres",new MessageValidator());
    MessageService messageService = new MessageService(messageDbRepository);


    @FXML
    private Button btnAdd;

    @FXML
    private Button btnAdd2;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField fieldName;

    @FXML
    private Label userText;

    @FXML
    private AnchorPane showFriendsScene;

    @FXML
    private AnchorPane addFriendScene;

    @FXML
    private AnchorPane requestScene;

    ObservableList<UserDTO> modelUser = FXCollections.observableArrayList();
    ObservableList<RequestDTO> modelRequest = FXCollections.observableArrayList();

    List<String> modelUsers;

    @FXML
    TableColumn<UserDTO, String> tableColumnFirstName;
    @FXML
    TableColumn<UserDTO, String> tableColumnLastName;
    @FXML
    TableColumn<UserDTO, String> tableColumnUsername;

    @FXML
    TableColumn<UserDTO, String> tableColumnFirstName2;
    @FXML
    TableColumn<UserDTO, String> tableColumnLastName2;
    @FXML
    TableColumn<UserDTO, String> tableColumnUsername2;


    @FXML
    TableColumn<RequestDTO, String> tableColumnFrom;
    @FXML
    TableColumn<RequestDTO, String> tableColumnStatus;
    @FXML
    TableColumn<RequestDTO, String> tableColumnDate;


    @FXML
    TableView<UserDTO> tableViewFriends;

    @FXML
    TableView<UserDTO> tableViewUsers;

    @FXML
    TableView<RequestDTO> tableViewRequests;


    public void makeVisibleFalse()
    {
        addFriendScene.setVisible(false);
        requestScene.setVisible(false);
        showFriendsScene.setVisible(false);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Data.text.equals("")){
            userText.setText("user");
        }
        else
            userText.setText(Data.text);
    }

    @FXML
    public void onShowButtonClick(ActionEvent actionEvent) throws IOException {
        modelUser.removeAll();
        modelUser.setAll(getFriendsDTOList());
        initialize();

        makeVisibleFalse();
        showFriendsScene.setVisible(true);

    }

    @FXML
    public void initialize() {
        //show & delete
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("username"));
        tableViewFriends.setItems(modelUser);


        //add new friend
        fieldName.textProperty().addListener(o -> handleFilter());
        tableColumnFirstName2.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tableColumnLastName2.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));
        tableColumnUsername2.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("username"));
        tableViewUsers.setItems(modelUser);

        //accept & reject request
        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("from"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("status"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("date"));
        tableViewRequests.setItems(modelRequest);


    }


    private List<UserDTO> getFriendsDTOList() {

        Predicate<Friendship> byId = x -> x.getID1().equals(Data.user.getId());

        List<Friendship> friendships = friendshipService.getListFriendships();

        return friendships.stream()
                .filter(byId)
                .map(x -> new UserDTO(userService.findUser(x.getID2()).getFirstName(), userService.findUser(x.getID2()).getLastName(),userService.findUser(x.getID2()).getUsername()))
                .collect(Collectors.toList());

    }

    @FXML
    public void onDeleteButtonClick(ActionEvent actionEvent) throws IOException {
       if(Objects.equals(tableViewFriends.getSelectionModel().getSelectedItem(),null))
       {
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setTitle("Error at delete button");
           alert.setHeaderText("");
           alert.setContentText("Please select a user");
           alert.showAndWait();

       }
       else {

           User user2 = userService.existsUsername(tableViewFriends.getSelectionModel().getSelectedItem().getUsername());
           Tuple<Long, Long> tuple = new Tuple<>(Data.user.getId(), user2.getId());
           Tuple<Long, Long> tuple2 = new Tuple<>(user2.getId(), Data.user.getId());
           friendshipService.deleteFriendship(tuple);
           friendshipService.deleteFriendship(tuple2);


               messageService.deleteRequest(Data.user.getUsername(), user2.getUsername());



           tableViewFriends.getItems().removeAll(
                   tableViewFriends.getSelectionModel().getSelectedItems());
       }
    }

    @FXML
    public void onAddButtonClick(ActionEvent actionEvent) throws IOException {

        modelUser.removeAll();
        modelUser.setAll(getUsersDTOList());
        makeVisibleFalse();
        addFriendScene.setVisible(true);


        initialize();

    }

    @FXML
    public void onAddButtonClick2(ActionEvent actionEvent) throws IOException {
        if(Objects.equals(tableViewUsers.getSelectionModel().getSelectedItem(),null))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error at add button");
            alert.setHeaderText("");
            alert.setContentText("Please select a user");
            alert.showAndWait();

        }
        else {

            User user2 = userService.existsUsername(tableViewUsers.getSelectionModel().getSelectedItem().getUsername());
            Tuple<Long, Long> tuple = new Tuple<>(Data.user.getId(), user2.getId());
            Tuple<Long, Long> tuple2 = new Tuple<>(user2.getId(), Data.user.getId());
            try {
                if(!Objects.equals(Data.user.getUsername() ,user2.getUsername())) {
                    LocalDateTime dateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDateTime = dateTime.format(formatter);

                    messageService.sendRequest(Data.user.getUsername(), user2.getUsername(), formattedDateTime);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succes");
                    alert.setHeaderText("");
                    alert.setContentText("Request sent successfully");
                    alert.showAndWait();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("");
                    alert.setContentText("You can't send a request to yourself");
                    alert.showAndWait();
                }
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void handleFilter() {

        Predicate<UserDTO> p1 = n -> n.getFirstName().startsWith(fieldName.getText());

        List<UserDTO> userList = new ArrayList<>();
        userService.getAll().forEach(x->userList.add(new UserDTO(x.getFirstName(), x.getLastName(), x.getUsername())));

        modelUser.setAll(userList
                .stream()
                .filter(p1)
                .collect(Collectors.toList()));

    }

    private List<UserDTO> getUsersDTOList() {

        List<User> userList = new ArrayList<>();
        userService.getAll().forEach(x->userList.add(x));
       // System.out.println(userList);
        return userList.stream()

                .map(x -> new UserDTO(x.getFirstName(),x.getLastName(), x.getUsername()))
                .collect(Collectors.toList());

    }

    private List<RequestDTO> getRequestsDTOList() {
      //  System.out.println(messageService.requestDTOList(Data.user.getUsername()));
     return messageService.requestDTOList(Data.user.getUsername());
    }




    @FXML
    public void onFriendRequestsBtnClick(ActionEvent actionEvent) throws IOException {
        makeVisibleFalse();
        requestScene.setVisible(true);
        modelRequest.removeAll();
        modelRequest.setAll(getRequestsDTOList());
        initialize();

    }


    public void OnAcceptFriendRequestsBtnClick(ActionEvent actionEvent) throws IOException
    {
        if(Objects.equals(tableViewRequests.getSelectionModel().getSelectedItem(),null))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error at accept request button");
            alert.setHeaderText("");
            alert.setContentText("Please select a user");
            alert.showAndWait();

        }
        else {
            try {
                String usernameFrom= tableViewRequests.getSelectionModel().getSelectedItem().getFrom();

                messageService.acceptRequest(Data.user.getUsername(), usernameFrom);

                User user= Data.user;
                User user2=userService.existsUsername(usernameFrom);

                Friendship friendship = new Friendship(user2.getId(), user.getId());
                friendship.setId(new Tuple<>(user2.getId(), user.getId()));
                friendshipService.addFriendship(friendship, userService.all());

                Friendship friendship2 = new Friendship(user.getId(), user2.getId());
                friendship2.setId(new Tuple<>(user.getId(), user2.getId()));
                friendshipService.addFriendship(friendship2, userService.all());

                int index= tableViewRequests.getSelectionModel().getFocusedIndex();

                LocalDateTime dateTime=LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDateTime = dateTime.format(formatter);

                RequestDTO requestDTO=new RequestDTO(usernameFrom,"aproved",formattedDateTime);
                tableViewRequests.getItems().set(index,requestDTO);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succes");
                alert.setHeaderText("");
                alert.setContentText("Request accepted");
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            catch (ValidationException e)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }

           // tableViewFriends.getItems().removeAll(
            //        tableViewFriends.getSelectionModel().getSelectedItems());
        }



       // modelRequest.removeAll();
      //  modelRequest.setAll(getRequestsDTOList());
       // initialize();

    }



    @FXML
    public void closeApp(javafx.scene.input.MouseEvent actionEvent)  {
        Platform.exit();


    }

    public void OnRejectFriendRequestsBtnClick(ActionEvent actionEvent) throws IOException
    {


            if (Objects.equals(tableViewRequests.getSelectionModel().getSelectedItem(), null)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error at reject request button");
                alert.setHeaderText("");
                alert.setContentText("Please select a user");
                alert.showAndWait();

            } else {

                try{
                String usernameFrom = tableViewRequests.getSelectionModel().getSelectedItem().getFrom();

                messageService.rejectRequest(Data.user.getUsername(), usernameFrom);


                int index = tableViewRequests.getSelectionModel().getFocusedIndex();

                LocalDateTime dateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDateTime = dateTime.format(formatter);

                RequestDTO requestDTO = new RequestDTO(usernameFrom, "reject", formattedDateTime);
                tableViewRequests.getItems().set(index, requestDTO);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succes");
                    alert.setHeaderText("");
                    alert.setContentText("Request rejected");
                    alert.showAndWait();

                // tableViewFriends.getItems().removeAll(
                //        tableViewFriends.getSelectionModel().getSelectedItems());
            }
                catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }

            // modelRequest.removeAll();
            //  modelRequest.setAll(getRequestsDTOList());
            // initialize();

        }



    }



    @FXML
    public void OnLogoutBtnClick(ActionEvent actionEvent) throws IOException {

    changeScene("login.fxml");
    }



    public void OnCancelFriendRequestsBtnClick(ActionEvent actionEvent) throws IOException
    {
        if (Objects.equals(tableViewRequests.getSelectionModel().getSelectedItem(), null)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error at cancel request button");
            alert.setHeaderText("");
            alert.setContentText("Please select a user");
            alert.showAndWait();

        } else {

            try{
                String usernameFrom = tableViewRequests.getSelectionModel().getSelectedItem().getFrom();

                messageService.cancelRequest(Data.user.getUsername(), usernameFrom);


                int index = tableViewRequests.getSelectionModel().getFocusedIndex();

                LocalDateTime dateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDateTime = dateTime.format(formatter);

                RequestDTO requestDTO = new RequestDTO(usernameFrom, "pending", formattedDateTime);
                tableViewRequests.getItems().set(index, requestDTO);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succes");
                alert.setHeaderText("");
                alert.setContentText("Request canceled");
                alert.showAndWait();

                // tableViewFriends.getItems().removeAll(
                //        tableViewFriends.getSelectionModel().getSelectedItems());
            }
            catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }

            // modelRequest.removeAll();
            //  modelRequest.setAll(getRequestsDTOList());
            // initialize();

        }



    }



}
