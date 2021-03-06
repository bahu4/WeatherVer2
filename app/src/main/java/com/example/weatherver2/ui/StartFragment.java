package com.example.weatherver2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.weatherver2.HistoryLogic;
import com.example.weatherver2.R;
import com.example.weatherver2.Repository;
import com.example.weatherver2.data.Constants;
import com.example.weatherver2.data.RetrofitRequest;
import com.example.weatherver2.data.dataRoom.History;
import com.example.weatherver2.data.dataRoom.RoomRepository;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment implements Constants, RetrofitRequest.RetrofitCallback {

    private Button settings;
    private Button start;
    private Button cityList;
    private Button history;
    private Button mapBtn;
    private TextView dateView;
    private TextInputEditText cityName;
    private ImageView imageView;

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
        MapsFragment mapsFragment = new MapsFragment();
        RetrofitRequest.RetrofitCallback retroCall = this;
        StartFragment startFragment = this;

        initField(view);
        initDate();
        initWallpaper();

        Bundle bundle = getArguments();
        if (bundle != null) {
            cityName.setText(bundle.getString(CITY_NAME));
        }

        clickProcessing(settings, settingsFragment);
        clickProcessing(cityList, cityListFragment);
        clickProcessing(history, historyFragment);
        clickProcessing(mapBtn, mapsFragment);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitRequest retrofitRequest = new RetrofitRequest(cityName, startFragment, retroCall);
                retrofitRequest.initRetrofit();
                retrofitRequest.request();
            }
        });
        return view;
    }

    private void initWallpaper() {
        int hour = new GregorianCalendar().get(Calendar.HOUR);
        if (hour > 5 && hour < 23) {
            Picasso.get()
                    .load("https://media.fotki.com/2v2HtB1RMx9YC2F.jpg")
                    .resize(800, 800)
                    .centerCrop()
                    .into(imageView);
        } else {
            Picasso.get()
                    .load("https://media.fotki.com/2v2HB9ZMGx9YC2F.jpg")
                    .into(imageView);
        }
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
        imageView = view.findViewById(R.id.imageView);
        mapBtn = view.findViewById(R.id.mapBtn);
    }

    private void clickProcessing(Button btn, Fragment fragment) {
        btn.setOnClickListener((v) -> {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        });
    }

    @Override
    public void callingBack(float temp, String name, float windSpeed, float pressure, String weather, float windDir, String icon) {
        WeatherFragment weatherFragment = new WeatherFragment();
        FragmentManager fragmentManager = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString(TEMPERATURE, String.format("%.0f", temp));
        bundle.putString(WIND_SPEED, String.format("%.0f", windSpeed));
        bundle.putString(PRESSURE, String.format("%.0f", pressure));
        bundle.putString(WIND_DIRECTION, windDirection(windDir));
        bundle.putString(WEATHER_CONDITIONS, weather);
        bundle.putString(CITY_NAME, name.substring(0, 1).toUpperCase() + name.substring(1));
        bundle.putString(ICON, icon);
        weatherFragment.setArguments(bundle);
        addToDatabase(temp, name, windSpeed, pressure, weather, windDir);
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, weatherFragment).commit();
    }

    private String windDirection(float deg) {

        if (deg >= 20 && deg < 70) {
            return getString(R.string.northeastern);
        } else if (deg >= 70 && deg < 110) {
            return getString(R.string.eastern);
        } else if (deg >= 110 && deg < 160) {
            return getString(R.string.southeastern);
        } else if (deg >= 160 && deg < 200) {
            return getString(R.string.southern);
        } else if (deg >= 200 && deg < 250) {
            return getString(R.string.southwestern);
        } else if (deg >= 250 && deg < 290) {
            return getString(R.string.western);
        } else if (deg >= 290 && deg < 340) {
            return getString(R.string.northwestern);
        } else {
            return getString(R.string.northern);
        }
    }

    private void addToDatabase(float temp, String name, float windSpeed, float pressure, String
            weather, float windDir) {
        Repository repository = new RoomRepository();
        HistoryLogic historyLogic = new HistoryLogic(repository);
        History history = new History();
        HistoryAdapter adapter = new HistoryAdapter(historyLogic.getRepository());
        historyLogic.setAdapter(adapter);
        history.setWeather(weather);
        history.setName(name.substring(0, 1).toUpperCase() + name.substring(1));
        history.setDate(DateFormat.getDateInstance().format(new Date()));
        history.setTemp(String.format("%.0f", temp) + " °C");
        history.setPressure(String.format("%.0f", pressure));
        history.setwSpeed(String.format("%.0f", windSpeed) + " " + windDirection(windDir));
        historyLogic.addHistory(history);
    }

    @Override
    public void errorDialog(int dialogId) {

        DialogBuilderFragment dialogBuilderFragment = new DialogBuilderFragment(dialogId, this);
        dialogBuilderFragment.show(getFragmentManager(), "dBuilder");
    }
}