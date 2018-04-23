package com.apolets.Controllers;

import com.apolets.main.API;
import com.apolets.main.Listing;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class CRUDListingsController implements Initializable {

//TODO: replace fields with JFX equivelent so that validators could be used
//TODO: Verifiy and Validate inputs

    public TextField txtName;
    public TextField txtPrice;
    public TextField txtCost;
    public TextField txtStock;
    public TextArea txtDesc;
    public HBox imageBox;
    public ComboBox<String> comboCat;
    public JFXButton chooseButton;
    public StackPane profileImagePane;
    public JFXButton addAdditionalButton;
    public JFXButton deleteAdditionalButton;
    public StackPane stackPane;
    protected ObservableList<Listing> oblistings;


    protected Node parent = null;

    protected String profileImageURI;
    protected ArrayList<File> additionalImages = new ArrayList<>();
    protected FileChooser chooser = new FileChooser();
    protected int imageCount = 0;

    protected Node selectednode;
    protected String selectedUri;


    protected void loadCategories() {

        if (API.fetchCategoriesRequest()) {
            comboCat.getItems().addAll(API.fetchCategories());
            comboCat.getSelectionModel().select(0);
        }
    }


    protected void displayDialog(boolean error) {


        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialogBox = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER, true);

        if (error) {
            dialogLayout.setHeading(new Text("Oh no! Something went wrong."));
            dialogLayout.setBody(new Text(API.getError()));
        } else {
            dialogLayout.setHeading(new Text("Operation successful!"));
        }

        JFXButton closeButton = new JFXButton("Close");
        closeButton.setOnAction(event -> {
            dialogBox.close();
        });
        dialogLayout.setActions(closeButton);

        dialogBox.show();

    }


    public void setParent(Node node) {
        parent = node;
    }

    public void goBack() {
        //TODO: reverse this so that listings controller deletes its child
        DashboardController.mainParentNode.getChildren().clear();
        DashboardController.mainParentNode.getChildren().add(parent);
    }


    protected HashMap<String, Object> getInput() {

        //TODO: Verify and Validate
        HashMap<String, Object> inputs = new HashMap<>();

        inputs.put("name", txtName.getText());
        inputs.put("description", txtDesc.getText());
        inputs.put("cost", Double.valueOf(txtCost.getText()));
        inputs.put("price", Double.valueOf(txtPrice.getText()));
        inputs.put("stock", Integer.parseInt(txtStock.getText()));

        try {
            inputs.put("category", comboCat.getValue());
        } catch (NullPointerException e) {
            inputs.put("category", "category 1");
        }


        return inputs;
    }

    public abstract void crudListing();


    protected void fileChooserSetup() {
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
        chooser.setTitle("Choose Image(s) for Listing");
    }

    public void chooseProfileImage() {

        File profileImage = chooser.showOpenDialog(chooseButton.getScene().getWindow());
        if (profileImage != null) {

            try {
                profileImageURI = profileImage.toURI().toString();

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
    }

    private void addToImageBox(List<File> images) {

        for (File f : images) {
            if (imageCount < 5) {
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/image-template.fxml"));
                    ImageContainerController controller = new ImageContainerController(f.toURI().toString());
                    loader.setController(controller);
                    Node image = loader.load();
                    additionalImages.add(f);


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

    public void addAdditional() {
        List<File> images;

        images = chooser.showOpenMultipleDialog(chooseButton.getScene().getWindow());
        if (images != null && images.size() > 0) {
            if (imageBox.getChildren().size() != 0 && imageBox.getChildren().get(0) instanceof Label) {
                imageBox.getChildren().clear();
            }
            addToImageBox(images);
        }
    }

    public abstract void deleteAdditional();


}
