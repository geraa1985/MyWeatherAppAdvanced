package com.example.myweatherappadvanced.ui.add;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.interfaces.OnFragmentInteractionListener;
import com.example.myweatherappadvanced.ui.list.CitiesListFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class AddCity extends Fragment {

    private TextInputEditText editText;
    private MaterialButton button;
    private Pattern newCityRules = Pattern.compile("^[A-ZА-ЯЁ\\s][a-zа-яё\\s]{2,}$+|");
    private String newCityName;
    private boolean isValid;
    private OnFragmentInteractionListener mListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_city, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        findViews();
        checkCityField();
        setOnClickBehaviourToOK();
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

    private void findViews() {
        editText = requireActivity().findViewById(R.id.enterCityInput);
        button = requireActivity().findViewById(R.id.buttonOk);
    }

    private void setOnClickBehaviourToOK() {
        button.setOnClickListener((v) -> {
            editText.clearFocus();
            if (newCityName != null) {
                if (isValid) {
                    mListener.onFragmentInteraction(newCityName);
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
