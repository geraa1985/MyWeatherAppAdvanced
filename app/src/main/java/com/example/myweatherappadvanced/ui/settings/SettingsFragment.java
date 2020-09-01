package com.example.myweatherappadvanced.ui.settings;

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
import com.example.myweatherappadvanced.settings.Settings;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

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
        degC.setChecked(!Settings.getInstance().isF());
        degF.setChecked(Settings.getInstance().isF());
        gPa.setChecked(!Settings.getInstance().isMM());
        mm.setChecked(Settings.getInstance().isMM());
        ms.setChecked(!Settings.getInstance().isKMH());
        kmh.setChecked(Settings.getInstance().isKMH());
        day.setChecked(!Settings.getInstance().isNight());
        night.setChecked(Settings.getInstance().isNight());
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
                    Settings.getInstance().setF(false);
                    break;
                case R.id.degF:
                    Settings.getInstance().setF(true);
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
                    Settings.getInstance().setMM(false);
                    break;
                case R.id.mm:
                    Settings.getInstance().setMM(true);
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
                    Settings.getInstance().setKMH(false);
                    break;
                case R.id.kmh:
                    Settings.getInstance().setKMH(true);
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
                    Settings.getInstance().setNight(false);
                    requireActivity().recreate();
                    break;
                case R.id.night:
                    Settings.getInstance().setNight(true);
                    requireActivity().recreate();
                    break;
                default:
                    break;
            }
        });
    }
}