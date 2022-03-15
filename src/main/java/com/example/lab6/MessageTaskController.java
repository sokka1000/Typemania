

package com.example.lab6;


import com.example.lab6.socialnetwork.domain.Message;
import com.example.lab6.socialnetwork.service.MessageTaskService;
import com.example.lab6.utils.events.MessageTaskChangeEvent;
import com.example.lab6.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageTaskController implements Observer<MessageTaskChangeEvent> {
    MessageTaskService service;
    ObservableList<Message> model = FXCollections.observableArrayList();


    @FXML
    TableView<Message> tableView;
    @FXML
    TableColumn<Message,String> tableColumnDesc;
    @FXML
    TableColumn<Message,String> tableColumnFrom;
    @FXML
    TableColumn<Message,String> tableColumnTo;
    @FXML
    TableColumn<Message,String> tableColumnData;

    public void setMessageTaskService(MessageTaskService messageTaskService) {
        service = messageTaskService;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        // TODO
        tableColumnDesc.setCellValueFactory(new PropertyValueFactory<Message, String>("description"));
        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<Message, String>("from"));
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<Message, String>("to"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory<Message, String>("date"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<Message> messages = service.getAll();
        List<Message> messageTaskList = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(messageTaskList);

    }

    @Override
    public void update(MessageTaskChangeEvent messageTaskChangeEvent) {
        initModel();
    }

    public void handleDeleteMessage(ActionEvent actionEvent) {
        // TODO
    }


    @FXML
    public void handleUpdateMessage(ActionEvent ev) {
        // TODO
    }

    @FXML
    public void handleAddMessage(ActionEvent ev) {
        // TODO
    }

    public void showMessageTaskEditDialog(Message messageTask) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/editmessagetask-view.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Message");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            //EditMessageTaskController editMessageViewController = loader.getController();
          //  editMessageViewController.setService(service, dialogStage, messageTask);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
