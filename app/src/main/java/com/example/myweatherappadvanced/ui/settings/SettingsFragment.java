package com.example.myweatherappadvanced.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myweatherappadvanced.MainActivity;
import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding fragmentSettingsBinding;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        return fragmentSettingsBinding.getRoot();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onStart() {
        super.onStart();

        sharedPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        MainActivity.getFab().setVisibility(View.GONE);
        getCheckedPositions();
        onChangeValuesListener();
    }

    private void getCheckedPositions() {
        fragmentSettingsBinding.degC.setChecked(sharedPreferences.getBoolean("isC", true));
        fragmentSettingsBinding.degF.setChecked(sharedPreferences.getBoolean("isF", false));
        fragmentSettingsBinding.gPa.setChecked(sharedPreferences.getBoolean("isGPA", true));
        fragmentSettingsBinding.mm.setChecked(sharedPreferences.getBoolean("isMM", false));
        fragmentSettingsBinding.ms.setChecked(sharedPreferences.getBoolean("isMS", true));
        fragmentSettingsBinding.kmh.setChecked(sharedPreferences.getBoolean("isKMH", false));
        fragmentSettingsBinding.day.setChecked(sharedPreferences.getBoolean("isDAY", true));
        fragmentSettingsBinding.night.setChecked(sharedPreferences.getBoolean("isNIGHT", false));
    }

    private void onChangeValuesListener() {
        onChangeTempValue();
        onChangePressValue();
        onChangeWindValue();
        onChangeThemeValue();
    }

    private void onChangeTempValue() {
        fragmentSettingsBinding.rgTemp.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.degC:
                    editor.putBoolean("isC", true);
                    editor.putBoolean("isF", false);
                    editor.apply();
                    break;
                case R.id.degF:
                    editor.putBoolean("isF", true);
                    editor.putBoolean("isC", false);
                    editor.apply();
                    break;
                default:
                    break;
            }
        });
    }

    private void onChangePressValue() {
        fragmentSettingsBinding.rgPress.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.gPa:
                    editor.putBoolean("isGPA", true);
                    editor.putBoolean("isMM", false);
                    editor.apply();
                    break;
                case R.id.mm:
                    editor.putBoolean("isMM", true);
                    editor.putBoolean("isGPA", false);
                    editor.apply();
                    break;
                default:
                    break;
            }
        });
    }

    private void onChangeWindValue() {
        fragmentSettingsBinding.rgWind.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.ms:
                    editor.putBoolean("isMS", true);
                    editor.putBoolean("isKMH", false);
                    editor.apply();
                    break;
                case R.id.kmh:
                    editor.putBoolean("isKMH", true);
                    editor.putBoolean("isMS", false);
                    editor.apply();
                    break;
                default:
                    break;
            }
        });
    }

    private void onChangeThemeValue() {
        fragmentSettingsBinding.rgTheme.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.day:
                    editor.putBoolean("isDAY", true);
                    editor.putBoolean("isNIGHT", false);
                    editor.apply();
                    requireActivity().recreate();
                    break;
                case R.id.night:
                    editor.putBoolean("isNIGHT", true);
                    editor.putBoolean("isDAY", false);
                    editor.apply();
                    requireActivity().recreate();
                    break;
                default:
                    break;
            }
        });
    }
}