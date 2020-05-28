package com.example.weatherver2.data;

import com.example.weatherver2.data.weather.WeatherRequest;
import com.example.weatherver2.ui.LoginInterceptor;
import com.example.weatherver2.ui.StartFragment;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest implements Constants {

    OpenWeather openWeather;
    TextInputEditText cityName;
    StartFragment startFragment;
    Retrofit retrofit;

    public RetrofitRequest(TextInputEditText cityName, StartFragment startFragment) {
        this.cityName = cityName;
        this.startFragment = startFragment;
    }

    public void initRetrofit() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new LoginInterceptor())
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            openWeather = retrofit.create(OpenWeather.class);
        }
    }

    public void request() {
        String city = cityName.getText().toString();
        openWeather.loadWeather(city, "metric", API_KEY)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                float result = response.body().getMain().getTemp();
                                String name = response.body().getName();
                                float windSpeed = response.body().getWind().getSpeed();
                                float pressure = response.body().getMain().getPressure();
                                String weather = response.body().getWeather()[0].getDescription();
                                startFragment.callback(result, name, windSpeed, pressure, weather);
                            }
                            if (!response.isSuccessful() && response.body() != null) {

                            }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                     
                    }
                });
    }
}
