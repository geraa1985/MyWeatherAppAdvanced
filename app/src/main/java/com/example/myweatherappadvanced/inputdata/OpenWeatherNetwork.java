package com.example.myweatherappadvanced.inputdata;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.myweatherappadvanced.BuildConfig;
import com.example.myweatherappadvanced.App;
import com.example.myweatherappadvanced.db.AppDatabase;
import com.example.myweatherappadvanced.db.CityDAO;
import com.example.myweatherappadvanced.db.CityDB;
import com.example.myweatherappadvanced.inputdata.model.WeatherRequest;
import com.example.myweatherappadvanced.interfaces.OpenWeatherRetrofitAPI;
import com.example.myweatherappadvanced.ui.weather.WeatherFragment;

import java.io.IOException;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherNetwork {

    private static OpenWeatherNetwork instance;

    private CurrentCity currentCity = CurrentCity.getInstance();

    private String lang = Locale.getDefault().getISO3Language().substring(0, 2);

    private static final String BASE_URL = "https://api.openweathermap.org/";

    private OpenWeatherNetwork() {}

    public static OpenWeatherNetwork getInstance() {
        if (instance == null) {
            instance = new OpenWeatherNetwork();
        }
        return instance;
    }

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public void requestRetrofit(String cityName, Context context, WeatherFragment fragment) {
        OpenWeatherRetrofitAPI openWeatherRetrofitAPI = retrofit.create(OpenWeatherRetrofitAPI.class);
        String units = "metric";
        String apiKey = BuildConfig.WEATHER_API_KEY;

        openWeatherRetrofitAPI.loadWeather(cityName, lang, units, apiKey).enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(@NonNull Call<WeatherRequest> call, @NonNull Response<WeatherRequest> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        WeatherRequest weatherRequest = response.body();
                        currentCity.setCurrentCity(weatherRequest, context);
                        fragment.setWeather();
                        new Thread(()->{
                            AppDatabase db = App.getInstance().getDatabase();
                            CityDAO cityDAO = db.cityDAO();
                            CityDB cityDB = new CityDB();
                            cityDB.name = currentCity.getName();
                            cityDB.temperature = currentCity.getTemp();
                            cityDAO.insert(cityDB);
                        }).start();
                    }
                } else {
                    ResponseBody errorBody = response.errorBody();
                    try {
                        if (errorBody != null) {
                            fragment.setErrorDialog(errorBody.string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherRequest> call, @NonNull Throwable t) {

            }
        });
    }

    public CurrentCity getCurrentCity() {
        return currentCity;
    }
}
