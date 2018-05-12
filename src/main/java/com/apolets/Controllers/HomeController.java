package com.apolets.Controllers;

import com.apolets.API.FinanceInfoRequest;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public LineChart profitGraph;
    public Label profitLabel;
    public JFXComboBox<String> timePickerComboBox;
    public Label refundLabel;
    public Label pendingLabel;
    private JSONArray profitData;
    private JSONArray pendingData;
    private JSONArray refundData;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        profitData = new JSONArray();
        pendingData = new JSONArray();
        refundData = new JSONArray();

        timePickerComboBox.getItems().addAll(
                "Today",
                "Week",
                "Month",
                "Year"
        );

        setFinanceInfo(profitData, "Complete", "week");
        setFinanceInfo(pendingData, "Waiting for Approval", "week");
        setFinanceInfo(refundData, "Refund", "week");


    }

    public void changeGraphClickListener(MouseEvent e) {

        AnchorPane a = (AnchorPane) e.getSource();
        String timePeriod = "week";
        try {
            timePeriod = timePickerComboBox.getValue().toLowerCase();
        } catch (Exception ex) {

        }
        if (a.getId().contains("profit"))
            drawGraph(profitData, timePeriod, "Profit");
        else if (a.getId().contains("pending"))
            drawGraph(pendingData, timePeriod, "Pending");
        else drawGraph(refundData, timePeriod, "Refunds");

    }


    private void setFinanceInfo(JSONArray data, String status, String timePeriod) {

        new FinanceInfoRequest(timePeriod, status) {
            @Override
            protected void success(JSONObject response) {
                if (status.equalsIgnoreCase("completed")) {

                    Platform.runLater(() -> {
                        profitData = response.getJSONArray("items");

                        profitLabel.setText(calculateTotalFinanceInfo(profitData));
                        drawGraph(profitData, timePeriod, "Profit");
                    });


                } else if (status.equalsIgnoreCase("refund"))
                    Platform.runLater(() -> {
                        refundData = response.getJSONArray("items");
                        refundLabel.setText(calculateTotalFinanceInfo(refundData));
                    });

                else
                    Platform.runLater(() -> {
                        pendingData = response.getJSONArray("items");
                        pendingLabel.setText(calculateTotalFinanceInfo(pendingData));
                    });
            }

            @Override
            protected void fail(String error) {
                Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
                alert.showAndWait();
            }
        };
    }


    public void refreshInfosComboBoxListener(ActionEvent e) {

        setFinanceInfo(profitData, "Completed", timePickerComboBox.getValue().toLowerCase());
        setFinanceInfo(pendingData, "Waiting for Approval", timePickerComboBox.getValue().toLowerCase());
        setFinanceInfo(refundData, "Refund", timePickerComboBox.getValue().toLowerCase());


    }


    private void drawGraph(JSONArray data, String timePeriod, String label) {
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
        HashMap<Integer, Double> map = getGraphContent(unit, data);
        for (Map.Entry e : map.entrySet()) {
            //System.out.println(e.getKey()+" "+e.getValue());
            lineData.getData().add(new XYChart.Data<String, Double>(String.valueOf(e.getKey()), (Double) e.getValue()));
        }

        profitGraph.getData().clear();
        profitGraph.getData().addAll(lineData);
    }

    public HashMap<Integer, Double> getGraphContent(int units, JSONArray data) {

        HashMap<Integer, Double> map = new HashMap<>();
        for (int i = 1; i <= units; i++) {
            map.put(i, 0.0);
        }
        JSONArray entries = data;
        for (int i = 0; i < entries.length(); i++) {
            JSONArray entry = entries.getJSONArray(i);
            map.put(entry.getInt(0), entry.getDouble(1));
        }


        return map;
    }

    public String calculateTotalFinanceInfo(JSONArray data) {
        Double total = 0.0;


        JSONArray entries = data;
        for (int i = 0; i < entries.length(); i++) {
            JSONArray entry = entries.getJSONArray(i);
            total += entry.getDouble(1);
        }


        return new DecimalFormat("#0.00").format(total);
    }

}
