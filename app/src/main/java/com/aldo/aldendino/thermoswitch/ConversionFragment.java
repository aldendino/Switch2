package com.aldo.aldendino.thermoswitch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ConversionFragment extends Fragment {

    private static final String INPUT_STATE = "inputState";
    private static final String OUTPUT_STATE = "outputState";
    private static final String TYPE_STATE = "typeState";
    private static final String DIRECTION_STATE = "directionState";

    private EditText inputEditText;
    private EditText outputEditText;
    private TextView inputTextView;
    private TextView outputTextView;

    private Converter.ConversionType type;
    private boolean direction = true;
    private DecimalFormat decimalFormat = new DecimalFormat("0.0");

    private String inputHint = null;
    private String inputLabel = null;
    private String outputHint = null;
    private String outputLabel = null;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.type = Converter.ConversionType.valueOf(args.getString(MainActivity.TYPE_KEY));

    }

    private void setValues(Context context) {
        switch (type) {
            case TEMPERATURE:
                inputHint = context.getResources().getString(R.string.celsius);
                inputLabel = context.getResources().getString(R.string.celsius_label);
                outputHint = context.getResources().getString(R.string.fahrenheit);
                outputLabel = context.getResources().getString(R.string.fahrenheit_label);
                break;
            case DISTANCE:
                inputHint = context.getResources().getString(R.string.kilometers);
                inputLabel = context.getResources().getString(R.string.kilometers_label);
                outputHint = context.getResources().getString(R.string.miles);
                outputLabel = context.getResources().getString(R.string.miles_label);
                break;
            case WEIGHT:
                inputHint = context.getResources().getString(R.string.kilograms);
                inputLabel = context.getResources().getString(R.string.kilograms_label);
                outputHint = context.getResources().getString(R.string.pounds);
                outputLabel = context.getResources().getString(R.string.pounds_label);
                break;
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_main, container, false);

        inputEditText = (EditText) rootView.findViewById(R.id.inputEditText);
        outputEditText = (EditText) rootView.findViewById(R.id.outputEditText);
        inputTextView = (TextView) rootView.findViewById(R.id.inputTextView);
        outputTextView = (TextView) rootView.findViewById(R.id.outputTextView);

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                display();
            }
        });

        final Context context = rootView.getContext();

        outputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(outputEditText.getWindowToken(), 0);
                }
            }
        });

        if(savedInstanceState != null) {
            inputEditText.setText(savedInstanceState.getString(INPUT_STATE));
            outputEditText.setText(savedInstanceState.getString(OUTPUT_STATE));
            type = Converter.ConversionType.valueOf(savedInstanceState.getString(TYPE_STATE));
            direction = savedInstanceState.getBoolean(DIRECTION_STATE);
        }

        if(type != null) {
            setValues(rootView.getContext());
        }

        update();
        inputEditText.requestFocus();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(INPUT_STATE, inputEditText.getText().toString());
        outState.putString(OUTPUT_STATE, outputEditText.getText().toString());
        outState.putString(TYPE_STATE, type.name());
        outState.putBoolean(DIRECTION_STATE, direction);
        super.onSaveInstanceState(outState);
    }

    private void display() {
        String input = inputEditText.getText().toString();
        if(!input.equals("")) {
            try {
                double inputTemp = Double.parseDouble(input);
                double outputTemp = convert(inputTemp);
                outputEditText.setText(decimalFormat.format(outputTemp));
            } catch(NullPointerException | NumberFormatException npe) {
                outputEditText.setText("");
            }
        } else {
            outputEditText.setText("");
        }
    }

    private Double convert(Double input) {
        Double output = null;
        switch (type) {
            case TEMPERATURE:
                output = Converter.convertTempurature(input, direction);
                break;
            case DISTANCE:
                output = Converter.convertDistance(input, direction);
                break;
            case WEIGHT:
                output = Converter.convertWeight(input, direction);
                break;
            default:
                break;
        }
        return output;
    }

    public void switchDirection() {
        direction = !direction;
        update();
        display();
    }

    private void update() {
        setHints();
        setLabels();
    }

    private void setHints() {
        if(direction) {
            inputEditText.setHint(inputHint);
            outputEditText.setHint(outputHint);
        } else {
            inputEditText.setHint(outputHint);
            outputEditText.setHint(inputHint);
        }
    }

    private void setLabels() {
        if(direction) {
            inputTextView.setText(inputLabel);
            outputTextView.setText(outputLabel);
        } else {
            inputTextView.setText(outputLabel);
            outputTextView.setText(inputLabel);
        }
    }
}
