package com.example.myweatherappadvanced.ui.weather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.calculate.Calculator;
import com.example.myweatherappadvanced.inputdata.City;
import com.example.myweatherappadvanced.settings.Settings;
import com.example.myweatherappadvanced.ui.add.AddCity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class WeatherFragment extends Fragment {
    private TextView cityNameView;
    private TextView mainTemperatureView;
    private ImageView weatherImageView;
    private TextView weatherDescription;
    private TextView feelsLikeTempView;
    private TextView humidityView;
    private TextView pressureView;
    private TextView windSpeedView;
    private TextView windDirectView;
    private FloatingActionButton fab;

    private ImageView yandexImage;
    private ImageView wikiImage;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        findViews();
        fab.setVisibility(View.GONE);

        if (!Settings.getInstance().isCityFromList()) {
            getCity(Settings.getInstance().getCitiesList().get(0));
        }
        Settings.getInstance().setCityFromList(false);

        onClickYandex();
        onClickWiki();
    }


    private void findViews() {
        fab = requireActivity().findViewById(R.id.fab);

        cityNameView = requireActivity().findViewById(R.id.cityName);
        mainTemperatureView = requireActivity().findViewById(R.id.mainTemperature);
        weatherImageView = requireActivity().findViewById(R.id.weatherImage);
        weatherDescription = requireActivity().findViewById(R.id.weatherDescription);
        feelsLikeTempView = requireActivity().findViewById(R.id.valueFeelsLike);
        humidityView = requireActivity().findViewById(R.id.valueHumidity);
        pressureView = requireActivity().findViewById(R.id.valuePressure);
        windSpeedView = requireActivity().findViewById(R.id.valueWindSpeed);
        windDirectView = requireActivity().findViewById(R.id.valueWindDirect);

        yandexImage = requireActivity().findViewById(R.id.yandexImage);
        wikiImage = requireActivity().findViewById(R.id.wikiImage);
    }

    @SuppressLint("SetTextI18n")
    public void getCity(String cityName) {
        new Thread(() -> {
            City city = new City(cityName);
            handler.post(() -> {

                Picasso.get().load(city.getWeatherImage()).into(weatherImageView);
                weatherDescription.setText(city.getDescription());
                humidityView.setText(city.getHumidity() + requireActivity().getResources().getString(R.string.percent));
                windDirectView.setText(city.getWindDirection() + requireActivity().getResources().getString(R.string.deg));

                if (Settings.getInstance().isF()) {
                    int mainTemp = Calculator.cToF(city.getMainTemperature());
                    int feelsLikeTemp = Calculator.cToF(city.getFeelsLikeTemp());
                    if (mainTemp > 0) {
                        mainTemperatureView.setText("+" + mainTemp + requireActivity().getResources().getString(R.string.deg_f));
                    } else {
                        mainTemperatureView.setText(mainTemp + requireActivity().getResources().getString(R.string.deg_f));
                    }
                    if (feelsLikeTemp > 0) {
                        feelsLikeTempView.setText("+" + feelsLikeTemp + requireActivity().getResources().getString(R.string.deg_f));
                    } else {
                        feelsLikeTempView.setText(feelsLikeTemp + requireActivity().getResources().getString(R.string.deg_f));
                    }
                } else {
                    if (city.getMainTemperature() > 0) {
                        mainTemperatureView.setText("+" + city.getMainTemperature() + requireActivity().getResources().getString(R.string.deg_c));
                    } else {
                        mainTemperatureView.setText(city.getMainTemperature() + requireActivity().getResources().getString(R.string.deg_c));
                    }
                    if (city.getFeelsLikeTemp() > 0) {
                        feelsLikeTempView.setText("+" + city.getFeelsLikeTemp() + requireActivity().getResources().getString(R.string.deg_c));
                    } else {
                        feelsLikeTempView.setText(city.getFeelsLikeTemp() + requireActivity().getResources().getString(R.string.deg_c));
                    }
                }

                if (Settings.getInstance().isMM()) {
                    int pressure = Calculator.gPaToMm(city.getPressure());
                    pressureView.setText(pressure + " " + requireActivity().getResources().getString(R.string.mm));
                } else {
                    pressureView.setText(city.getPressure() + " " + requireActivity().getResources().getString(R.string.gpa));
                }

                if (Settings.getInstance().isKMH()) {
                    int windSpeed = Calculator.msToKmh(city.getWindSpeed());
                    windSpeedView.setText(windSpeed + " " + requireActivity().getResources().getString(R.string.km_h));
                } else {
                    windSpeedView.setText(city.getWindSpeed() + " " + requireActivity().getResources().getString(R.string.m_s));
                }

                if (city.getException() == null) {
                    cityNameView.setText(city.getName());
                    Settings.getInstance().getCitiesList().remove(city.getName());
                    Settings.getInstance().getCitiesList().addFirst(city.getName());
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setTitle(R.string.error_title)
                            .setCancelable(false)
                            .setMessage(city.getName() + getString(R.string.errorMessage))
                            .setPositiveButton(R.string.ok, (dialogInterface, i) -> new AddCity().show(requireActivity().getSupportFragmentManager(), "AddCityDialog"));
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }).start();
    }

    private void onClickYandex() {
        yandexImage.setOnClickListener(view -> {
            String info = "https://yandex.ru/pogoda/" + cityNameView.getText().toString();
            Uri uri = Uri.parse(info);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    private void onClickWiki() {
        wikiImage.setOnClickListener(view -> {
            String info = "https://ru.wikipedia.org/wiki/" + cityNameView.getText().toString();
            Uri uri = Uri.parse(info);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
}