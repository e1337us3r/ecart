package com.apolets.Controllers;

import com.apolets.main.API;
import com.apolets.main.Listing;
import com.apolets.main.fxMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ListingsController implements Initializable {
    public static TreeItem<Listing> selectedListingItem = null;
    public static AnchorPane mainParent;
    public JFXTreeTableView<Listing> listingsTable;
    public JFXButton deleteListingButton;
    public JFXButton createListingButton;
    public JFXButton updateListingButton;
    public AnchorPane rootPane;
    public static TreeItem<Listing> treeRoot = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        setupTable();


        //GET DATE AND SET IT TO TABLE
        refreshItems();


    }

    public void setMain(AnchorPane ap) {
        mainParent = ap;
    }

    private void setupTable() {
        listingsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {

            if (newSelection != null) {
                selectedListingItem = newSelection;
                updateListingButton.setDisable(false);
                deleteListingButton.setDisable(false);
            } else {
                updateListingButton.setDisable(true);
                deleteListingButton.setDisable(true);
            }

        });

        listingsTable.setShowRoot(false);//DON'T SHOW ROOT EXPLICITLY

        //CREATE THE COLUMNS
        JFXTreeTableColumn<Listing, String> nameCol = new JFXTreeTableColumn<>("Name");
        JFXTreeTableColumn<Listing, String> descCol = new JFXTreeTableColumn<>("Description");
        descCol.setMaxWidth(100);
        JFXTreeTableColumn<Listing, Double> priceCol = new JFXTreeTableColumn<>("Price");
        JFXTreeTableColumn<Listing, Double> costCol = new JFXTreeTableColumn<>("Cost");
        JFXTreeTableColumn<Listing, Integer> stockCol = new JFXTreeTableColumn<>("Stock");
        JFXTreeTableColumn<Listing, LocalDate> dateCol = new JFXTreeTableColumn<>("Release Date");
        JFXTreeTableColumn<Listing, String> categoryCol = new JFXTreeTableColumn<>("Category");
        JFXTreeTableColumn<Listing, Integer> ratingCol = new JFXTreeTableColumn<>("Rating");

        //ADD COLUMNS TO TABLE
        listingsTable.getColumns().addAll(nameCol, descCol, priceCol, costCol, stockCol, dateCol, categoryCol);

        //SET VALUES TO VIEW IN COLUMNS
        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        priceCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("price"));
        costCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("cost"));
        stockCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("stock"));
        dateCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("listDate"));
        categoryCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("category"));
        ratingCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("rating"));
    }

    public void refreshItems() {
        Platform.runLater(() -> {
            if (API.fetchAllListingsRequest()) {
                ObservableList<Listing> listings = API.fetchAllListingsPayload();
                treeRoot = new RecursiveTreeItem<>(listings, RecursiveTreeObject::getChildren);
                listingsTable.setRoot(treeRoot);
            } else
                System.out.println(API.getError());
        });
    }


    public void updateListing() {
        changeView(true);
    }

    public void deleteListing() {

        boolean result = API.deleteListingRequest(selectedListingItem.getValue().getId());
        if (result) {
            selectedListingItem.getParent().getChildren().remove(selectedListingItem); //remove the selected node from it's parent's children.
        } else System.out.println(API.getError());

    }

    public void createListing() {
        changeView(false);
    }


    private void changeView(Boolean update) {

        Platform.runLater(() -> {
            try {

                CRUDListingsController controller;

                if (update) {
                    controller = new UpdateListingController(selectedListingItem.getValue(), listingsTable);
                } else {
                    controller = new CreateListingController(listingsTable);
                }
                controller.setParent(rootPane);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/crudListing.fxml"), fxMain.languageBundle);
                loader.setController(controller);

                mainParent.getChildren().clear();
                mainParent.getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


}
