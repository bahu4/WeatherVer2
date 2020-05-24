package com.example.weatherver2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.weatherver2.MainActivity;
import com.example.weatherver2.R;
import com.example.weatherver2.data.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements Constants {

    private SwitchCompat switchDarkTheme;
    private CheckBox weatherBox;
    private CheckBox windBox;
    private CheckBox pressureBox;
    private Button okSettingsBtn;
    private Button cancelSettingsBtn;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        initSettingsField(view);

        btnSettingsCancelClick(cancelSettingsBtn);

        okSettingsBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(SWITCH_DARK, switchDarkTheme.isChecked());
            intent.putExtra(WEATHER_BOX, weatherBox.isChecked());
            intent.putExtra(WIND_BOX, windBox.isChecked());
            intent.putExtra(PRESSURE_BOX, pressureBox.isChecked());
            startActivity(intent);
        });
        return view;
    }

    private void initSettingsField(View view) {
        switchDarkTheme = view.findViewById(R.id.themeSwitch);
        weatherBox = view.findViewById(R.id.weatherBox);
        windBox = view.findViewById(R.id.windBox);
        pressureBox = view.findViewById(R.id.pressureBox);
        okSettingsBtn = view.findViewById(R.id.okSettingsBtn);
        cancelSettingsBtn = view.findViewById(R.id.cancelSettingsBtn);
    }

    private void btnSettingsCancelClick(Button btn) {
        btn.setOnClickListener((v) -> {
            StartFragment startFragment = new StartFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, startFragment).commit();
        });
    }
}