package com.example.weatherver2.data.dataRoom;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {History.class}, version = 1)
public abstract class DatabaseHistory extends RoomDatabase {
    public abstract HistoryDAO getHistoryDao();
}
