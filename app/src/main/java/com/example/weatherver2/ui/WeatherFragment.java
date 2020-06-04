package com.example.weatherver2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.weatherver2.HistoryLogic;
import com.example.weatherver2.R;
import com.example.weatherver2.Repository;
import com.example.weatherver2.data.Constants;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements Constants {

    private TextView weatherCityName;
    private TextView weatherTemperature;
    private TextView weatherConditions;
    private TextView weatherWindSpeed;
    private TextView weatherPressure;
    private Button weatherOkBtn;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        initWeatherField(view);
        Bundle bundle = getArguments();
        Date date = new Date();

        weatherTemperature.setText(bundle.getString(TEMPERATURE) + " °C");
        weatherWindSpeed.setText(bundle.getString(WIND_SPEED));
        weatherPressure.setText(String.format("%.4s", bundle.getString(PRESSURE)));
        weatherConditions.setText(bundle.getString(WEATHER_CONDITIONS));
        weatherCityName.setText(bundle.getString(CITY_NAME));

        btnWeatherClick(weatherOkBtn);
        return view;
    }

    private void initWeatherField(View view) {
        weatherCityName = view.findViewById(R.id.weatherCityName);
        weatherTemperature = view.findViewById(R.id.weatherTemperatureField);
        weatherConditions = view.findViewById(R.id.weatherConditionsField);
        weatherWindSpeed = view.findViewById(R.id.weatherWindSpeedFielad);
        weatherPressure = view.findViewById(R.id.weatherPressureField);
        weatherOkBtn = view.findViewById(R.id.weatherOkBtn);
    }

    private void btnWeatherClick(Button btn) {
        btn.setOnClickListener((v) -> {
            StartFragment startFragment = new StartFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, startFragment)
                    .commit();
        });
    }
}
