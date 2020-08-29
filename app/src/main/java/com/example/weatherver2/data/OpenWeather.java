package com.example.weatherver2.data;

import com.example.weatherver2.data.weather.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {
    @GET("data/2.5/weather?")
    Call<WeatherRequest> loadWeather(@Query("q") String cityCountry, @Query("units") String metric, @Query("appid") String keyApi);

    @GET("data/2.5/weather?")
    Call<WeatherRequest> loadWeatherFromCoord(@Query("lat") String latitude, @Query("lon") String longitude, @Query("units") String metric, @Query("appid") String keyApi);
}
