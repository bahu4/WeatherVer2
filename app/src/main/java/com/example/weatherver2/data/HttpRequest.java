package com.example.weatherver2.data;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.weatherver2.data.weather.WeatherRequest;
import com.example.weatherver2.ui.StartFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequest implements Constants {

    CallbackRequest cbReq;
    TextInputEditText textInputEditText;
    StartFragment startFragment;

    public HttpRequest(CallbackRequest cbReq, TextInputEditText textInputEditText, StartFragment startFragment) {
        this.cbReq = cbReq;
        this.textInputEditText = textInputEditText;
        this.startFragment = startFragment;
    }

    public interface CallbackRequest {
        void callback(WeatherRequest weatherRequest);
    }

    public void request() {
        try {
            final URL uri = new URL(WEATHER_URL + textInputEditText.getText() + WEATHER_API_KEY);
            Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(10000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String result = getLines(in);
                        Gson gson = new Gson();
                        final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                cbReq.callback(weatherRequest);
                            }
                        });

                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                        startFragment.showDialog();
                    }
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                private String getLines(BufferedReader in) {
                    return in.lines().collect(Collectors.joining("\n"));
                }
            }).start();

        } catch (Exception e) {
            Log.e(TAG, "Fail url", e);
            e.printStackTrace();
        }
    }
}