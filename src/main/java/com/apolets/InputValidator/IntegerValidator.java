package com.apolets.InputValidator;

import com.apolets.main.fxMain;
import com.jfoenix.validation.NumberValidator;

public class IntegerValidator extends NumberValidator {

    public IntegerValidator() {
        setMessage(fxMain.languageBundle.getString("validator.number"));
    }

}
