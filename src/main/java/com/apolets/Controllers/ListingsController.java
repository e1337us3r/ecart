package com.apolets.Controllers;

import com.apolets.main.API;
import com.apolets.main.Listing;
import com.apolets.main.fxMain;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

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
    public StackPane rootPane;
    public JFXTextField searchBar;
    public static TreeItem<Listing> treeRoot = null;
    private ObservableList<Listing> oblistings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        setupTable();

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            listingsTable.setPredicate(listingTreeItem -> {
                //System.out.println(listingsTable.);
                Listing testSubject = listingTreeItem.getValue();
                return testSubject.getName().contains(newValue)
                        || testSubject.getDesc().contains(newValue)
                        || testSubject.getCategory().contains(newValue);

            });
        });


        //GET DATA AND SET IT TO TABLE
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
        //TODO: use .property for labels
        JFXTreeTableColumn<Listing, String> nameCol = new JFXTreeTableColumn<>("Name");
        nameCol.setMinWidth(200);
        JFXTreeTableColumn<Listing, String> descCol = new JFXTreeTableColumn<>("Description");
        descCol.setMinWidth(190);
        JFXTreeTableColumn<Listing, Double> priceCol = new JFXTreeTableColumn<>("Price");
        JFXTreeTableColumn<Listing, Double> costCol = new JFXTreeTableColumn<>("Cost");
        JFXTreeTableColumn<Listing, Integer> stockCol = new JFXTreeTableColumn<>("Stock");
        JFXTreeTableColumn<Listing, LocalDate> dateCol = new JFXTreeTableColumn<>("Release Date");
        dateCol.setMinWidth(50);
        JFXTreeTableColumn<Listing, String> categoryCol = new JFXTreeTableColumn<>("Category");
        categoryCol.setMinWidth(50);
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
        new Thread(new Task<Object>() {
            @Override
            protected Object call() {
                if (API.fetchAllListingsRequest()) {
                    oblistings = API.fetchAllListingsPayload();
                    treeRoot = new RecursiveTreeItem<>(oblistings, RecursiveTreeObject::getChildren);
                    Platform.runLater(() -> {
                        listingsTable.setRoot(treeRoot);
                    });
                } else
                    System.out.println(API.getError());

                return null;
            }
        }).start();


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
                    controller = new CreateListingController(listingsTable, oblistings);
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
