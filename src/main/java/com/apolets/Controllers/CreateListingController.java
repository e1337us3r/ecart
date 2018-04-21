package com.apolets.Controllers;

import com.apolets.main.API;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.scene.Node;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CreateListingController extends CRUDListingsController {


    private JFXTreeTableView table;

    public CreateListingController(JFXTreeTableView table) {
        this.table = table;
    }

    public void setParent(Node node) {
        parent = node;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        deleteAdditionalButton.setDisable(true);
        fileChooserSetup();

    }

    public void crudListing() {

        //TODO: validation
        HashMap<String, Object> inputs = getInput();

        boolean result = API.createListingRequest((String) inputs.get("name"), (Double) inputs.get("price"), (String) inputs.get("description"), (Integer) inputs.get("stock"), (String) inputs.get("category"), (Double) inputs.get("cost"), additionalImages);

        if (result) {
            System.out.println("Success");
            table.getRoot().getChildren().add(API.getListing());
            table.refresh();
        } else System.out.println(API.getError());


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

