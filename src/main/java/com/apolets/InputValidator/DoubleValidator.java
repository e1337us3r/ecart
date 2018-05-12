package com.apolets.InputValidator;

import com.apolets.main.fxMain;

public class DoubleValidator extends com.jfoenix.validation.DoubleValidator {

    public DoubleValidator() {
        setMessage(fxMain.languageBundle.getString("validator.number"));
    }
}
