package com.example.lab6;

import com.example.lab6.Launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class Controller {

    private double xOffset =0;
    private double yOffset =0;

    public void changeScene(String FileXMLScene) throws IOException {

        Parent root= FXMLLoader.load(getClass().getResource(FileXMLScene));
        Launch.stage.getScene().setRoot(root);

/*
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                xOffset= mouseEvent.getSceneX();
                yOffset=mouseEvent.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Launch.stage.setX(mouseEvent.getScreenX()-xOffset);
                Launch.stage.setY(mouseEvent.getScreenY()-yOffset);
            }
        });

*/
    }
}
