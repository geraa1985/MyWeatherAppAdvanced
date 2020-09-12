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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.customview.Thermometer;
import com.example.myweatherappadvanced.db.CityDB;
import com.example.myweatherappadvanced.inputdata.OpenWeatherNetwork;
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

    private Thermometer thermometer;

    OpenWeatherNetwork openWeatherNetwork = OpenWeatherNetwork.getInstance();

    private ImageView yandexImage;
    private ImageView wikiImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        findViews();
        fab.setVisibility(View.GONE);

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

        thermometer = requireActivity().findViewById(R.id.thermometer);
    }

    public void getCity(String cityName, Context context) {
        openWeatherNetwork.requestRetrofit(cityName, context, this);
    }

    public void setWeather() {
        cityNameView.setText(openWeatherNetwork.getCurrentCity().getName());
        mainTemperatureView.setText(openWeatherNetwork.getCurrentCity().getTemperature());
        Picasso.get().load(openWeatherNetwork.getCurrentCity().getImgUrl()).into(weatherImageView);
        feelsLikeTempView.setText(openWeatherNetwork.getCurrentCity().getFeelsLikeTemp());
        weatherDescription.setText(openWeatherNetwork.getCurrentCity().getWeatherDescription());
        humidityView.setText(openWeatherNetwork.getCurrentCity().getHumidity());
        pressureView.setText(openWeatherNetwork.getCurrentCity().getPressure());
        windSpeedView.setText(openWeatherNetwork.getCurrentCity().getWindSpeed());
        windDirectView.setText(openWeatherNetwork.getCurrentCity().getWindDirect());
        thermometer.setTemperature(openWeatherNetwork.getCurrentCity().getTemp());

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LastCity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LastCity", cityNameView.getText().toString());
        editor.apply();
    }

    public CityDB setCityDB() {
        CityDB cityDB = new CityDB();

        cityDB.name = cityNameView.getText().toString();
        cityDB.temperature = mainTemperatureView.getText().toString();

        return cityDB;
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