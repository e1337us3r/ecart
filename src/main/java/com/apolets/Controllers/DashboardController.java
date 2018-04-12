package com.apolets.Controllers;


import com.apolets.main.fxMain;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    public JFXButton navHomeButton;
    public JFXButton navListingsButton;
    public JFXButton navOrdersButton;
    public JFXButton navAccountButton;
    public AnchorPane mainParent;

    private Node home = null;


    private void changeView(Node node) {
        Platform.runLater(() -> {
            mainParent.getChildren().clear();
            mainParent.getChildren().add(node);
        });

    }


    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(() -> {
            try {
                home = FXMLLoader.load(getClass().getResource("/view/home.fxml"), fxMain.languageBundle);
                changeView(home);
            } catch (IOException e) {
                e.printStackTrace();
                fxMain.exitApplication();
            }
        });

    }

    public void showHome() {
        changeView(home);
    }


}





