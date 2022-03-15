package com.example.lab6;

import com.example.lab6.socialnetwork.domain.*;
import com.example.lab6.socialnetwork.domain.validators.EventValidator;
import com.example.lab6.socialnetwork.domain.validators.FriendshipValidator;
import com.example.lab6.socialnetwork.domain.validators.MessageTaskValidator;
import com.example.lab6.socialnetwork.domain.validators.UserValidator;
import com.example.lab6.socialnetwork.repository.database.PagingEventDbRepository;
import com.example.lab6.socialnetwork.repository.database.PagingFriendshipDbRepository;
import com.example.lab6.socialnetwork.repository.database.PagingMessageDbRepository;
import com.example.lab6.socialnetwork.repository.database.PagingUserDbRepository;
import com.example.lab6.socialnetwork.service.*;
import com.example.lab6.utils.CustomRecursiveAction;
import com.example.lab6.utils.SortByNames;
import com.example.lab6.utils.events.ChangeEventType;
import com.example.lab6.utils.events.MessageTaskChangeEvent;
import com.example.lab6.utils.events.TaskStatusEvent;
import com.example.lab6.utils.observer.Observable;
import com.example.lab6.utils.observer.Observer;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTextArray;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import org.w3c.dom.ls.LSOutput;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.Predicate;
import java.util.stream.Stream;



public class MainUserController extends Controller  implements Initializable, Observer<MessageTaskChangeEvent>  {



    private int numberOfPages;
    private int numberOfUsers;
    private int numberOfFriendships;

    @FXML
    private ImageView loadingImage;

    @FXML
    private TextField searchField;

    @FXML
    private VBox panelUsers;

    @FXML
    private VBox panelFriends;

    @FXML
    private Pagination paginationUsers;

    @FXML
    private VBox vBox1;

    @FXML
    private Label labelName1;
    @FXML
    private Label labelName2;
    @FXML
    private Label labelName3;
    @FXML
    private Label labelName4;
    @FXML
    private Label labelName5;



    @FXML
    private Label labelName6;
    @FXML
    private Label labelName7;
    @FXML
    private Label labelName8;
    @FXML
    private Label labelName9;




    @FXML
    TableView<UserDTO> tableViewUsers;

    ObservableList<UserDTO> modelUser = FXCollections.observableArrayList();


    @FXML
    private HBox hBoxName1;
    @FXML
    private HBox hBoxName2;
    @FXML
    private HBox hBoxName3;
    @FXML
    private HBox hBoxName4;
    @FXML
    private HBox hBoxName5;



    @FXML
    private HBox hBoxName6;
    @FXML
    private HBox hBoxName7;
    @FXML
    private HBox hBoxName8;
    @FXML
    private HBox hBoxName9;


    @FXML
    private Line lineName1;
    @FXML
    private Line lineName2;
    @FXML
    private Line lineName3;
    @FXML
    private Line lineName4;


    @FXML
    private Line lineName5;
    @FXML
    private Line lineName6;
    @FXML
    private Line lineName7;


    @FXML
    private Line lineNameConversation1;
    @FXML
    private Line lineNameConversation2;
    @FXML
    private Line lineNameConversation3;
    @FXML
    private Line lineNameConversation4;





    @FXML
    private Label isFriendLabel1;
    @FXML
    private Label isFriendLabel2;
    @FXML
    private Label isFriendLabel3;
    @FXML
    private Label isFriendLabel4;
    @FXML
    private Label isFriendLabel5;





    @FXML
    private Pane paneUserRequestMessages;
    @FXML
    private Pane paneAcceptDeny;
    @FXML
    private Pane paneAddFriend;
    @FXML
    private Pane paneCancelRequest;
    @FXML
    private Pane paneMessage;
    @FXML
    private Pane paneRemoveFriend;


    @FXML
    private AnchorPane anchor;

    @FXML
    private Pane errorMessageSearch;

    private Set<User> users;

    private Predicate<User> filtered;
    private Predicate<Friendship> filteredFriendship;
    @FXML
    private AnchorPane userProfileAnchor;

    @FXML
    private AnchorPane requestsAnchorPane;

    @FXML
    private AnchorPane requestsAnchoPaneMain;

    @FXML
    public AnchorPane messageAnchorPane;

    @FXML
    public AnchorPane eventAnchorPane;



    @FXML
    private Label labelNameUser;


    @FXML
    private Label labelNumberOfFriendsUser;
    @FXML
    private Label labelNumberOfCommonFriendsUser;

    private int numberOfFriendsUser;

    @FXML
    private Label labelNameProfile;

    @FXML
    private ImageView imageViewHomeWhite;
    @FXML
    private ImageView imageViewHomeGray;
    @FXML
    private ImageView imageViewUserWhite;
    @FXML
    private ImageView imageViewUserGray;
    @FXML
    private ImageView imageViewGroupWhite;
    @FXML
    private ImageView imageViewGroupGray;
    @FXML
    private ImageView imageViewMessengerWhite;
    @FXML
    private ImageView imageViewMessengerGray;

    @FXML
    private ImageView imageViewEventWhite;
    @FXML
    private ImageView imageViewEventGray;

    @FXML
    private ImageView imageViewLogoutWhite;
    @FXML
    private ImageView imageViewLogoutGray;

    @FXML
    private ImageView imageViewNotificationWhite;
    @FXML
    private ImageView imageViewNotificationGray;




    @FXML
    private ImageView imageUserConnectedProfile;
    @FXML
    private ImageView imageUserSearchedProfile;

    @FXML
    public ImageView imageIconChangeProfilePhoto;





    @FXML
    private ImageView imageUserSearch1;
    @FXML
    private ImageView imageUserSearch2;
    @FXML
    private ImageView imageUserSearch3;
    @FXML
    private ImageView imageUserSearch4;
    @FXML
    private ImageView imageUserSearch5;


    @FXML
    private ImageView imageUserSearch6;
    @FXML
    private ImageView imageUserSearch7;
    @FXML
    private ImageView imageUserSearch8;
    @FXML
    private ImageView imageUserSearch9;

    @FXML
    private Pane paneChangeProfilePhoto;


    @FXML
    private Label labelNotification;

    @FXML
    private DatePicker date1;

    @FXML
    private DatePicker date2;

    @FXML
    private TextField friendUsername;

    @FXML
    private Button activityButton;

    @FXML
    private Button friendMessagesButton;


    private List<HBox> hBoxUsersList =new ArrayList<>();
    private List<Label> labelUsersList =new ArrayList<>();
    private List<Line> lineUsersList =new ArrayList<>();
    private List<Label> isFriendLabelList=new ArrayList<>();
    private List<ImageView> imageViewUsersList=new ArrayList<>();


    private List<HBox> hBoxFriendshipsList =new ArrayList<>();
    private List<Label> labelFriendshipsList =new ArrayList<>();
    private List<Line> lineFriendshipsList =new ArrayList<>();
    private List<ImageView> imageViewFriendshipsList=new ArrayList<>();



    PagingUserDbRepository pagingUserDbRepository;
    UserTaskService userService;
    PagingFriendshipDbRepository pagingFriendshipDbRepository;
    FriendshipTaskService friendshipService;
    PagingMessageDbRepository pagingMessageDbRepository;
    MessageTaskService messageService;
    PagingEventDbRepository pagingEventDbRepository;
    EventTaskService eventService;





    private void startAnimation()
    {
        loadingImage.setVisible(true);
        RotateTransition rotateTransition=new RotateTransition();
        rotateTransition.setNode(loadingImage);
        rotateTransition.setDuration(Duration.millis(1000));
        rotateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setByAngle(-360);
        rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.play();

    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        pagingUserDbRepository = new PagingUserDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "postgres",new UserValidator());
        userService = new UserTaskService(pagingUserDbRepository);
        pagingFriendshipDbRepository = new PagingFriendshipDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "postgres", new FriendshipValidator());
        friendshipService = new FriendshipTaskService(pagingFriendshipDbRepository);
        pagingMessageDbRepository = new PagingMessageDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "postgres",new MessageTaskValidator());
        messageService=new MessageTaskService(pagingMessageDbRepository);
        messageService.addObserver(this);
        pagingEventDbRepository=new PagingEventDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "postgres",new EventValidator());
        eventService=new EventTaskService(pagingEventDbRepository);
        eventService.addObserver(this);


        hBoxUsersList.add(hBoxName1);
        hBoxUsersList.add(hBoxName2);
        hBoxUsersList.add(hBoxName3);
        hBoxUsersList.add(hBoxName4);
        hBoxUsersList.add(hBoxName5);

        labelUsersList.add(labelName1);
        labelUsersList.add(labelName2);
        labelUsersList.add(labelName3);
        labelUsersList.add(labelName4);
        labelUsersList.add(labelName5);

        lineUsersList.add(null);
        lineUsersList.add(lineName1);
        lineUsersList.add(lineName2);
        lineUsersList.add(lineName3);
        lineUsersList.add(lineName4);


        hBoxFriendshipsList.add(hBoxName6);
        hBoxFriendshipsList.add(hBoxName7);
        hBoxFriendshipsList.add(hBoxName8);
        hBoxFriendshipsList.add(hBoxName9);

        labelFriendshipsList.add(labelName6);
        labelFriendshipsList.add(labelName7);
        labelFriendshipsList.add(labelName8);
        labelFriendshipsList.add(labelName9);

        lineFriendshipsList.add(null);
        lineFriendshipsList.add(lineName5);
        lineFriendshipsList.add(lineName6);
        lineFriendshipsList.add(lineName7);

        isFriendLabelList.add(isFriendLabel1);
        isFriendLabelList.add(isFriendLabel2);
        isFriendLabelList.add(isFriendLabel3);
        isFriendLabelList.add(isFriendLabel4);
        isFriendLabelList.add(isFriendLabel5);

        imageViewUsersList.add(imageUserSearch1);
        imageViewUsersList.add(imageUserSearch2);
        imageViewUsersList.add(imageUserSearch3);
        imageViewUsersList.add(imageUserSearch4);
        imageViewUsersList.add(imageUserSearch5);

        imageViewFriendshipsList.add(imageUserSearch6);
        imageViewFriendshipsList.add(imageUserSearch7);
        imageViewFriendshipsList.add(imageUserSearch8);
        imageViewFriendshipsList.add(imageUserSearch9);

        int numberOfNotifications=0;
        try {
            numberOfNotifications=eventService.getAllEventsForGivenUsername(Data.connectedUser.getUsername()).size();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(numberOfNotifications>0)
        {
            labelNotification.setVisible(true);
            labelNotification.setText(String.valueOf(numberOfNotifications));
        }
        else
        {
            labelNotification.setVisible(false);
        }





        labelNameProfile.setText(Data.connectedUser.getFirstName()+" "+Data.connectedUser.getLastName());

        setImage(imageUserConnectedProfile,Data.connectedUser.getImageUrl());


startAnimation();
    }






    private void setImage(ImageView imageView,String imageURL) {
        if (Objects.equals(getClass().getResourceAsStream(imageURL), null))
        imageView.setImage(new Image(imageURL));
        else
            imageView.setImage(new Image(getClass().getResourceAsStream(imageURL)));


    }
    private void disablePaneSearch()
    {
        paginationUsers.setVisible(false);
        errorMessageSearch.setVisible(false);
    }



    private void disablePaneUserProfileInside()
    {
        paneAcceptDeny.setVisible(false);
        paneAddFriend.setVisible(false);
        paneCancelRequest.setVisible(false);
        paneRemoveFriend.setVisible(false);
        labelNumberOfCommonFriendsUser.setVisible(false);
        paneHello.setVisible(false);
        btnCommonFriends.setVisible(false);
        paginationFriends.setVisible(false);
        paneHello.setVisible(false);

    }

    private void disablePaneUserProfileOutside()
    {
        userProfileAnchor.setVisible(false);
    }
    private void disablePaneRequests()
    {
        requestsAnchoPaneMain.setVisible(false);
    }

    private void disablePaneMessenger()
    {
        messageAnchorPane.setVisible(false);
        messagesPane.setVisible(false);
    }
    private void disablePaneEvent()
    {
        eventAnchorPane.setVisible(false);
        paneEventDetails.setVisible(false);
        // TODO

    }
    private void disablePaneNotification()
    {
        scrollPaneNotification.setVisible(false);
    }

    private void disableAll()
    {
        disablePaneSearch();
        disablePaneUserProfileOutside();
        disablePaneRequests();
        disablePaneMessenger();
        disablePaneEvent();
        disablePaneNotification();


    }



    public void makeAllMainButtonGray()
    {
        imageViewHomeWhite.setVisible(false);
        imageViewGroupWhite.setVisible(false);
        imageViewLogoutWhite.setVisible(false);
        imageViewMessengerWhite.setVisible(false);
        imageViewUserWhite.setVisible(false);
        imageViewEventWhite.setVisible(false);
        imageViewNotificationWhite.setVisible(false);
        imageViewHomeGray.setVisible(true);
        imageViewGroupGray.setVisible(true);
        imageViewLogoutGray.setVisible(true);
        imageViewMessengerGray.setVisible(true);
        imageViewUserGray.setVisible(true);
        imageViewEventGray.setVisible(true);
        imageViewNotificationGray.setVisible(true);




    }



    @FXML
    public void onHomeButton(MouseEvent mouseEvent)
    {
        makeAllMainButtonGray();
        imageViewHomeGray.setVisible(false);
        imageViewHomeWhite.setVisible(true);
        disableAll();

    }


    @FXML
    public void onSearchButton(ActionEvent actionEvent)
    {
        makeAllMainButtonGray();

        disableAll();


        String textSearch = searchField.getText();
        String[] text=textSearch.split(" ");
        searchField.deselect();

        Predicate<User> byFirstName1= x->x.getFirstName().equalsIgnoreCase(text[0]);
        Predicate<User> byLastName1= x->x.getLastName().equalsIgnoreCase(text[0]);

        filtered = byFirstName1.or(byLastName1);

        if(text.length>1)
        {
            Predicate<User> byLastName2 = x -> x.getLastName().equalsIgnoreCase(text[1]);
            Predicate<User> byFirstName2 = x -> x.getFirstName().equalsIgnoreCase(text[1]);
            filtered = byFirstName1.or(byLastName1).or(byFirstName2).or(byLastName2);
        }

        Iterable<User> usersIterable= userService.filter(userService.getAll(),filtered);

        int size=0;
        for(User user : usersIterable) {
            size++;
        }

        /*
        List<User> users=new ArrayList<>();

        int size=0;
        for(User user : usersIterable) {
            size++;
            users.add(user);
        }

        Collections.sort(users,new SortByNames());
        */
        numberOfUsers=size;
        numberOfPages=(numberOfUsers+4)/5;



        if(numberOfUsers!=0)
        {
            paginationUsers.setVisible(true);
            paginationUsers.setLayoutX(98);
            paginationUsers.setLayoutY(100);

            userService.setPageSize(5);
            paginationUsers.setPageCount(numberOfPages);
            paginationUsers.setPageFactory((Integer pageIndex) -> createPage(pageIndex,usersIterable));

            anchor.setTopAnchor(paginationUsers, 15.0);
            anchor.setLeftAnchor(paginationUsers, 150.0);
            anchor.setBottomAnchor(paginationUsers, 0.0);
            anchor.setRightAnchor(paginationUsers, 150.0);

        }
        else
        {
            errorMessageSearch.setVisible(true);

        }


    }




    private VBox createPage(Integer pageIndex,Iterable<User> userIterable) {

        panelUsers.setVisible(true);

        Set<User> users=userService.getUsersOnPage(pageIndex,userIterable);

        int count=0;

        for(int i=0;i<5;i++)
        {
            hBoxUsersList.get(i).setVisible(false);
            labelUsersList.get(i).setVisible(false);

            if(!Objects.equals(null,lineUsersList.get(i)))
                lineUsersList.get(i).setVisible(false);
            isFriendLabelList.get(i).setVisible(false);

        }

            for(User user :users)
            {
                    labelUsersList.get(count).setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            Data.username=user.getUsername();
                            createUserProfilePage();



                        }
                    });
                imageViewUsersList.get(count).setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        Data.username=user.getUsername();
                        createUserProfilePage();



                    }
                });


                //change photo

                setImage(imageViewUsersList.get(count),user.getImageUrl());


                //change text
                labelUsersList.get(count).setVisible(true);
                labelUsersList.get(count).setText(user.getFirstName() + " " + user.getLastName());

                //change text
                isFriendLabelList.get(count).setVisible(true);
                if(!Objects.equals(null,friendshipService.findOne(new Tuple<>(Data.connectedUser.getId(),user.getId()))))
                    isFriendLabelList.get(count).setText("Friend");
                else
                    isFriendLabelList.get(count).setText("");


                    //show hbox
                    hBoxUsersList.get(count).setVisible(true);

                    //show line
                    if(!Objects.equals(null, lineUsersList.get(count)))
                        lineUsersList.get(count).setVisible(true);
                    count++;
            }
      //  }
        return panelUsers;
    }




    @FXML
    private Pagination paginationFriends;

    private List<User> friendsForSearchedUser;
    private int sizeFriendsForSearchedUser;

    private List<User> friendsCommonForSearchedUser;
    private int sizeFriendsCommonForSearchedUser;




    @FXML
    public Pane paneHello;


    @FXML
    public Button btnCommonFriends;



    @FXML
    private void createUserProfilePage()
    {
        Data.task="USER";
        makeAllMainButtonGray();
        imageViewUserGray.setVisible(false);
        imageViewUserWhite.setVisible(true);


        disableAll();
        disablePaneUserProfileInside();
        userProfileAnchor.setVisible(true);



        User userConnected= Data.connectedUser;
        User userSearched=userService.existsUsername(Data.username);
        Data.currentUser=userSearched;


        paneChangeProfilePhoto.setVisible(false);
        date1.setVisible(false);
        date2.setVisible(false);
        friendUsername.setVisible(false);
        activityButton.setVisible(false);
        friendMessagesButton.setVisible(false);


        //set name
        labelNameUser.setText(userSearched.getFirstName()+" "+userSearched.getLastName());
        //set photo

        setImage(imageUserSearchedProfile,userSearched.getImageUrl());




        //calculate number of friends and put this in label
        Predicate<Friendship> byID1= x->x.getID1().equals(userSearched.getId());
        Predicate<Friendship> byID2= x->x.getID2().equals(userSearched.getId());
        filteredFriendship = byID1.or(byID2);
        Iterable<Friendship>  friendshipsForSearchedUser= friendshipService.filter(friendshipService.getAll(),filteredFriendship);


        friendsForSearchedUser=userService.getUserFriends(userSearched,friendshipsForSearchedUser);


        //sorted by names
        Collections.sort(friendsForSearchedUser, new SortByNames());


        sizeFriendsForSearchedUser=friendsForSearchedUser.size();
        if(sizeFriendsForSearchedUser==1)
        labelNumberOfFriendsUser.setText(sizeFriendsForSearchedUser+" friend");
        else
            labelNumberOfFriendsUser.setText(sizeFriendsForSearchedUser+" friends");


        if(Objects.equals(Data.connectedUser,Data.currentUser))
        {
            //SAME USER
            paneHello.setVisible(true);
            imageUserSearchedProfile.setCursor(Cursor.DEFAULT);
            paneChangeProfilePhoto.setVisible(true);

            date1.setVisible(true);
            date2.setVisible(true);
            friendUsername.setVisible(true);
            activityButton.setVisible(true);
            friendMessagesButton.setVisible(true);



        }
        else {
            //DIFFERENT USER

            btnCommonFriends.setVisible(true);

            friendsCommonForSearchedUser = friendshipService.getUserFriends(userConnected.getId(),friendsForSearchedUser);

           /* friendsCommonForSearchedUser = new ArrayList<>();

            for(User user : friendsForSearchedUser) {
                if(!Objects.equals(friendshipService.findOne(new Tuple<>(userConnected.getId(),user.getId())),null)) {
                    friendsCommonForSearchedUser.add(user);

                }
            }

            */

            sizeFriendsCommonForSearchedUser=friendsCommonForSearchedUser.size();

            labelNumberOfCommonFriendsUser.setVisible(true);
            if(sizeFriendsCommonForSearchedUser==1)
                 labelNumberOfCommonFriendsUser.setText(sizeFriendsCommonForSearchedUser+" common friend");
            else
                labelNumberOfCommonFriendsUser.setText(sizeFriendsCommonForSearchedUser+" common friends");

            //if user Connected is friend with user Searched
            if (!Objects.equals(friendshipService.findOne(new Tuple<>(userConnected.getId(), userSearched.getId())), null)) {
               // paneMessage.setVisible(true);
                paneRemoveFriend.setVisible(true);

            } else {
                String statusCtoS = messageService.findStatus(userConnected.getUsername(), userSearched.getUsername());
                String statusStoC = messageService.findStatus(userSearched.getUsername(), userConnected.getUsername());

                //userSearched sent a request to userConnected
                if (Objects.equals("pending", statusStoC)) {
                    paneAcceptDeny.setVisible(true);

                } else if (!Objects.equals("rejected", statusStoC)) {
                    //request doesn't exist
                    if (Objects.equals("pending", statusCtoS)) {
                        paneCancelRequest.setVisible(true);

                    } else if (Objects.equals(null, statusCtoS) || Objects.equals("rejected", statusCtoS)) {
                        paneAddFriend.setVisible(true);

                    }
                }

            }
        }




    }


    @FXML
    private void onPaneChangeProfilePhotoMouseEntered()
    {
        imageIconChangeProfilePhoto.setVisible(true);

    }
    @FXML
    private void onPaneChangeProfilePhotoMouseExited()
    {
        imageIconChangeProfilePhoto.setVisible(false);
    }
    @FXML
    private  void onImageIconChangeProfilePhotoClicked()
    {
        //https://generated.photos/faces

        FileChooser fileChooser=new FileChooser();

        File f=fileChooser.showOpenDialog(null);

        if(!Objects.equals(f,null))
        {
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.{png,jpg}");


            Path filename = f.toPath();
            if (matcher.matches(filename)) {


                String string=f.toURI().toString();
                Data.connectedUser.setImageUrl(filename.toString());

                userService.updateUser(Data.connectedUser);

                imageUserSearchedProfile.setImage(new Image(f.toURI().toString()));
                imageUserConnectedProfile.setImage(new Image(f.toURI().toString()));

            }



        }



    }

    @FXML
    public void onActivityButtonClicked(ActionEvent actionEvent) throws FileNotFoundException, DocumentException {
        LocalDate dateActivity1 = date1.getValue();

        LocalDate dateActivity2 = date2.getValue();


        if(Objects.equals(dateActivity1, null) || Objects.equals(dateActivity2, null)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Select a date");
            alert.showAndWait();
        }
        if(dateActivity2.isBefore(dateActivity1)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Invalid time interval");
            alert.showAndWait();
        }
        else {
            Map<String, LocalDate> friends = friendshipService.friendsTimePeriod(Data.connectedUser.getUsername(), dateActivity1, dateActivity2);
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("New friends and messages.pdf"));
            document.open();

            Paragraph paragraph = new Paragraph("New friends");

            if (friends.isEmpty())
                paragraph.add("\nYou haven't made any new friends in that time interval");
            else {
                PdfPTable friendsTable = new PdfPTable(4);
                Stream.of("First name", "Last name", "Username", "Date").forEach(friendsTable::addCell);
                for (String usernameFriend : friends.keySet()) {
                    User user = userService.existsUsername(usernameFriend);
                    friendsTable.addCell(user.getFirstName());
                    friendsTable.addCell(user.getLastName());
                    friendsTable.addCell(usernameFriend);
                    friendsTable.addCell(friends.get(usernameFriend).toString());
                }
                paragraph.add(friendsTable);
            }

            paragraph.add("\nReceived messages");

            List<MessageDTO> messageDTOList = messageService.getMessagesInTimeInterval(Data.connectedUser.getUsername(), dateActivity1, dateActivity2);

            if (messageDTOList.isEmpty())
                paragraph.add("\nYou didn't receive any messages in that time interval.");
            else {
                PdfPTable messagesTable = new PdfPTable(5);
                Stream.of("First name", "Last name", "Username", "Message", "Date").forEach(messagesTable::addCell);
                for (MessageDTO messageDTO: messageDTOList) {
                    User user = userService.existsUsername(messageDTO.getUsername());
                    messagesTable.addCell(user.getFirstName());
                    messagesTable.addCell(user.getLastName());
                    messagesTable.addCell(user.getUsername());
                    messagesTable.addCell(messageDTO.getMessage());
                    messagesTable.addCell(messageDTO.getDateTime());
                }
                paragraph.add(messagesTable);
            }
            document.add(paragraph);
            document.close();
        }

        date1.getEditor().clear();
        date2.getEditor().clear();


    }

    @FXML
    public void onMessagesReceivedButtonClicked(ActionEvent actionEvent) throws FileNotFoundException, DocumentException {
        LocalDate dateActivity1 = date1.getValue();

        LocalDate dateActivity2 = date2.getValue();

        String friend = friendUsername.getText();

        if(Objects.equals(dateActivity1, null) || Objects.equals(dateActivity2, null)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Select a date");
            alert.showAndWait();
        }
        if(dateActivity2.isBefore(dateActivity1)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Invalid time interval");
            alert.showAndWait();
        }
        else {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Messages received from searched user in the time period.pdf"));
            document.open();

            Paragraph paragraph = new Paragraph("Messages received from searched user in the time period:");

            List<MessageDTO> messageDTOList = messageService.getMessagesFromFriendInTimeInterval(Data.connectedUser.getUsername(), friend, dateActivity1, dateActivity2);

            if (messageDTOList.isEmpty())
                paragraph.add("\nYou didn't receive any messages from that friend in that time interval.");
            else {
                PdfPTable messagesTable = new PdfPTable(5);
                Stream.of("First name", "Last name", "Username", "Message", "Date").forEach(messagesTable::addCell);
                for (MessageDTO messageDTO: messageDTOList) {
                    User user = userService.existsUsername(messageDTO.getUsername());
                    messagesTable.addCell(user.getFirstName());
                    messagesTable.addCell(user.getLastName());
                    messagesTable.addCell(user.getUsername());
                    messagesTable.addCell(messageDTO.getMessage());
                    messagesTable.addCell(messageDTO.getDateTime());
                }
                paragraph.add(messagesTable);
            }
            document.add(paragraph);
            document.close();
        }
        date1.getEditor().clear();
        date2.getEditor().clear();
        friendUsername.clear();
    }


    @FXML
    private void showFriends(ActionEvent actionEvent)
    {
        paginationFriends.setVisible(false);

        numberOfFriendships=sizeFriendsForSearchedUser;
        numberOfPages=(numberOfFriendships+3)/4;

        if(numberOfFriendships!=0)
        {
            paginationFriends.setVisible(true);
            paginationFriends.setLayoutX(98);
            paginationFriends.setLayoutY(100);

            userService.setPageSize(4);
            paginationFriends.setPageCount(numberOfPages);

            //create list of users
            paginationFriends.setPageFactory((Integer pageIndex) -> createPageFriends(pageIndex,friendsForSearchedUser));



            userProfileAnchor.setTopAnchor(paginationFriends, 120.0);
            userProfileAnchor.setLeftAnchor(paginationFriends, 300.0);
            userProfileAnchor.setBottomAnchor(paginationFriends, 0.0);
            userProfileAnchor.setRightAnchor(paginationFriends, 100.0);
        }
    }

    @FXML
    private void showCommonFriends(ActionEvent actionEvent)
    {

        paginationFriends.setVisible(false);
        List<Friendship> friendshipCommonList=new ArrayList<>();

        numberOfFriendships=sizeFriendsCommonForSearchedUser;
        numberOfPages=(numberOfFriendships+3)/4;

        if(numberOfFriendships!=0)
        {
            paginationFriends.setVisible(true);
            paginationFriends.setLayoutX(98);
            paginationFriends.setLayoutY(100);

            userService.setPageSize(4);
            paginationFriends.setPageCount(numberOfPages);
            paginationFriends.setPageFactory((Integer pageIndex) -> createPageFriends(pageIndex,friendsCommonForSearchedUser));

            userProfileAnchor.setTopAnchor(paginationFriends, 120.0);
            userProfileAnchor.setLeftAnchor(paginationFriends, 300.0);
            userProfileAnchor.setBottomAnchor(paginationFriends, 0.0);
            userProfileAnchor.setRightAnchor(paginationFriends, 100.0);
        }
    }




    private VBox createPageFriends(Integer pageIndex,Iterable<User> friendsIterable) {



        panelFriends.setVisible(true);
        Set<User> usersSet=userService.getUsersOnPage(pageIndex,friendsIterable);
        List<User> users=new ArrayList<>();

        for(User user : usersSet)
        {
            users.add(user);
        }
        Collections.sort(users,new SortByNames());

        int count=0;

        for(HBox hBox: hBoxFriendshipsList)
        {
            hBox.setVisible(false);
        }
        for(Label label: labelFriendshipsList)
        {
            label.setVisible(false);
        }

        for(Line line: lineFriendshipsList)
        {
            if(!Objects.equals(null,line))
                line.setVisible(false);
        }

        for(User user :users)
            {
                labelFriendshipsList.get(count).setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        Data.username=user.getUsername();
                        createUserProfilePage();

                    }
                });

                //change photo
                // imageViewFriendshipsList.get(count).setImage(new Image(getClass().getResourceAsStream(user.getImageUrl())));
               setImage(imageViewFriendshipsList.get(count),user.getImageUrl());


                //change text
                labelFriendshipsList.get(count).setVisible(true);
                labelFriendshipsList.get(count).setText(user.getFirstName() + " " + user.getLastName());

                //show hbox
                hBoxFriendshipsList.get(count).setVisible(true);

                //show line
                if(!Objects.equals(null, lineFriendshipsList.get(count)))
                    lineFriendshipsList.get(count).setVisible(true);
                count++;
            }

        return panelFriends;

    }







    @FXML
    public void onAddButton(ActionEvent actionEvent)
    {
        paneAddFriend.setVisible(false);
        paneCancelRequest.setVisible(true);
        LocalDateTime dateTime=LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        messageService.sendRequest(Data.connectedUser.getUsername(),Data.currentUser.getUsername(),formattedDateTime);

    }
    @FXML
    public void onCancelRequestButton(ActionEvent actionEvent)
    {
        paneAddFriend.setVisible(true);
        paneCancelRequest.setVisible(false);
        messageService.cancelRequest(Data.connectedUser.getUsername(),Data.currentUser.getUsername());

    }
    @FXML
    public void onAcceptButton(ActionEvent actionEvent)
    {
        paneAcceptDeny.setVisible(false);
        messageService.acceptRequest(Data.connectedUser.getUsername(),Data.currentUser.getUsername());
        Friendship friendship = new Friendship(Data.connectedUser.getId(), Data.currentUser.getId());
        friendship.setId(new Tuple<>(Data.connectedUser.getId(), Data.currentUser.getId()));
        friendshipService.addFriendship(friendship);



        //add user to list of users
        if(!Objects.equals(friendsForSearchedUser,null))
        {
            sizeFriendsForSearchedUser++;
            if(sizeFriendsForSearchedUser==1)
                labelNumberOfFriendsUser.setText(sizeFriendsForSearchedUser+" friend");
            else
                labelNumberOfFriendsUser.setText(sizeFriendsForSearchedUser+" friends");

            userService.setPageSize(4);
            paginationFriends.setPageCount(numberOfPages);
            friendsForSearchedUser.add(Data.connectedUser);
            paginationFriends.setPageFactory((Integer pageIndex) -> createPageFriends(pageIndex,friendsForSearchedUser));

        }




    }
    @FXML
    public void onDenyButton(ActionEvent actionEvent)
    {
        paneAcceptDeny.setVisible(false);
        paneAddFriend.setVisible(true);
        messageService.rejectRequest(Data.connectedUser.getUsername(),Data.currentUser.getUsername());

    }

    @FXML
    public void onRemoveButton(ActionEvent actionEvent)
    {

        paneRemoveFriend.setVisible(false);
        paneAddFriend.setVisible(true);

        friendshipService.deleteFriendship(new Tuple<>(Data.connectedUser.getId(), Data.currentUser.getId()));
        messageService.deleteRequest(Data.connectedUser.getUsername(),Data.currentUser.getUsername());


        //remove user from list of users
        if(!Objects.equals(friendsForSearchedUser,null)) {


            sizeFriendsForSearchedUser--;
            if (sizeFriendsForSearchedUser == 1)
                labelNumberOfFriendsUser.setText(sizeFriendsForSearchedUser + " friend");
            else
                labelNumberOfFriendsUser.setText(sizeFriendsForSearchedUser + " friends");

            userService.setPageSize(4);
            paginationFriends.setPageCount(numberOfPages);
            //create list of users
            friendsForSearchedUser.remove(Data.connectedUser);
            paginationFriends.setPageFactory((Integer pageIndex) -> createPageFriends(pageIndex, friendsForSearchedUser));
        }


    }
    @FXML
    public void onRefreshButton(MouseEvent mouseEvent)
    {
        String task=Data.task;
            if(Objects.equals(task,null))
            {
               // System.out.println("nothing");
            }
            else if(Objects.equals(task,"USER"))
            {
                createUserProfilePage();
            }
            else if(Objects.equals(task,"GROUP"))
            {
                createGroupPane();
            }
            else if(Objects.equals(task,"MESSENGER"))
            {
                createMessengerPane();
            }



    }
    @FXML
    public void onProfileButton(MouseEvent mouseEvent)
    {
        Data.username=Data.connectedUser.getUsername();
        createUserProfilePage();
    }


    @FXML
    Pagination paginationConversations;
    List<Conversation> conversations;


    int numberOfConversations;

    public void createMessengerPane()
    {

        Data.task="MESSENGER";
        disableAll();
        messageAnchorPane.setVisible(true);

        makeAllMainButtonGray();
        imageViewMessengerGray.setVisible(false);
        imageViewMessengerWhite.setVisible(true);


        conversations=messageService.getConversations(Data.connectedUser.getUsername());



        numberOfConversations=conversations.size();
        numberOfPages=(numberOfConversations+4)/5;

        if(numberOfConversations!=0)
        {
            paginationConversations.setVisible(true);


            messageService.setPageSize(5);
            paginationConversations.setPageCount(numberOfPages);
            paginationConversations.setPageFactory((Integer pageIndex) -> createPageConversations(pageIndex,conversations));

            anchor.setTopAnchor(paginationConversations, 100.0);
            anchor.setLeftAnchor(paginationConversations, 100.0);
            anchor.setBottomAnchor(paginationConversations, 50.0);
            anchor.setRightAnchor(paginationConversations, 400.0);

        }
        else
        {
            paginationConversations.setVisible(false);
            //errorMessageSearch.setVisible(true);

        }


        // TODO

    }

    @FXML
    public void onMainMessageButton(MouseEvent mouseEvent)
    {
            createMessengerPane();
    }

    private String convertListToString(List<String> usernames)
    {
        String usernamesString="";

        for(String username : usernames)
            usernamesString+=username+",";
        return usernamesString;

    }

    Conversation currentConversation;
    Label currentLabelLastMessageConversation;
    HBox currentConversationHBox;



    public HBox getConversationHbox(Conversation conversation)
    {

        HBox mainHBox= new HBox();
        mainHBox.setStyle("-fx-background-color: #46474A;");



        ImageView imageView=new ImageView();

        if(conversation.getUsernames().size()==2)
        {
            //profile photo

            User user1=userService.existsUsername( conversation.getUsernames().get(0)) ;
            User user2=userService.existsUsername( conversation.getUsernames().get(1)) ;


            if(Objects.equals(user1,Data.connectedUser))
            setImage(imageView,user2.getImageUrl());
            else
                setImage(imageView,user1.getImageUrl());

        }
        else
        {
            //default photo
            setImage(imageView,"C:\\FACULTATE\\MAP\\lab6_proiect\\map224-catalindiana-lab6\\src\\main\\resources\\com\\example\\lab6\\data\\profile_pictures\\default_user_profile.png");

        }



        imageView.setFitHeight(35.0);
        imageView.setFitWidth(35.0);

        imageView.setStyle("-fx-padding: 10 10 10 10");

        StackPane stackPaneforImageView = new StackPane();
        stackPaneforImageView.getChildren().add(imageView);
        stackPaneforImageView.setPadding(new Insets(10,10,10,10));
        stackPaneforImageView.setStyle("-fx-background-color: transparent;");



        VBox labelNameMessageDataVBox = new VBox();

        List<String> usernamesWithoutUsername=conversation.getUsernamesWithoutUsername(Data.connectedUser.getUsername());

        String usernames= convertListToString(usernamesWithoutUsername);


        Label labelName=new Label(usernames);
        labelName.setPadding(new Insets(5,0,0,15));
        labelName.setStyle("-fx-font-size: 14px;-fx-text-fill: white; -fx-font-weight: bold;");


        VBox labelMessageDataVBox=new VBox();


        String sender="";
        if(Objects.equals(Data.connectedUser.getUsername(),conversation.getMessageDTO().getUsername()))
            sender="You: ";
        else if(!Objects.equals(conversation.getMessageDTO().getUsername(),""))
            sender=conversation.getMessageDTO().getUsername()+": ";

        Label labelMessage=new Label(sender + conversation.getMessageDTO().getMessage());
        Label labelDate = new Label( conversation.getMessageDTO().getDateTime());
        labelMessage.setPadding(new Insets(5,0,0,15));
        labelMessage.setStyle("-fx-font-size: 13px;-fx-text-fill: #b0b0b0;");

        labelMessage.setPadding(new Insets(5,0,0,15));
        labelDate.setStyle("-fx-font-size: 10px;-fx-text-fill: #b0b0b0;");

        labelMessageDataVBox.getChildren().setAll(labelMessage,labelDate);
        labelNameMessageDataVBox.getChildren().setAll(labelName,labelMessageDataVBox);
        mainHBox.getChildren().setAll(stackPaneforImageView,labelNameMessageDataVBox);



        mainHBox.setCursor(Cursor.HAND);
        mainHBox.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                sendMessageToUsers = new ArrayList<>();
                currentConversation=conversation;

                for(String username:conversation.getUsernames())
                {
                    if(!Objects.equals(username, Data.connectedUser.getUsername()))
                        sendMessageToUsers.add(username);
                }
                currentConversationHBox=mainHBox;
                currentLabelLastMessageConversation=labelMessage;
                createPaneMessages();

                paneNewConversation.setVisible(false);

            }
        });




        return mainHBox;
    }


    private double getCurrentWidthOfText(String string)
        {
            Text text = new Text(string);
            text.applyCss();
            double width = text.getLayoutBounds().getWidth();
            return  width;
        }


    public HBox getMessageHBox(MessageDTO currentMessageDTO)
    {

        HBox mainMainHbox=new HBox();

        HBox mainHBox= new HBox();
        User user=userService.existsUsername(currentMessageDTO.getUsername());
        VBox labelNameMessageDataVBox = new VBox();

        Label labelName=new Label(user.getUsername());
        labelName.setPadding(new Insets(5,0,0,15));
        labelName.setStyle("-fx-font-size: 10px;-fx-text-fill: white; -fx-font-weight: bold;");


        HBox labelMessageDataHBox=new HBox();


        Label labelMessage=new Label(currentMessageDTO.getMessage());
        labelMessage.setPadding(new Insets(5,20,10,15));
        labelMessage.setStyle("-fx-font-size: 14px;-fx-text-fill: white; -fx-font-weight: bold;");
        labelMessage.setWrapText(true);
        labelMessage.setMaxWidth(230.0);

        mainHBox.setMaxWidth(getCurrentWidthOfText(labelMessage.getText()) + 130.0);


        mainMainHbox.setMargin(mainHBox,new Insets(5, 10, 5, 10));

        labelMessageDataHBox.getChildren().setAll(labelMessage);

        if(Objects.equals(currentMessageDTO.getUsername(),Data.connectedUser.getUsername()))
        {
            //connected user
            mainHBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

            mainMainHbox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            mainHBox.getChildren().setAll(labelNameMessageDataVBox);
            mainHBox.setStyle("-fx-background-color: #6D1FF2;   -fx-background-radius: 25px; ");
            labelNameMessageDataVBox.getChildren().setAll(labelMessage);


            mainHBox.setCursor(Cursor.HAND);
            mainHBox.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {

                    boolean result = ConfirmBox.display("Warning","Are you sure that you want to delete this message?");
                    if(result==true)
                    {
                     //delete message


                        Message newMessage= new Message(null,null,null,null,null);

                        newMessage.setId(currentMessageDTO.getId());

                        messageService.deleteMessageTask(newMessage,currentConversation);
                        messagesVBox.getChildren().remove(mainMainHbox);

                       // messageService.updateConversationWithLastMessage(currentConversation);





                    }

                }
            });


        }
        else
        {
            mainHBox.setStyle("-fx-background-color: #46474A;   -fx-background-radius: 25px; ");
            ImageView imageView=new ImageView();


            setImage(imageView,user.getImageUrl());

            imageView.setFitHeight(25.0);
            imageView.setFitWidth(25.0);

            imageView.setStyle("-fx-padding: 10 10 10 10");

            StackPane stackPaneforImageView = new StackPane();
            stackPaneforImageView.getChildren().add(imageView);
            stackPaneforImageView.setPadding(new Insets(10,10,10,10));
            stackPaneforImageView.setStyle("-fx-background-color: transparent;");

            mainHBox.getChildren().setAll(stackPaneforImageView,labelNameMessageDataVBox);
            labelNameMessageDataVBox.getChildren().setAll(labelName,labelMessage);


        }


        mainMainHbox.getChildren().setAll(mainHBox);
        return mainMainHbox;
    }



    @FXML
    ScrollPane messagesScrollPane;
    @FXML
    Pane messagesPane;
    VBox messagesVBox;
    @FXML
    Label conversationName;



    private void createPaneMessages()
    {
             messagesPane.setVisible(true);

             conversationName.setText(convertListToString(currentConversation.getUsernames()));
            messagesVBox=new VBox();
            messagesVBox.setStyle("-fx-background-color: white");
            messagesVBox.setPrefWidth(315);
            messagesVBox.setPrefHeight(357);
            List<MessageDTO> messageDTOList=messageService.getMessages(currentConversation.getId());

            for(MessageDTO messageDTO : messageDTOList )
            {
                HBox hbox= getMessageHBox(messageDTO);

                messagesVBox.getChildren().add(hbox);


            }
        messagesScrollPane.setContent(messagesVBox);
        messagesScrollPane.vvalueProperty().bind(messagesVBox.heightProperty());
        messagesScrollPane.setStyle("-fx-background-color: white");



    }






    private VBox createPageConversations(Integer pageIndex,Iterable<Conversation> conversationsIterable) {

        VBox panelConversation=new VBox();

        panelConversation.setVisible(true);


        List<Conversation> conversationsList=messageService.getConversationsOnPage(pageIndex,conversationsIterable);

        Collections.sort(conversationsList, new Comparator<Conversation>() {
            @Override
            public int compare(Conversation o1, Conversation o2) {
                return o2.getMessageDTO().getDateTime().compareTo(o1.getMessageDTO().getDateTime());
            }
        });


        // TODO
        //  Collections.sort(conversations,new SortByData());



        for(Conversation conversation :conversationsList)
        {
           HBox hbox= getConversationHbox(conversation);
           panelConversation.getChildren().add(hbox);

        }

        return panelConversation;

    }

    @FXML
    Button buttonSendMessage;
    @FXML
    TextField textFieldSendMessage;

    @Override
    public void update(MessageTaskChangeEvent messageTaskChangeEvent) {



        if(Objects.equals(messageTaskChangeEvent.getType(), ChangeEventType.ADD) || Objects.equals(messageTaskChangeEvent.getType(),ChangeEventType.UPDATE) || Objects.equals(messageTaskChangeEvent.getType(),ChangeEventType.DELETE)) {

            conversations = messageService.getConversations(Data.connectedUser.getUsername());

            numberOfConversations = conversations.size();
            numberOfPages = (numberOfConversations + 4) / 5;
            paginationConversations.setPageCount(numberOfPages);

            paginationConversations.setVisible(false);
            paginationConversations.setPageFactory((Integer pageIndex) -> createPageConversations(pageIndex, conversations));
            paginationConversations.setVisible(true);
        }
        if(Objects.equals(messageTaskChangeEvent.getType(),ChangeEventType.SUBSCRIBE) || Objects.equals(messageTaskChangeEvent.getType(),ChangeEventType.UNSUBSCRIBE) )
        {
            events=eventService.getAll();

            numberOfEvents=events.size();
            numberOfPages= (numberOfEvents+4)/5;
           int currentPage=  paginationEvents.getCurrentPageIndex();

            paginationEvents.setPageCount(numberOfPages);
            paginationEvents.setVisible(false);
            paginationEvents.setPageFactory((Integer pageIndex) -> createPageEvents(pageIndex, events));
            paginationEvents.setVisible(true);

            paginationEvents.setCurrentPageIndex(currentPage);

        }

    }


    @FXML
    public void onSendMessageButton(ActionEvent ActionEvent) throws IOException {

        String textSendMessage = textFieldSendMessage.getText();
        textFieldSendMessage.setText("");
        textFieldSendMessage.deselect();

        List<User> sendTo = new ArrayList<>();

        for(String username: sendMessageToUsers){
            if(!Objects.equals(username, Data.connectedUser.getUsername())) {
                User user = userService.existsUsername(username);
                sendTo.add(user);
            }
        }

        if(!Objects.equals(textSendMessage,"")) {
            Message newMessage = new Message(Data.connectedUser, sendTo, textSendMessage, LocalDateTime.now(), null);
            newMessage.setIdConversation(currentConversation.getId());



            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = LocalDateTime.now().format(formatter);

            MessageDTO messageDTOsent = new MessageDTO(Data.connectedUser.getUsername(), formattedDateTime, textSendMessage);
            currentConversation.setMessageDTO(messageDTOsent);

            //update in db

            messageService.updateConversation(currentConversation);
            messageService.addMessageTask(newMessage);
            messageDTOsent.setId(newMessage.getId());

            //set visual changes

            if(!Objects.equals(null,currentLabelLastMessageConversation) && !Objects.equals(currentConversation.getMessageDTO().getDateTime(),"" ))
            currentLabelLastMessageConversation.setText(textSendMessage);


            HBox hBoxMessageDTOsent = getMessageHBox(messageDTOsent);
            //messagesVBox.setMargin(hBoxMessageDTOsent,new Insets(5, 0, 5, 0));
            messagesVBox.getChildren().add(hBoxMessageDTOsent);


        }

    }

    @FXML
    TableColumn tableColumnName;

    @FXML
    TableColumn tableColumnSelect;


    private void newConversationButton()
    {
        messagesPane.setVisible(false);
        modelUser.removeAll(modelUser);

        paneNewConversation.setVisible(true);

        Iterable<User> users= userService.getAll();

        for(User user : users)
        {
            if(!Objects.equals(user,Data.connectedUser))
            modelUser.add(new UserDTO(user.getFirstName(),user.getLastName(),user.getUsername()));

        }
        Collections.sort(modelUser, new Comparator<UserDTO>() {
            @Override
            public int compare(UserDTO o1, UserDTO o2) {
                return o1.getUsername().compareTo(o2.getUsername());
            }
        });


        tableColumnName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("username"));
        tableColumnSelect.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("remark"));
        tableViewUsers.setItems(modelUser);


    }
        @FXML
    private void onNewConversationButton(MouseEvent mouseEvent) throws IOException
    {

      newConversationButton();
    }





    private int generateIDConversation() {
        Iterable<Conversation> conversations = messageService.findAllConversations();

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

List<String> sendMessageToUsers = new ArrayList<>();

    @FXML
    private void onCreateNewConversationButton(MouseEvent mouseEvent)
    {
            sendMessageToUsers=null;
            ObservableList<UserDTO> observable=tableViewUsers.getItems();


            List<String> usernames=new ArrayList<>();

            for(UserDTO userDTO : observable)
            {
                if(userDTO.getRemark().isSelected())
                {
                    usernames.add(userDTO.getUsername());
                }


            }

            sendMessageToUsers = usernames;
            usernames.add(Data.connectedUser.getUsername());


           Conversation newConversation= new Conversation(usernames);

        //   Iterable<Conversation> conversations=messageService.getAllConversations();

           Collections.sort(newConversation.getUsernames());

           boolean ok=false;
           for(Conversation conversation : conversations)
           {
               Collections.sort(conversation.getUsernames());



                if(conversation.getUsernames().equals(newConversation.getUsernames()))
                {
                    currentConversation=conversation;
                    ok=true;

                    createPaneMessages();
                    paneNewConversation.setVisible(false);

                }

           }
           if(ok==false)
           {

               newConversation.setId(generateIDConversation());
               newConversation.setMessageDTO(new MessageDTO("","",""));
               messageService.addConversation(newConversation);

               conversations.add(newConversation);

                currentConversation=newConversation;


               createPaneMessages();
               paneNewConversation.setVisible(false);

           }

    }


    @FXML
    private  void onProfileMessengeButton(ActionEvent event)
    {
        createMessengerPane();


        List<String> usernames=new ArrayList<>();
        usernames.add(Data.currentUser.getUsername());
        if(!Objects.equals(Data.currentUser.getUsername(),Data.connectedUser.getUsername()))
            usernames.add(Data.connectedUser.getUsername());
        

        Conversation newConversation= new Conversation(usernames);

        //   Iterable<Conversation> conversations=messageService.getAllConversations();

        Collections.sort(newConversation.getUsernames());

        boolean ok=false;
        for(Conversation conversation : conversations)
        {
            Collections.sort(conversation.getUsernames());



            if(conversation.getUsernames().equals(newConversation.getUsernames()))
            {
                currentConversation=conversation;
                ok=true;

                createPaneMessages();
                paneNewConversation.setVisible(false);

            }

        }
        if(ok==false)
        {

            newConversation.setId(generateIDConversation());
            newConversation.setMessageDTO(new MessageDTO("","",""));
            messageService.addConversation(newConversation);

            conversations.add(newConversation);

            currentConversation=newConversation;


            createPaneMessages();
            paneNewConversation.setVisible(false);

        }







    }





    private int numberOfFriendRequestsInteger;
    @FXML
    private Label numberOfFriendRequests;

    private VBox mainVBox;
    @FXML
    private ScrollPane requestScrollPane;


    private void calibration()
    {


        numberOfFriendRequestsInteger--;
        if(numberOfFriendRequestsInteger==1)
        numberOfFriendRequests.setText(numberOfFriendRequestsInteger+" friend request");
        else
            numberOfFriendRequests.setText(numberOfFriendRequestsInteger+" friend requests");


    }

    public HBox getShowRequestHBox(User user)
    {

        HBox mainHBox= new HBox();
        mainHBox.setStyle("-fx-background-color: #46474A;");



        ImageView imageView=new ImageView();
        setImage(imageView,user.getImageUrl());

        imageView.setFitHeight(50.0);
        imageView.setFitWidth(50.0);

        imageView.setStyle("-fx-padding: 10 10 10 10");

        StackPane stackPaneforImageView = new StackPane();
        stackPaneforImageView.getChildren().add(imageView);
        stackPaneforImageView.setPadding(new Insets(10,10,10,10));
        stackPaneforImageView.setStyle("-fx-background-color: transparent;");



        VBox labelButtonVBox = new VBox();

        Label label=new Label(user.getFirstName()+" "+user.getLastName());
        label.setCursor(Cursor.HAND);
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {

                                    @Override
                                    public void handle(MouseEvent event) {
                                        Data.username = user.getUsername();
                                        createUserProfilePage();
                                    }
                                });

        label.setPadding(new Insets(5,0,0,15));
        label.setStyle("-fx-font-size: 14px;-fx-text-fill: white; -fx-font-weight: bold;");


        HBox buttonsHBox=new HBox();

        Button confirmButton = new Button("Confirm");
        confirmButton.setCursor(Cursor.HAND);

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                mainVBox.getChildren().remove(mainHBox);
                Data.currentUser=user;
                onAcceptButton(event);
                calibration();

            }
        });

        confirmButton.setStyle("-fx-text-fill: white;-fx-background-color:   #6D1FF2");
        StackPane stackPaneforConfirmButton = new StackPane();
        stackPaneforConfirmButton.getChildren().add(confirmButton);
        stackPaneforConfirmButton.setPadding(new Insets(10,25,0,25));
        stackPaneforConfirmButton.setStyle("-fx-background-color: transparent;");


        Button deleteButton = new Button("Delete");
        deleteButton.setCursor(Cursor.HAND);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                mainVBox.getChildren().remove(mainHBox);
                Data.currentUser=user;
                onDenyButton(event);
                calibration();

            }
        });


        StackPane stackPaneforDeleteButton = new StackPane();
        stackPaneforDeleteButton.getChildren().add(deleteButton);
        stackPaneforDeleteButton.setPadding(new Insets(10,25,0,25));
        stackPaneforDeleteButton.setStyle("-fx-background-color: transparent;");



        buttonsHBox.getChildren().setAll(stackPaneforConfirmButton,stackPaneforDeleteButton);
        labelButtonVBox.getChildren().setAll(label,buttonsHBox);
        mainHBox.getChildren().setAll(stackPaneforImageView,labelButtonVBox);



        return mainHBox;
    }
    double sizehbox=70.4;

    public void createGroupPane()
    {

        Data.task="GROUP";
        makeAllMainButtonGray();
        imageViewGroupGray.setVisible(false);
        imageViewGroupWhite.setVisible(true);

        disableAll();
        requestsAnchoPaneMain.setVisible(true);
        requestScrollPane.setVisible(true);

        List<String> usernames = messageService.getFriendshipRequests(Data.connectedUser.getUsername());
        List<User> users = userService.getUsersFromUsernames(usernames);



        numberOfFriendRequestsInteger = users.size();
        if (numberOfFriendRequestsInteger == 0)
        {
            numberOfFriendRequests.setText(numberOfFriendRequestsInteger + " friend requests");
            requestScrollPane.setVisible(false);
        }
        else {


            if (numberOfFriendRequestsInteger == 1)
                numberOfFriendRequests.setText(numberOfFriendRequestsInteger + " friend request");
            else
                numberOfFriendRequests.setText(numberOfFriendRequestsInteger + " friend requests");

            requestScrollPane.setPrefWidth(320.0);





            mainVBox = new VBox();
            mainVBox.setPrefWidth(300.0);
            for (User user : users) {

                mainVBox.getChildren().add(getShowRequestHBox(user));
            }

            requestScrollPane.setContent(mainVBox);




        }

    }
    @FXML
    Pane paneNewConversation;


    @FXML
    public void onMainGroupButton(MouseEvent mouseEvent) {
        createGroupPane();
    }


    List<Event> events;
    int numberOfEvents;

    @FXML
    Pagination paginationEvents;

    @FXML
    private void onCreateEventPane(MouseEvent mouseEvent)
    {

        createEventPane();

    }
    private void createEventPane()
    {
        Data.task="EVENT";
        makeAllMainButtonGray();
        imageViewEventGray.setVisible(false);
        imageViewEventWhite.setVisible(true);
        disableAll();
        eventAnchorPane.setVisible(true);



        events=eventService.getAll();

        numberOfEvents=events.size();
        numberOfPages=(numberOfEvents+4)/5;

        if(numberOfEvents!=0)
        {

            paginationEvents.setVisible(true);


            eventService.setPageSize(5);
            paginationEvents.setPageCount(numberOfPages);
            paginationEvents.setPageFactory((Integer pageIndex) -> createPageEvents(pageIndex,events));

            anchor.setTopAnchor(paginationEvents, 50.0);
            anchor.setLeftAnchor(paginationEvents, 100.0);
            anchor.setBottomAnchor(paginationEvents, 50.0);
            anchor.setRightAnchor(paginationEvents, 400.0);

        }
        else
        {
            paginationEvents.setVisible(false);
            //errorMessageSearch.setVisible(true);

        }


        // TODO
    }


    @FXML
    ScrollPane scrollPaneNotification;


    @FXML
    private void onNotificationButton(MouseEvent mouseEvent) throws ParseException {
        scrollPaneNotification.setVisible(true);
        labelNotification.setVisible(false);
        labelNotification.setText("0");
        makeAllMainButtonGray();

        imageViewNotificationWhite.setVisible(true);
        imageViewNotificationGray.setVisible(false);


        fillScrollPaneNotification();

    }
    @FXML
    private void onNotificationButtonWhite(MouseEvent mouseEvent) throws ParseException {
        scrollPaneNotification.setVisible(false);
        imageViewNotificationWhite.setVisible(false);
        imageViewNotificationGray.setVisible(true);

    }




    private void fillScrollPaneNotification() throws ParseException {

        VBox panelEventVBox=new VBox();
        panelEventVBox.setPrefWidth(300);
        List<Event> eventList= eventService.getAllEventsForGivenUsername(Data.connectedUser.getUsername());

        for(Event event :eventList)
        {

            HBox hbox= getEventHbox(event);
            panelEventVBox.getChildren().add(hbox);

        }

        scrollPaneNotification.setContent(panelEventVBox);
        scrollPaneNotification.vvalueProperty().bind(panelEventVBox.heightProperty());

    }


    private VBox createPageEvents(Integer pageIndex, List<Event> events) {
        VBox panelEvent=new VBox();

        panelEvent.setVisible(true);


        List<Event> eventList=eventService.getEventsOnPage(pageIndex);



        Collections.sort(eventList, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });


        // TODO
        //  Collections.sort(conversations,new SortByData());



        for(Event event :eventList)
        {

            HBox hbox= getEventHbox(event);
            panelEvent.getChildren().add(hbox);

        }

        return panelEvent;

    }

    @FXML
    Pane paneEventDetails;
    @FXML
    Label labelTitleEvent;
    @FXML
    Label labelDescriptionEvent;
    @FXML
    ImageView imageEventImageUrl;
    @FXML
    Button buttonSubscribe;
    @FXML
    Button buttonUnSubscribe;

    Event currentEvent;



    private boolean existStringInList(String giveString,List<String> list)
    {

        for(String string : list)
        {
            if(Objects.equals(string,giveString))
                return  true;
        }
        return false;

    }


    public HBox getEventHbox(Event event)
    {

        HBox mainHBox= new HBox();
        mainHBox.setStyle("-fx-background-color: #46474A;");



        ImageView imageView=new ImageView();


        setImage(imageView,event.getImageUrl());

        imageView.setFitHeight(35.0);
        imageView.setFitWidth(35.0);

        imageView.setStyle("-fx-padding: 10 10 10 10");

        StackPane stackPaneforImageView = new StackPane();
        stackPaneforImageView.getChildren().add(imageView);
        stackPaneforImageView.setPadding(new Insets(10,10,10,10));
        stackPaneforImageView.setStyle("-fx-background-color: transparent;");



        VBox labelTitleNumberOfParticipantsDateVBox = new VBox();

        String title= event.getTitle();
        int numberOfParticipants=event.getParticipants().size();
        String date= event.getDate();


        Label labelTitle=new Label(title);
        Label labelNumberOfParticipants= new Label("Participants: "+String.valueOf(numberOfParticipants));
        Label labelDate = new Label(date);


        labelTitle.setPadding(new Insets(5,0,0,15));
        labelTitle.setStyle("-fx-font-size: 14px;-fx-text-fill: white; -fx-font-weight: bold;");

        labelNumberOfParticipants.setPadding(new Insets(5,0,0,15));
        labelNumberOfParticipants.setStyle("-fx-font-size: 12px;-fx-text-fill: #b0b0b0;");

        labelDate.setPadding(new Insets(5,0,0,15));
        labelDate.setStyle("-fx-font-size: 12px;-fx-text-fill: #b0b0b0;");



        labelTitleNumberOfParticipantsDateVBox.getChildren().setAll(labelTitle,labelNumberOfParticipants,labelDate);


        mainHBox.getChildren().setAll(stackPaneforImageView,labelTitleNumberOfParticipantsDateVBox);



        mainHBox.setCursor(Cursor.HAND);
        mainHBox.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {

                currentEvent=event;
                buttonUnSubscribe.setVisible(false);
                buttonSubscribe.setVisible(false);

                paneEventDetails.setVisible(true);
                labelTitleEvent.setText(title);
                labelDescriptionEvent.setText(event.getDescription());
                setImage(imageEventImageUrl,event.getImageUrl());

                if(existStringInList(Data.connectedUser.getUsername(),event.getParticipants()))
                {
                    buttonUnSubscribe.setVisible(true);

                }
                else
                {
                    buttonSubscribe.setVisible(true);
                }

                if(Objects.equals(scrollPaneNotification.isVisible(),true))
                {
                    scrollPaneNotification.setVisible(false);
                    imageViewNotificationWhite.setVisible(false);
                    imageViewNotificationGray.setVisible(true);
                    createEventPane();
                    paneEventDetails.setVisible(true);
                    labelTitleEvent.setText(title);
                    labelDescriptionEvent.setText(event.getDescription());
                    setImage(imageEventImageUrl,event.getImageUrl());

                }



            }
        });




        return mainHBox;
    }





    @FXML
    private void onSubscribeButtonClick(ActionEvent actionEvent) throws ParseException {
          eventService.addParticipantAtEvent(currentEvent,Data.connectedUser.getUsername());
          if(eventService.isEventComingSoon(currentEvent))
          {
              labelNotification.setVisible(true);
              labelNotification.setText(String.valueOf(Integer.parseInt(labelNotification.getText()) + 1));
          }

          buttonSubscribe.setVisible(false);
          buttonUnSubscribe.setVisible(true);




    }

    @FXML
    private void onUnsubscribeButtonClick(ActionEvent actionEvent) throws ParseException {

        eventService.removeParticipantAtEvent(currentEvent,Data.connectedUser.getUsername());
        buttonSubscribe.setVisible(true);
        buttonUnSubscribe.setVisible(false);

        if(eventService.isEventComingSoon(currentEvent))
        {
            labelNotification.setVisible(true);
            if(Integer.parseInt(labelNotification.getText())>1)
            labelNotification.setText(String.valueOf(Integer.parseInt(labelNotification.getText()) - 1));
            else {
                labelNotification.setText("0");
                labelNotification.setVisible(false);
            }

        }


    }






    @FXML
    public void OnLogoutBtnClick(MouseEvent mouseEvent) throws IOException {

        makeAllMainButtonGray();
        imageViewLogoutGray.setVisible(false);
        imageViewLogoutWhite.setVisible(true);


        changeScene("login.fxml");
    }






}

