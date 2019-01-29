package com.example.lenovo.recipes.fragmentsUtile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.NumberPicker;

public class ServingNumberPickerDialog extends DialogFragment {

    public ServingNumberPickerDialog() {

    }

    private NumberPicker.OnValueChangeListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        assert getArguments() != null;
        int mCurrentValue = getArguments().getInt("value");

        final NumberPicker numberPicker = new NumberPicker(getActivity());
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(50);
        numberPicker.setValue(mCurrentValue);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Servings");

        builder.setPositiveButton("Set", (dialog, which) -> listener.onValueChange(numberPicker, numberPicker.getValue(),
                numberPicker.getValue()));

        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });
        builder.setView(numberPicker);
        return builder.create();

    }

    public void setListener(NumberPicker.OnValueChangeListener changeListener) {
        this.listener = changeListener;
    }

}
