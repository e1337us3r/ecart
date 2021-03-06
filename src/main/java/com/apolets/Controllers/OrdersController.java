package com.apolets.Controllers;

import com.apolets.API.FetchOrdersRequest;
import com.apolets.API.UpdateOrderStatus;
import com.apolets.API.UpdateTrackingCode;
import com.apolets.main.ComboBoxEditorBuilder;
import com.apolets.main.Order;
import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {
    public JFXComboBox<String> statusCombo;
    public JFXDatePicker startDatePicker;
    public JFXDatePicker endDatePicker;
    public JFXTreeTableView<Order> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        setupComboBox();
    }

    private void setupComboBox() {
        statusCombo.getItems().add("All");
        statusCombo.getItems().add("Waiting For Approval");
        statusCombo.getItems().add("Preparing");
        statusCombo.getItems().add("Shipped");
        statusCombo.getItems().add("Refund");
        statusCombo.getItems().add("Completed");
    }

    private void setupTable() {
        tableView.setShowRoot(false);//DON'T SHOW ROOT EXPLICITLY
        tableView.setEditable(true);

        //CREATE THE COLUMNS
        //TODO: use .property for labels
        JFXTreeTableColumn<Order, String> nameCol = new JFXTreeTableColumn<>("Listing Name");
        nameCol.setMinWidth(200);
        JFXTreeTableColumn<Order, String> dateCol = new JFXTreeTableColumn<>("Date");
        dateCol.setMinWidth(150);
        JFXTreeTableColumn<Order, Integer> quantityCol = new JFXTreeTableColumn<>("Quantity");
        JFXTreeTableColumn<Order, Double> priceCol = new JFXTreeTableColumn<>("Price");
        JFXTreeTableColumn<Order, Double> costCol = new JFXTreeTableColumn<>("Cost");
        JFXTreeTableColumn<Order, String> statusCol = new JFXTreeTableColumn<>("Status");
        JFXTreeTableColumn<Order, String> trackingCol = new JFXTreeTableColumn<>("Tracking Code");
        statusCol.setMinWidth(200);
        //ADD COLUMNS TO TABLE
        tableView.getColumns().addAll(nameCol, dateCol, priceCol, costCol, quantityCol, statusCol, trackingCol);

        //SET VALUES TO VIEW IN COLUMNS
        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("listing_name"));
        priceCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("price"));
        costCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("cost"));
        dateCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("date"));
        quantityCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("quantity"));
        statusCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));
        trackingCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("trackingCode"));


        ObservableList<String> comboList = FXCollections.observableArrayList();


        comboList.add("Preparing");
        comboList.add("Shipped");
        comboList.add("Refund");

        //statusCol.setCellFactory(ChoiceBoxTreeTableCell.forTreeTableColumn(comboList));
        ComboBoxEditorBuilder comboEditor = new ComboBoxEditorBuilder(comboList);

        statusCol.setCellFactory(param -> new GenericEditableTreeTableCell<Order, String>(comboEditor));

        TextFieldEditorBuilder trackingEditor = new TextFieldEditorBuilder();
        trackingCol.setCellFactory(param -> new GenericEditableTreeTableCell<Order, String>(trackingEditor));


        trackingCol.setOnEditCommit(event -> {
            TreeItem<Order> editedOrder = tableView.getTreeItem(event.getTreeTablePosition().getRow());
            trackingEditor.setValue("");
            tableView.refresh();
            new UpdateTrackingCode(editedOrder.getValue().getId(), event.getNewValue()) {
                @Override
                protected void success(JSONObject response) {
                    editedOrder.getValue().setTrackingCode(event.getNewValue());
                    trackingEditor.setValue(event.getNewValue());
                    tableView.refresh();

                }

                @Override
                protected void fail(String error) {

                }
            };


        });

        statusCol.setOnEditCommit(event -> {
            TreeItem<Order> editedOrder = tableView.getTreeItem(event.getTreeTablePosition().getRow());

            if (event.getNewValue().equalsIgnoreCase("Preparing") && event.getOldValue().equalsIgnoreCase("Waiting For Approval")) {
                new UpdateOrderStatus(editedOrder.getValue().getId(), "Preparing") {
                    @Override
                    protected void success(JSONObject response) {
                        editedOrder.getValue().setStatus("Preparing");
                        tableView.refresh();
                    }

                    @Override
                    protected void fail(String error) {
                        System.out.println("fail");
                    }
                };
            } else if (event.getNewValue().equalsIgnoreCase("Shipped") && event.getOldValue().equalsIgnoreCase("Preparing") && !editedOrder.getValue().getTrackingCode().isEmpty()) {
                new UpdateOrderStatus(editedOrder.getValue().getId(), "Shipped") {
                    @Override
                    protected void success(JSONObject response) {
                        editedOrder.getValue().setStatus("Shipped");
                        tableView.refresh();
                    }

                    @Override
                    protected void fail(String error) {
                        System.out.println("fail");
                    }
                };


            } else if (event.getNewValue().equalsIgnoreCase("Refund") && comboList.contains(event.getOldValue())) {
                new UpdateOrderStatus(editedOrder.getValue().getId(), "Refund") {
                    @Override
                    protected void success(JSONObject response) {
                        editedOrder.getValue().setStatus("Refund");
                        tableView.refresh();
                    }

                    @Override
                    protected void fail(String error) {
                        System.out.println("fail");
                    }
                };
            } else comboEditor.setValue(event.getOldValue());

            tableView.refresh();
            //update database
        });


    }

    public void refreshTable() {
        if (validateInputs()) {

            new FetchOrdersRequest(statusCombo.getValue(), startDatePicker.getValue().toString(), endDatePicker.getValue().toString()) {
                @Override
                protected void success(JSONObject response) {
                    ObservableList<Order> orders = this.getOrdersPayload(response);
                    TreeItem<Order> treeRoot = new RecursiveTreeItem<>(orders, RecursiveTreeObject::getChildren);
                    Platform.runLater(() -> tableView.setRoot(treeRoot));
                }

                @Override
                protected void fail(String error) {
                    //TODO: show message?
                    System.out.println("FetchOrdersRequest : " + error);
                }
            };

        }
    }

    private boolean validateInputs() {

        return statusCombo.getValue() != null && startDatePicker.getValue() != null && endDatePicker.getValue() != null;


    }
}
