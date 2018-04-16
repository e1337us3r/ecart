package com.apolets.Controllers;

import com.apolets.main.Listing;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class CRUDListingController implements Initializable {


    public TextField txtName;
    public TextField txtPrice;
    public TextField txtCost;
    public TextField txtStock;
    public TextArea txtDesc;

    public JFXButton chooseButton;
    private Node root = null;
    private Listing existingListing = null;

    private File image = null;

    public void setRoot(Node node) {
        root = node;
    }

    public void setListing(Listing listing) {
        existingListing = listing;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (existingListing != null) {
            txtName.setText(existingListing.getName());
            txtDesc.setText(existingListing.getDesc());
            txtCost.setText(String.valueOf(existingListing.getCost()));
            txtPrice.setText(String.valueOf(existingListing.getPrice()));
            txtStock.setText(String.valueOf(existingListing.getStock()));

        }


    }

    public void goBack() {
        ListingsController.mainParent.getChildren().clear();
        ListingsController.mainParent.getChildren().add(root);

    }

    public void createListing() {
    }

    public void chooseImage() {

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        chooser.setTitle("Choose Image for Listing");
        image = chooser.showOpenDialog(chooseButton.getScene().getWindow());

    }


}

