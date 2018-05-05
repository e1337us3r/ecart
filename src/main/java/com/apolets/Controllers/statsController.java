package com.apolets.Controllers;

import com.apolets.main.API;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class statsController implements Initializable {
    public VBox topContainer;
    public PieChart pieChart;
    public ProgressIndicator fulfillmentIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pieChart.setLabelsVisible(false);


        new Thread(() -> {
            if (API.getStatsRequest()) {
                //Animation for setting fulfillment indicator value
                new Thread(() -> {
                    double fulfillmentRate = API.getFulfillmentRate();
                    double currentRate = 0.0;

                    while (currentRate < fulfillmentRate) {

                        try {
                            Thread.sleep(25);
                            currentRate += 0.01;
                            fulfillmentIndicator.setProgress(currentRate);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();

                LinkedHashMap<String, Integer> categoriesMap = API.getTopCategories();
                for (Map.Entry<String, Integer> singleCat : categoriesMap.entrySet()) {
                    Platform.runLater(() -> {
                        pieChart.getData().add(new PieChart.Data(singleCat.getKey(), singleCat.getValue()));
                    });


                }

                String[] topListings = API.getTop5Listings();

                int i = 0;
                for (String topListing : topListings) {
                    i++;
                    Label listingLabel = new Label(i + ". " + topListing);
                    listingLabel.getStyleClass().add("white-text");
                    listingLabel.setPadding(new Insets(0, 0, 15, 0));
                    Platform.runLater(() -> topContainer.getChildren().add(listingLabel));

                }

            }
        }).start();


    }
}
