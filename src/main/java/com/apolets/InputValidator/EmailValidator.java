package com.apolets.InputValidator;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.base.ValidatorBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator extends ValidatorBase {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern;

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    protected void eval() {

        JFXTextField field = (JFXTextField) srcControl.get();
        if (!validate(field.getText())) {
            hasErrors.set(true);
            setMessage("Incorrect email format");
            field.getStyleClass().remove(field.getStyleClass().size() - 1);
            field.getStyleClass().add("fieldfail");
            //System.out.println(field.getStyleClass());
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
