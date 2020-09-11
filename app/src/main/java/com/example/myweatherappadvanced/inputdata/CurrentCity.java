package com.example.myweatherappadvanced.inputdata;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.calculate.Calculator;
import com.example.myweatherappadvanced.inputdata.model.WeatherRequest;

public class CurrentCity {

    private static CurrentCity instance;

    private String name;
    private String temperature;
    private String imgUrl;
    private String feelsLikeTemp;
    private String weatherDescription;
    private String humidity;
    private String pressure;
    private String windSpeed;
    private String windDirect;
    private long temp;

    private CurrentCity() {}

    public static CurrentCity getInstance() {
        if (instance == null){
            instance = new CurrentCity();
        }
        return instance;
    }

    public void setCurrentCity(WeatherRequest city, Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);

        this.name = city.getName();

        this.imgUrl = "http://openweathermap.org/img/wn/" + city.getWeather().get(0).getIcon() + "@2x.png";
        this.weatherDescription = city.getWeather().get(0).getDescription();
        this.humidity = city.getMain().getHumidity() + context.getResources().getString(R.string.percent);
        this.windDirect = city.getWind().getDeg() + context.getResources().getString(R.string.deg);
        this.temp = Math.round(city.getMain().getTemp());

        if (sharedPreferences.getBoolean("isF", false)) {
            long mainTemp = Calculator.cToF(city.getMain().getTemp());
            long feelsLikeTemp = Calculator.cToF(city.getMain().getFeelsLike());
            if (mainTemp > 0) {
                this.temperature = "+" + mainTemp + context.getResources().getString(R.string.deg_f);
            } else {
                this.temperature = mainTemp + context.getResources().getString(R.string.deg_f);
            }
            if (feelsLikeTemp > 0) {
                this.feelsLikeTemp = "+" + feelsLikeTemp + context.getResources().getString(R.string.deg_f);
            } else {
                this.feelsLikeTemp = feelsLikeTemp + context.getResources().getString(R.string.deg_f);
            }
        } else {
            if (Math.round(city.getMain().getTemp()) > 0) {
                this.temperature = "+" + Math.round(city.getMain().getTemp()) + context.getResources().getString(R.string.deg_c);
            } else {
                this.temperature = Math.round(city.getMain().getTemp()) + context.getResources().getString(R.string.deg_c);
            }
            if (Math.round(city.getMain().getFeelsLike()) > 0) {
                this.feelsLikeTemp = "+" + Math.round(city.getMain().getFeelsLike()) + context.getResources().getString(R.string.deg_c);
            } else {
                this.feelsLikeTemp = Math.round(city.getMain().getFeelsLike()) + context.getResources().getString(R.string.deg_c);
            }
        }

        if (sharedPreferences.getBoolean("isMM", false)) {
            long calcPress = Calculator.gPaToMm(city.getMain().getPressure());
            this.pressure = calcPress + " " + context.getResources().getString(R.string.mm);
        } else {
            this.pressure = city.getMain().getPressure() + " " + context.getResources().getString(R.string.gpa);
        }

        if (sharedPreferences.getBoolean("isKMH", false)) {
            long calcWindSpeed = Calculator.msToKmh(city.getWind().getSpeed());
            this.windSpeed = calcWindSpeed + " " + context.getResources().getString(R.string.km_h);
        } else {
            this.windSpeed = Math.round(city.getWind().getSpeed()) + " " + context.getResources().getString(R.string.m_s);
        }
    }

    public String getName() {
        return name;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getFeelsLikeTemp() {
        return feelsLikeTemp;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindDirect() {
        return windDirect;
    }

    public long getTemp() {
        return temp;
    }
}
