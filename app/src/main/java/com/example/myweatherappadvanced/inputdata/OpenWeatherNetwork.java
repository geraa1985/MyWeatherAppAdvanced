package com.example.myweatherappadvanced.inputdata;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.myweatherappadvanced.App;
import com.example.myweatherappadvanced.BuildConfig;
import com.example.myweatherappadvanced.db.AppDatabase;
import com.example.myweatherappadvanced.db.CityDAO;
import com.example.myweatherappadvanced.db.CityDB;
import com.example.myweatherappadvanced.inputdata.model.WeatherRequest;
import com.example.myweatherappadvanced.interfaces.OpenWeatherRetrofitAPI;
import com.example.myweatherappadvanced.ui.weather.WeatherFragment;

import java.io.IOException;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
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

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient().build())
            .build();


    public void requestRetrofit(String cityName, Context context, WeatherFragment fragment) {
        OpenWeatherRetrofitAPI openWeatherRetrofitAPI = retrofit.create(OpenWeatherRetrofitAPI.class);
        String units = "metric";
        String apiKey = BuildConfig.WEATHER_API_KEY;

        openWeatherRetrofitAPI.loadWeather(cityName, lang, units, apiKey).enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(@NonNull Call<WeatherRequest> call,
                                   @NonNull Response<WeatherRequest> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        WeatherRequest weatherRequest = response.body();
                        currentCity.setCurrentCity(weatherRequest, context);
                        fragment.setWeather();
                        new Thread(() -> {
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
