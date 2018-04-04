package ru.devsp.app.mtgcollections.view.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Перевод фокуса
 * Created by gen on 11.10.2017.
 */

public class SetTextWatcher implements TextWatcher {

    private EditText mViewToRequestFocus;

    public SetTextWatcher(EditText viewToRequestFocus) {
        mViewToRequestFocus = viewToRequestFocus;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //not used
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mViewToRequestFocus != null && charSequence.length() == 7) {
            mViewToRequestFocus.requestFocus();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //not used
    }
}
