package com.example.myweatherappadvanced.ui.add;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.databinding.FragmentAddCityBinding;
import com.example.myweatherappadvanced.interfaces.OnNewCityClick;
import com.example.myweatherappadvanced.ui.list.CitiesListFragment;
import com.example.myweatherappadvanced.ui.weather.WeatherFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.regex.Pattern;

public class AddCity extends BottomSheetDialogFragment {

    private FragmentAddCityBinding fragmentAddCityBinding;

    private Pattern newCityRules = Pattern.compile("^[A-ZА-ЯЁ\\s][a-zа-яё\\s]{2,}$+|");
    private String newCityName;
    private boolean isValid;
    private OnNewCityClick onNewCityClick;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentAddCityBinding = FragmentAddCityBinding.inflate(inflater, container, false);
        return fragmentAddCityBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        checkCityField();
        setOnClickBehaviourToOK();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onNewCityClick = (OnNewCityClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    private void setOnClickBehaviourToOK() {
        fragmentAddCityBinding.buttonOk.setOnClickListener((v) -> {
            fragmentAddCityBinding.enterCityInput.clearFocus();
            if (newCityName != null) {
                if (isValid) {
                    dismiss();

                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LastCity",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("LastCity", newCityName);
                    editor.apply();

                    FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, new WeatherFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            } else {
                FragmentTransaction fragmentTransaction =
                        requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new CitiesListFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void checkCityField() {
        fragmentAddCityBinding.enterCityInput.setOnFocusChangeListener((view, b) -> {
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
