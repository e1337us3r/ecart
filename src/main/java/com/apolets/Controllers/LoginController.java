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
import javafx.concurrent.Task;
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


            new Thread(new Task<Void>() {

                @Override
                protected Void call() {
                    boolean loginSuccess = API.loginRequest(jf_email.getText(), jf_password.getText());

                    Platform.runLater(() -> {
                        if (loginSuccess) {
                            fxMain.switchToDashboard();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, API.getError(), ButtonType.OK);
                            alert.showAndWait();
                        }
                    });

                    return null;
                }
            }).start();


        }
    }

    public void signup(ActionEvent e) {

        if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {

            try {
                Desktop.getDesktop().browse(new URI("http://www.shop.apolets.com/signup.php"));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        }

    }


}





