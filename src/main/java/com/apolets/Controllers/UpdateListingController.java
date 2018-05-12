package com.apolets.Controllers;

import com.apolets.API.UpdateListingRequest;
import com.apolets.main.Listing;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.json.JSONObject;

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
    public Label masterLabel;
    public JFXButton createButton;

    public UpdateListingController(Listing existingListing, JFXTreeTableView table) {
        this.table = table;
        this.existingListing = existingListing;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Recycle Create Listing View
        masterLabel.setText(resources.getString("createlisting.updatelabel"));
        createButton.setText(resources.getString("createlisting.updatebutton"));


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

            loadCategories();
            comboCat.getSelectionModel().select(existingListing.getCategory());

        }

        deleteAdditionalButton.setDisable(true);
        fileChooserSetup();
        System.out.println(existingListing);

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


        new UpdateListingRequest(temp, additionalImages, deletedAdditionalImages, profileImageURI) {
            @Override
            protected void success(JSONObject response) {
                Listing updatedListing = this.getListing(response);
                existingListing.setName(updatedListing.getName());
                existingListing.setDesc(updatedListing.getDesc());
                existingListing.setStock(updatedListing.getStock());
                existingListing.setCategory(updatedListing.getCategory());
                existingListing.setCost(updatedListing.getCost());
                existingListing.setPrice(updatedListing.getPrice());
                existingListing.setStoreImage(updatedListing.getStoreImage());
                existingListing.setRating(updatedListing.getRating());
                existingListing.setAdditionalImages(updatedListing.getAdditionalImages());
                table.refresh();
                Platform.runLater(() -> displayDialog(false));

            }

            @Override
            protected void fail(String error) {
                Platform.runLater(() -> displayDialog(true));
            }
        };





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
