package com.example.myweatherappadvanced.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myweatherappadvanced.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsFragment extends Fragment {

    private FloatingActionButton fab;

    private RadioGroup rgTemp;
    private RadioGroup rgPress;
    private RadioGroup rgWind;
    private RadioGroup rgTheme;

    private RadioButton degC;
    private RadioButton degF;
    private RadioButton gPa;
    private RadioButton mm;
    private RadioButton ms;
    private RadioButton kmh;
    private RadioButton day;
    private RadioButton night;

    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onStart() {
        super.onStart();

        sharedPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        findViews();
        fab.setVisibility(View.GONE);
        getCheckedPositions();
        onChangeValuesListener();

    }

    private void findViews() {
        fab = requireActivity().findViewById(R.id.fab);

        rgTemp = requireActivity().findViewById(R.id.rgTemp);
        rgPress = requireActivity().findViewById(R.id.rgPress);
        rgWind = requireActivity().findViewById(R.id.rgWind);
        rgTheme = requireActivity().findViewById(R.id.rgTheme);

        degC = requireActivity().findViewById(R.id.degC);
        degF = requireActivity().findViewById(R.id.degF);
        gPa = requireActivity().findViewById(R.id.gPa);
        mm = requireActivity().findViewById(R.id.mm);
        ms = requireActivity().findViewById(R.id.ms);
        kmh = requireActivity().findViewById(R.id.kmh);
        day = requireActivity().findViewById(R.id.day);
        night = requireActivity().findViewById(R.id.night);
    }

    private void getCheckedPositions() {
        degC.setChecked(sharedPreferences.getBoolean("isF", true));
        degF.setChecked(sharedPreferences.getBoolean("isF", false));
        gPa.setChecked(sharedPreferences.getBoolean("isMM", true));
        mm.setChecked(sharedPreferences.getBoolean("isMM", false));
        ms.setChecked(sharedPreferences.getBoolean("isKMH", true));
        kmh.setChecked(sharedPreferences.getBoolean("isKMH", false));
        day.setChecked(sharedPreferences.getBoolean("isNight", true));
        night.setChecked(sharedPreferences.getBoolean("isNight", false));
    }

    private void onChangeValuesListener() {
        onChangeTempValue();
        onChangePressValue();
        onChangeWindValue();
        onChangeThemeValue();
    }

    private void onChangeTempValue() {
        rgTemp.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.degC:
                    editor.putBoolean("isF", false);
                    editor.apply();
                    break;
                case R.id.degF:
                    editor.putBoolean("isF", true);
                    editor.apply();
                    break;
                default:
                    break;
            }
        });
    }

    private void onChangePressValue() {
        rgPress.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.gPa:
                    editor.putBoolean("isMM", false);
                    editor.apply();
                    break;
                case R.id.mm:
                    editor.putBoolean("isMM", true);
                    editor.apply();
                    break;
                default:
                    break;
            }
        });
    }

    private void onChangeWindValue() {
        rgWind.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.ms:
                    editor.putBoolean("isKMH", false);
                    editor.apply();
                    break;
                case R.id.kmh:
                    editor.putBoolean("isKMH", true);
                    editor.apply();
                    break;
                default:
                    break;
            }
        });
    }

    private void onChangeThemeValue() {
        rgTheme.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.day:
                    editor.putBoolean("isNight", false);
                    editor.apply();
                    requireActivity().recreate();
                    break;
                case R.id.night:
                    editor.putBoolean("isNight", true);
                    editor.apply();
                    requireActivity().recreate();
                    break;
                default:
                    break;
            }
        });
    }
}