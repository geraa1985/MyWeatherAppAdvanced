package com.example.myweatherappadvanced.ui.add;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.interfaces.OnFragmentInteractionListener;
import com.example.myweatherappadvanced.ui.list.CitiesListFragment;
import com.example.myweatherappadvanced.ui.weather.WeatherFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class AddCity extends BottomSheetDialogFragment {

    private TextInputEditText editText;
    private MaterialButton button;
    private Pattern newCityRules = Pattern.compile("^[A-ZА-ЯЁ\\s][a-zа-яё\\s]{2,}$+|");
    private String newCityName;
    private boolean isValid;
    private OnFragmentInteractionListener mListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_city, container, false);

        editText = view.findViewById(R.id.enterCityInput);
        button = view.findViewById(R.id.buttonOk);
        checkCityField();
        setOnClickBehaviourToOK();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    private void setOnClickBehaviourToOK() {
        button.setOnClickListener((v) -> {
            editText.clearFocus();
            if (newCityName != null) {
                if (isValid) {
                    dismiss();
                    if (requireActivity().getSupportFragmentManager().getFragments().get(0).getClass() == WeatherFragment.class) {
                        WeatherFragment fragment = (WeatherFragment) requireActivity().getSupportFragmentManager().getFragments().get(0);
                        fragment.getCity(newCityName);
                    } else {
                        mListener.onFragmentInteraction(newCityName);
                    }
                }
            } else {
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new CitiesListFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void checkCityField() {
        editText.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                TextView inputText = (TextView) view;
                isValid = validate(inputText, newCityRules);
            }
        });
    }

    private boolean validate(TextView inputText, Pattern newCityRules) {
        String value = inputText.getText().toString();
        if (!value.equals("")) {
            newCityName = value;
            if (newCityRules.matcher(value).matches()) {
                inputText.setError(null);
                return true;
            } else {
                inputText.setError(getString(R.string.wrong_name_message));
                return false;
            }
        } else return true;
    }
}
