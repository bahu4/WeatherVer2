package com.example.weatherver2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.example.weatherver2.R;
import com.example.weatherver2.data.Constants;
import com.example.weatherver2.data.HttpRequest;
import com.example.weatherver2.data.weather.WeatherRequest;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment implements Constants, HttpRequest.CallbackRequest {

    private Button settings;
    private Button start;
    private Button cityList;
    private Button history;
    private TextView dateView;
    private TextInputEditText cityName;

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        SettingsFragment settingsFragment = new SettingsFragment();
        CityListFragment cityListFragment = new CityListFragment();
        HistoryFragment historyFragment = new HistoryFragment();

        HttpRequest.CallbackRequest callbackRequest = this;
        StartFragment startFragment = this;

        initField(view);
        initDate();

        Bundle bundle = getArguments();
        if (bundle != null) {
            cityName.setText(bundle.getString(CITY_NAME));
        }

        clickProcessing(settings, settingsFragment);
        clickProcessing(cityList, cityListFragment);
        clickProcessing(history, historyFragment);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpRequest httpRequest = new HttpRequest(callbackRequest, cityName, startFragment);
                httpRequest.request();
            }
        });
        return view;
    }

    private void initDate() {
        String currentDate = DateFormat.getDateInstance().format(new Date());
        dateView.setText(currentDate);
    }

    private void initField(View view) {
        settings = view.findViewById(R.id.settingsBtn);
        start = view.findViewById(R.id.startBtn);
        cityList = view.findViewById(R.id.cityListBtn);
        dateView = view.findViewById(R.id.dateViewSettings);
        cityName = view.findViewById(R.id.editCityName);
        history = view.findViewById(R.id.historyBtn);
    }

    private void clickProcessing(Button btn, Fragment fragment) {
        btn.setOnClickListener((v) -> {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        });
    }

    public void showDialog() {
        DialogBuilderFragment dialogBuilderFragment = new DialogBuilderFragment();
        dialogBuilderFragment.show(getFragmentManager(), "dBuilder");
    }

    @Override
    public void callback(WeatherRequest weatherRequest) {
        WeatherFragment weatherFragment = new WeatherFragment();
        FragmentManager fragmentManager = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString(TEMPERATURE, String.format("%.0f", weatherRequest.getMain().getTemp()));
        bundle.putString(WIND_SPEED, String.format("%d", weatherRequest.getWind().getSpeed()));
        bundle.putString(PRESSURE, String.format("%.0f", weatherRequest.getMain().getPressure()));
        bundle.putString(WEATHER_CONDITIONS, String.format("%s", weatherRequest.getClouds().getAll()));
        bundle.putString(CITY_NAME, cityName.getText().toString());
        weatherFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, weatherFragment).commit();
    }
}