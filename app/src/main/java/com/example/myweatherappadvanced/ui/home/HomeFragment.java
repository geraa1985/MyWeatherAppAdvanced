package com.example.myweatherappadvanced.ui.home;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.Fragment;

import com.example.myweatherappadvanced.MainActivity;
import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.calculate.Calculator;
import com.example.myweatherappadvanced.inputdata.City;
import com.example.myweatherappadvanced.settings.Settings;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    private TextView cityNameView;
    private TextView mainTemperatureView;
    private ImageView weatherImageView;
    private TextView weatherDescription;
    private TextView feelsLikeTempView;
    private TextView humidityView;
    private TextView pressureView;
    private TextView windSpeedView;
    private TextView windDirectView;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        findViews();
        if (MainActivity.getCurrentCityName() != null) {
            getCity(MainActivity.getCurrentCityName());
        } else {
            getCity(requireActivity().getResources().getStringArray(R.array.cities)[0]);
        }
    }

    private void findViews() {
        cityNameView = requireActivity().findViewById(R.id.cityName);
        mainTemperatureView = requireActivity().findViewById(R.id.mainTemperature);
        weatherImageView = requireActivity().findViewById(R.id.weatherImage);
        weatherDescription = requireActivity().findViewById(R.id.weatherDescription);
        feelsLikeTempView = requireActivity().findViewById(R.id.valueFeelsLike);
        humidityView = requireActivity().findViewById(R.id.valueHumidity);
        pressureView = requireActivity().findViewById(R.id.valuePressure);
        windSpeedView = requireActivity().findViewById(R.id.valueWindSpeed);
        windDirectView = requireActivity().findViewById(R.id.valueWindDirect);
    }

    @SuppressLint("SetTextI18n")
    public void getCity(String cityName) {
        new Thread(() -> {
            City city = new City(cityName);
            handler.post(() -> {
                MainActivity.setCurrentCityName(city.getName());
                cityNameView.setText(city.getName());
                Picasso.get().load(city.getWeatherImage()).into(weatherImageView);
                weatherDescription.setText(city.getDescription());
                humidityView.setText(city.getHumidity() + requireActivity().getResources().getString(R.string.percent));
                windDirectView.setText(city.getWindDirection() + requireActivity().getResources().getString(R.string.deg));

                if (Settings.getInstance().isF()) {
                    int mainTemp = Calculator.cToF(city.getMainTemperature());
                    int feelsLikeTemp = Calculator.cToF(city.getFeelsLikeTemp());
                    mainTemperatureView.setText(mainTemp + requireActivity().getResources().getString(R.string.deg_f));
                    feelsLikeTempView.setText(feelsLikeTemp + requireActivity().getResources().getString(R.string.deg_f));
                } else {
                    mainTemperatureView.setText(city.getMainTemperature() + requireActivity().getResources().getString(R.string.deg_c));
                    feelsLikeTempView.setText(city.getFeelsLikeTemp() + requireActivity().getResources().getString(R.string.deg_c));
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
            });
        }).start();
    }
}