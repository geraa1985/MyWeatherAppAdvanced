package com.example.myweatherappadvanced.ui.weather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myweatherappadvanced.MainActivity;
import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.databinding.FragmentWeatherBinding;
import com.example.myweatherappadvanced.inputdata.OpenWeatherNetwork;
import com.example.myweatherappadvanced.ui.add.AddCity;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding fragmentWeatherBinding;

    OpenWeatherNetwork openWeatherNetwork = OpenWeatherNetwork.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentWeatherBinding = FragmentWeatherBinding.inflate(inflater, container, false);
        return fragmentWeatherBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        MainActivity.getFab().setVisibility(View.GONE);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LastCity", Context.MODE_PRIVATE);
        String lastCity = sharedPreferences.getString("LastCity", "");
        if (Objects.equals(lastCity, "")) {
            new AddCity().show(requireActivity().getSupportFragmentManager(),
                    "AddCityDialog");
        } else {
            getCity(lastCity, requireActivity());
        }

        onClickYandex();
        onClickWiki();
    }

    public void getCity(String cityName, Context context) {
        openWeatherNetwork.requestRetrofit(cityName, context, this);
    }

    public void setWeather() {
        fragmentWeatherBinding.cityName.setText(openWeatherNetwork.getCurrentCity().getName());
        fragmentWeatherBinding.mainTemperature.setText(openWeatherNetwork.getCurrentCity().getTemperature());
        Picasso.get().load(openWeatherNetwork.getCurrentCity().getImgUrl()).into(fragmentWeatherBinding.weatherImage);
        fragmentWeatherBinding.feelsLike.setText(openWeatherNetwork.getCurrentCity().getFeelsLikeTemp());
        fragmentWeatherBinding.weatherDescription.setText(openWeatherNetwork.getCurrentCity().getWeatherDescription());
        fragmentWeatherBinding.valueHumidity.setText(openWeatherNetwork.getCurrentCity().getHumidity());
        fragmentWeatherBinding.valuePressure.setText(openWeatherNetwork.getCurrentCity().getPressure());
        fragmentWeatherBinding.valueWindSpeed.setText(openWeatherNetwork.getCurrentCity().getWindSpeed());
        fragmentWeatherBinding.valueWindDirect.setText(openWeatherNetwork.getCurrentCity().getWindDirect());
        fragmentWeatherBinding.thermometer.setTemperature(openWeatherNetwork.getCurrentCity().getTemp());
    }

    public void setErrorDialog(String errorMassage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.error_title)
                .setCancelable(false)
                .setMessage(errorMassage)
                .setPositiveButton(R.string.ok,
                        (dialogInterface, i) -> new AddCity().show(requireActivity().getSupportFragmentManager(),
                                "AddCityDialog"));
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void onClickYandex() {
        fragmentWeatherBinding.yandexImage.setOnClickListener(view -> {
            String info = "https://yandex.ru/pogoda/" + fragmentWeatherBinding.cityName.getText().toString();
            Uri uri = Uri.parse(info);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    private void onClickWiki() {
        fragmentWeatherBinding.wikiImage.setOnClickListener(view -> {
            String info =
                    "https://ru.wikipedia.org/wiki/" + fragmentWeatherBinding.cityName.getText().toString();
            Uri uri = Uri.parse(info);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
}