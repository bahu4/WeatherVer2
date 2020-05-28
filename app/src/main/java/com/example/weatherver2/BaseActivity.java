package com.example.weatherver2;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherver2.data.Constants;

public class BaseActivity extends AppCompatActivity implements Constants {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isDarkTheme()) {
            setTheme(R.style.AppDarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    protected boolean isDarkTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(SWITCH_DARK, MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_DARK, true);
    }

    protected void setDarkTheme(boolean isDarkTheme) {
        SharedPreferences sharedPreferences = getSharedPreferences(SWITCH_DARK, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_DARK, isDarkTheme);
        editor.apply();
    }
}
