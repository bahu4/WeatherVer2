package com.example.weatherver2;

import android.app.Application;

import androidx.room.Room;

import com.example.weatherver2.data.dataRoom.DatabaseHistory;
import com.example.weatherver2.data.dataRoom.HistoryDAO;

public class App extends Application {
    private static App instance;
    private DatabaseHistory db;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        db = Room.databaseBuilder(getApplicationContext(), DatabaseHistory.class, "History_database")
                .build();
    }

    public HistoryDAO getHistoryDao() {
        return db.getHistoryDao();
    }
}
