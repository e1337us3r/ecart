package com.apolets.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class fxMain extends Application {

    public static Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    public static ResourceBundle languageBundle = ResourceBundle.getBundle("view/lang", new Locale("en", "US"));
    private static Parent dashboardScene = null;

    public static void exitApplication() {
        window.close();

    }

    public static void switchToDashboard() {
        try {
            dashboardScene = FXMLLoader.load(fxMain.class.getResource("/view/dashboard.fxml"), languageBundle);
            window.setTitle("E-cart.com Merchant Client v.01");
            window.setScene(new Scene(dashboardScene));
            window.setResizable(false);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) {
        Parent loginScene = null;
        window = primaryStage;

        try {

            loginScene = FXMLLoader.load(getClass().getResource("/view/login.fxml"), languageBundle);
            window.setTitle("E-cart.com Merchant Client v.01");
            window.setScene(new Scene(loginScene));
            window.setResizable(false);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
