package com.apolets.InputValidator;

import com.apolets.main.fxMain;
import com.jfoenix.validation.RequiredFieldValidator;

public class RequiredValidator extends RequiredFieldValidator {

    //TODO: Localize these
    public RequiredValidator() {
        setMessage(fxMain.languageBundle.getString("validator.required"));

    }

}
