package com.apolets.Controllers;

import com.apolets.InputValidator.EmailValidator;
import com.apolets.InputValidator.PasswordValidator;
import com.apolets.InputValidator.RequiredValidator;
import com.apolets.main.fxMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
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
        if (jf_password.validate() && jf_email.validate()) {
            System.out.println(jf_email.getText() + " logged in with password: " + jf_password.getText());
            fxMain.exitLogin();
        } else {

        }
    }

    public void signup(ActionEvent e) {

        if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {

            try {
                Desktop.getDesktop().browse(new URI("http://www.yahoo.com"));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        }

    }


}





