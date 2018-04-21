package com.apolets.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
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
        setUp();
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

    private static void setUp() {


        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

}
