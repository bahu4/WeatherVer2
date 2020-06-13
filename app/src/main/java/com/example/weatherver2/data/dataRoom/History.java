package com.example.weatherver2.data.dataRoom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class History {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "DATE")
    public String date;
    @ColumnInfo(name = "NAME")
    public String name;
    @ColumnInfo(name = "TEMPERATURE")
    public String temp;
    @ColumnInfo(name = "PRESSURE")
    public String pressure;
    @ColumnInfo(name = "WINDSPEED")
    public String wSpeed;
    @ColumnInfo(name = "WEATHER")
    public String weather;

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getwSpeed() {
        return wSpeed;
    }

    public void setwSpeed(String wSpeed) {
        this.wSpeed = wSpeed;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
