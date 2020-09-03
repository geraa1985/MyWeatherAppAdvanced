package com.example.myweatherappadvanced.inputdata;

import android.content.Context;

import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.calculate.Calculator;
import com.example.myweatherappadvanced.inputdata.model.WeatherRequest;
import com.example.myweatherappadvanced.settings.Settings;

public class CurrentCity {

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

    public CurrentCity(WeatherRequest city, Context context) {

        this.name = city.getName();
        Settings.getInstance().getCitiesList().remove(city.getName());
        Settings.getInstance().getCitiesList().addFirst(city.getName());


        this.imgUrl = "http://openweathermap.org/img/wn/" + city.getWeather()[0].getIcon() + "@2x.png";
        this.weatherDescription = city.getWeather()[0].getDescription();
        this.humidity = city.getMain().getHumidity() + context.getResources().getString(R.string.percent);
        this.windDirect = city.getWind().getDeg() + context.getResources().getString(R.string.deg);
        this.temp = Math.round(city.getMain().getTemp());

        if (Settings.getInstance().isF()) {
            long mainTemp = Calculator.cToF(Math.round(city.getMain().getTemp()));
            long feelsLikeTemp = Calculator.cToF(Math.round(city.getMain().getFeels_like()));
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
            if (Math.round(city.getMain().getFeels_like()) > 0) {
                this.feelsLikeTemp = "+" + Math.round(city.getMain().getFeels_like()) + context.getResources().getString(R.string.deg_c);
            } else {
                this.feelsLikeTemp = Math.round(city.getMain().getFeels_like()) + context.getResources().getString(R.string.deg_c);
            }
        }

        if (Settings.getInstance().isMM()) {
            long calcPress = Calculator.gPaToMm(city.getMain().getPressure());
            this.pressure = calcPress + " " + context.getResources().getString(R.string.mm);
        } else {
            this.pressure = city.getMain().getPressure() + " " + context.getResources().getString(R.string.gpa);
        }

        if (Settings.getInstance().isKMH()) {
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
