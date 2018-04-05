package com.apolets.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class fxMain extends Application {

    static Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    public static void exitLogin() {
        window.close();

    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        window = primaryStage;

        try {
            root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            window.setTitle("E-cart.com Merchant Client v.01");
            window.setScene(new Scene(root, 1000, 580));
            window.setResizable(false);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
