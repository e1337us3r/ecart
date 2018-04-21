package com.apolets.Controllers;

import com.apolets.main.API;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ImageContainerController implements Initializable {

    public ImageView imageView;
    private String imageUrl = "";


    public ImageContainerController(String url) {
        imageUrl = url;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image((!imageUrl.startsWith("file:")) ? (API.SITEURL + "images/" + imageUrl) : imageUrl);
        imageView.setImage(image);
    }


    public String getImageUrl() {
        return imageUrl;
    }
}
