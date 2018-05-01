package com.apolets.InputValidator;

import com.apolets.main.fxMain;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.validation.base.ValidatorBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator extends ValidatorBase {

    private static final String PASSWORD_PATTERN =
            "^[-_\\.\\da-zA-Z]{6,20}$";
    private Pattern pattern;

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
        setMessage(fxMain.languageBundle.getString("validator.password"));
    }

    @Override
    protected void eval() {
        //TODO: Localize these
        JFXPasswordField field = (JFXPasswordField) srcControl.get();
        if (!validate(field.getText())) {
            hasErrors.set(true);
            field.getStyleClass().remove(field.getStyleClass().size() - 1);
            field.getStyleClass().add("fieldfail");
        } else {
            hasErrors.set(false);
            field.getStyleClass().remove(field.getStyleClass().size() - 1);
            field.getStyleClass().add("fieldpass");
        }

    }


    public boolean validate(String hex) {

        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }


}
