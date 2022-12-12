package edu.northeastern.driversafebc;

import android.text.Editable;
import android.text.TextWatcher;

public class TextValidateListener implements TextWatcher {

    private final TextValidator validator;

    public TextValidateListener(TextValidator validator) {
        this.validator = validator;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        validator.validate();
    }

}
