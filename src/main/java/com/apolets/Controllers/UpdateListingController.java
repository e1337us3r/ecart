package com.apolets.Controllers;

import com.apolets.main.API;
import com.apolets.main.Listing;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdateListingController extends CRUDListingsController {

    private Listing existingListing = null;
    private ArrayList<String> deletedAdditionalImages = new ArrayList<>(); //these are additionals that already existed and need to be sent separately to server in order to be deleted completely
    private JFXTreeTableView table;

    public UpdateListingController(Listing existingListing, JFXTreeTableView table) {
        this.table = table;
        this.existingListing = existingListing;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize update = " + existingListing.getAdditionalImages());
        if (existingListing != null) {
            txtName.setText(existingListing.getName());
            txtDesc.setText(existingListing.getDesc());
            txtCost.setText(String.valueOf(existingListing.getCost()));
            txtPrice.setText(String.valueOf(existingListing.getPrice()));
            txtStock.setText(String.valueOf(existingListing.getStock()));
            profileImageURI = existingListing.getStoreImage();
            if (existingListing.getAdditionalImages().size() > 0) {
                imageBox.getChildren().clear();
                addToImageBox(existingListing.getAdditionalImages());
            }
            initProfileImage();

        }

        deleteAdditionalButton.setDisable(true);
        fileChooserSetup();

    }

    @Override
    public void crudListing() {

        Listing temp = existingListing;

        HashMap<String, Object> inputs = getInput();

        temp.setName((String) inputs.get("name"));
        temp.setPrice((Double) inputs.get("price"));
        temp.setCost((Double) inputs.get("cost"));
        temp.setCategory((String) inputs.get("category"));
        temp.setDesc((String) inputs.get("description"));
        temp.setStock((Integer) inputs.get("stock"));
        temp.setStoreImage(profileImageURI);


        if (API.updateListingRequest(temp, additionalImages, deletedAdditionalImages, profileImageURI)) {
            temp = API.getListing();
            existingListing.setAdditionalImages(temp.getAdditionalImages());
            System.out.println("api return= " + existingListing.getAdditionalImages());
            table.refresh();
        } else System.out.println(API.getError());


    }


    @Override
    public void deleteAdditional() {
        if (selectednode != null && !selectedUri.isEmpty()) {
            if (selectedUri.startsWith("file:"))
                additionalImages.removeIf((File file) -> file.toURI().toString().equalsIgnoreCase(selectedUri));
            else
                deletedAdditionalImages.add(selectedUri);

            imageBox.getChildren().remove(selectednode);
            selectedUri = "";
            selectednode = null;
            addAdditionalButton.setDisable(false);
            deleteAdditionalButton.setDisable(true);
            imageCount--;

        }

    }

    private void initProfileImage() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/image-template.fxml"));
            ImageContainerController controller = new ImageContainerController(profileImageURI);
            loader.setController(controller);
            Node image = loader.load();
            profileImagePane.getChildren().clear();
            profileImagePane.getChildren().add(image);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void addToImageBox(ArrayList<String> images) {

        for (String f : images) {

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/image-template.fxml"));
                ImageContainerController controller = new ImageContainerController(f);
                loader.setController(controller);
                Node image = loader.load();


                image.setOnMouseClicked(event -> {
                    selectednode = image;
                    selectedUri = controller.getImageUrl();
                    deleteAdditionalButton.setDisable(false);
                });

                imageBox.getChildren().add(image);
                imageCount++;

                if (imageCount == 5) {
                    addAdditionalButton.setDisable(true);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
