package com.apolets.Controllers;

import com.apolets.main.API;
import com.apolets.main.Listing;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CreateListingController extends CRUDListingsController {


    private JFXTreeTableView table;


    public CreateListingController(JFXTreeTableView table, ObservableList<Listing> oblistings) {
        this.oblistings = oblistings;
        this.table = table;
    }

    public void setParent(Node node) {
        parent = node;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        deleteAdditionalButton.setDisable(true);
        loadCategories();
        fileChooserSetup();
        setupValidators();

    }

    public void crudListing() {

        if (validateInputs()) {
            HashMap<String, Object> inputs = getInput();
            try {
                //required step due to server api, and my laziness to change it
                additionalImages.add(0, new File(new URI(profileImageURI)));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            boolean result = API.createListingRequest((String) inputs.get("name"), (Double) inputs.get("price"), (String) inputs.get("description"), (Integer) inputs.get("stock"), (String) inputs.get("category"), (Double) inputs.get("cost"), additionalImages);

            if (result) {
                System.out.println("Success");
                oblistings.add(API.getListing());
                displayDialog(false);
            } else displayDialog(true);
        }

    }

    @Override
    public void deleteAdditional() {
        if (selectednode != null && !selectedUri.isEmpty()) {
            imageBox.getChildren().remove(selectednode);
            additionalImages.removeIf((File file) -> file.toURI().toString().equalsIgnoreCase(selectedUri));
            selectedUri = "";
            selectednode = null;
            addAdditionalButton.setDisable(false);
            deleteAdditionalButton.setDisable(true);
            imageCount--;
        }
    }


}

