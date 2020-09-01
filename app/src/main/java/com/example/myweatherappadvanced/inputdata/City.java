package com.example.myweatherappadvanced.inputdata;

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

public class City {

    private String name;
    private int mainTemperature;
    private String weatherImage;
    private String description;
    private int humidity;
    private int feelsLikeTemp;
    private int pressure;
    private int windSpeed;
    private int windDirection;

    private Exception exception;

    private String lang = Locale.getDefault().getISO3Language().substring(0, 2);

    public City(String name) {
        this.name = name;
        this.getWeather();
    }

    public void getWeather() {
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
                displayWeather(weatherRequest);
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

    private void displayWeather(WeatherRequest weatherRequest) {
        this.name = weatherRequest.getName();
        this.mainTemperature = Math.round(weatherRequest.getMain().getTemp());
        this.feelsLikeTemp = Math.round(weatherRequest.getMain().getFeels_like());
        this.weatherImage = "http://openweathermap.org/img/wn/" + weatherRequest.getWeather()[0].getIcon() + "@2x.png";
        this.description = weatherRequest.getWeather()[0].getDescription();
        this.humidity = weatherRequest.getMain().getHumidity();
        this.pressure = weatherRequest.getMain().getPressure();
        this.windSpeed = Math.round(weatherRequest.getWind().getSpeed());
        this.windDirection = weatherRequest.getWind().getDeg();
    }

    private String getLines(BufferedReader reader) {
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

    public String getName() {
        return name;
    }

    public int getMainTemperature() {
        return mainTemperature;
    }

    public String getWeatherImage() {
        return weatherImage;
    }

    public String getDescription() {
        return description;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getFeelsLikeTemp() {
        return feelsLikeTemp;
    }

    public int getPressure() {
        return pressure;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public Exception getException() {
        return exception;
    }

}
