package com.example.myweatherappadvanced.inputdata;

import android.content.Context;

import com.example.myweatherappadvanced.BuildConfig;
import com.example.myweatherappadvanced.inputdata.model.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class OpenWeatherNetwork {

    private Exception exception;

    private CurrentCity currentCity;

    private String lang = Locale.getDefault().getISO3Language().substring(0, 2);

    public void getWeather(String name, Context context) {
        try {
            final URL uri = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + name + "&lang=" + lang + "&units=metric&appid=" + BuildConfig.WEATHER_API_KEY);
            HttpsURLConnection urlConnection = null;
            try {
                urlConnection = (HttpsURLConnection) uri.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String result = getLines(in);
                Gson gson = new Gson();
                WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                this.currentCity = new CurrentCity(weatherRequest, context);
            } catch (Exception e) {
                this.exception = e;
                e.printStackTrace();
            } finally {
                if (null != urlConnection) {
                    urlConnection.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static String getLines(BufferedReader reader) {
        StringBuilder rawData = new StringBuilder(1024);

        while (true) {
            try {
                String tempVariable = reader.readLine();
                if (tempVariable == null) break;
                rawData.append(tempVariable).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawData.toString();
    }

    public CurrentCity getCurrentCity() {
        return currentCity;
    }

    public Exception getException() {
        return exception;
    }
}
