package com.example.myweatherappadvanced.interfaces;

import com.example.myweatherappadvanced.inputdata.model.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherRetrofitAPI {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(
            @Query("q") String cityName,
            @Query("lang") String lang,
            @Query("units") String units,
            @Query("appid") String apiKey
    );
}
