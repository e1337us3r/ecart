package com.apolets.Controllers;

import com.apolets.main.API;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public LineChart profitGraph;
    public Label profitLabel;
    public JFXComboBox<String> timePickerComboBox;
    public Label refundLabel;
    public Label pendingLabel;
    private API profitApi;
    private API pendingApi;
    private API refundApi;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        profitApi = new API();
        pendingApi = new API();
        refundApi = new API();
        // profitGraph.setLegendVisible(false);

        //TODO:Localize these
        timePickerComboBox.getItems().addAll(
                "Today",
                "Week",
                "Month",
                "Year"
        );

        setFinanceInfo(profitApi, "Complete", "week");
        setFinanceInfo(pendingApi, "Waiting for Approval", "week");
        setFinanceInfo(refundApi, "Refund", "week");


    }

    public void changeGraphClickListener(MouseEvent e) {

        AnchorPane a = (AnchorPane) e.getSource();
        String timePeriod = "week";
        try {
            timePeriod = timePickerComboBox.getValue().toLowerCase();
        } catch (Exception ex) {

        }
        if (a.getId().contains("profit"))
            drawGraph(profitApi, timePeriod, "Profit");
        else if (a.getId().contains("pending"))
            drawGraph(pendingApi, timePeriod, "Pending");
        else drawGraph(refundApi, timePeriod, "Refunds");

    }


    private void setFinanceInfo(API api, String status, String timePeriod) {

        new Thread(new Task<Object>() {
            @Override
            protected Object call() {
                if (api.getFinanceInfoRequest(timePeriod, status)) {//change status to pending


                    if (status.equalsIgnoreCase("completed")) {

                        Platform.runLater(() -> {
                            profitLabel.setText(String.valueOf(api.calculateTotalFinanceInfo()));
                            drawGraph(api, timePeriod, "Profit");
                        });


                    } else if (status.equalsIgnoreCase("refund"))
                        Platform.runLater(() -> {
                            refundLabel.setText(String.valueOf(api.calculateTotalFinanceInfo()));
                        });

                    else
                        Platform.runLater(() -> {
                            pendingLabel.setText(String.valueOf(api.calculateTotalFinanceInfo()));
                        });


                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, API.getError(), ButtonType.OK);
                    alert.showAndWait();
                }

                return null;
            }
        }).start();





    }


    public void refreshInfosComboBoxListener(ActionEvent e) {

        setFinanceInfo(profitApi, "Completed", timePickerComboBox.getValue().toLowerCase());
        setFinanceInfo(pendingApi, "Waiting for Approval", timePickerComboBox.getValue().toLowerCase());
        setFinanceInfo(refundApi, "Refund", timePickerComboBox.getValue().toLowerCase());


    }


    private void drawGraph(API api, String timePeriod, String label) {
        profitGraph.getData().clear();
        int unit = 30;

        switch (timePeriod) {

            case "today":
                unit = 24;
                break;
            case "week":
                unit = 7;
                break;
            case "month":
                unit = 30;
                break;
            case "year":
                unit = 12;
                break;
        }

        XYChart.Series lineData = new XYChart.Series();
        lineData.setName(label);
        HashMap<Integer, Double> map = api.getGraphContent(unit);
        for (Map.Entry e : map.entrySet()) {
            //System.out.println(e.getKey()+" "+e.getValue());
            lineData.getData().add(new XYChart.Data<String, Double>(String.valueOf(e.getKey()), (Double) e.getValue()));
        }

        profitGraph.getData().clear();
        profitGraph.getData().addAll(lineData);
    }

}
