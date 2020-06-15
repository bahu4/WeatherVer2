package com.example.weatherver2.data;

import com.example.weatherver2.data.weather.WeatherRequest;
import com.example.weatherver2.ui.LoginInterceptor;
import com.example.weatherver2.ui.MapsFragment;
import com.example.weatherver2.ui.StartFragment;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest implements Constants {

    private OpenWeather openWeather;
    private TextInputEditText cityName;
    private StartFragment startFragment;
    private MapsFragment mapsFragment;
    private Retrofit retrofit;
    private RetrofitCallback retrofitCallback;
    String latitude;
    String longitude;

    public interface RetrofitCallback {
        void callingBack(float temp, String name, float windSpeed, float pressure, String weather, float windDir);

        void errorDialog(int dialogId);
    }

    public RetrofitRequest(TextInputEditText cityName, StartFragment startFragment, RetrofitCallback retrofitCallback) {
        this.cityName = cityName;
        this.startFragment = startFragment;
        this.retrofitCallback = retrofitCallback;
    }

    public RetrofitRequest(String latitude, String longitude, MapsFragment mapsFragment, RetrofitCallback retrofitCallback) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.mapsFragment = mapsFragment;
        this.retrofitCallback = retrofitCallback;
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
                            float temp = response.body().getMain().getTemp();
                            String name = response.body().getName();
                            float windSpeed = response.body().getWind().getSpeed();
                            float pressure = response.body().getMain().getPressure();
                            String weather = response.body().getWeather()[0].getDescription();
                            float windDir = response.body().getWind().getDeg();
                            retrofitCallback.callingBack(temp, name, windSpeed, pressure, weather, windDir);
                        }
                        if (!response.isSuccessful() && response.errorBody() != null) {
                            retrofitCallback.errorDialog(1);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        retrofitCallback.errorDialog(2);
                    }
                });
    }

    public void requestWithCoord() {
        openWeather.loadWeatherFromCoord(latitude, longitude, "metric", API_KEY)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            float temp = response.body().getMain().getTemp();
                            String name = response.body().getName();
                            float windSpeed = response.body().getWind().getSpeed();
                            float pressure = response.body().getMain().getPressure();
                            String weather = response.body().getWeather()[0].getDescription();
                            float windDir = response.body().getWind().getDeg();
                            retrofitCallback.callingBack(temp, name, windSpeed, pressure, weather, windDir);
                        }
                        if (!response.isSuccessful() && response.errorBody() != null) {
                            retrofitCallback.errorDialog(1);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        retrofitCallback.errorDialog(2);
                    }
                });
    }
}
