package com.example.lab6.socialnetwork.service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class ServiceController {

    public void setImage(ImageView imageView, String imageURL)
    {
        if(Objects.equals(getClass().getResourceAsStream(imageURL),null))
            imageView.setImage(new Image(imageURL));
        else
            imageView.setImage(new Image(getClass().getResourceAsStream(imageURL)));


    }


}
