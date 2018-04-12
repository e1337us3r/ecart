package com.apolets.Controllers;

import com.apolets.InputValidator.EmailValidator;
import com.apolets.InputValidator.PasswordValidator;
import com.apolets.InputValidator.RequiredValidator;
import com.apolets.main.API;
import com.apolets.main.fxMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public JFXTextField jf_email;
    public JFXPasswordField jf_password;
    public JFXButton jb_login;
    public Hyperlink jlink_forgot;
    public JFXButton jb_signup;

    public void initialize(URL location, ResourceBundle resources) {


        jf_email.setValidators(new RequiredValidator(), new EmailValidator());
        jf_password.setValidators(new RequiredValidator(), new PasswordValidator());

    }


    public void login(ActionEvent e) {
        /*Had to use validate functions this way because if we put them directly in if condition
          they get validated in one by one and if one validate
          doesn't pass second one is not checked*/
        boolean passwordCheck = jf_password.validate();
        boolean emailCheck = jf_email.validate();
        if (passwordCheck && emailCheck) {

            //Standard Thread implementation doesn't work because of JavaFX rules
            //so Platform.runLater is used instead
            Platform.runLater(() -> {
                boolean loginSuccess = API.loginRequest(jf_email.getText(), jf_password.getText());
                //TODO: Have a little animation between scene switch
                if (loginSuccess)
                    fxMain.switchToDashboard();
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, API.getError(), ButtonType.OK);
                    alert.showAndWait();
                }
            });


            System.out.println("pass");


        }
    }

    public void signup(ActionEvent e) {

        if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {

            try {
                //TODO: Replace this url with sign up page once it is completed
                Desktop.getDesktop().browse(new URI("http://www.yahoo.com"));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        }

    }


}





