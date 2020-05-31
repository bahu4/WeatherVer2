package com.example.weatherver2.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class City {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "DATE")
    public String date;
    @ColumnInfo(name = "NAME")
    public String name;
    @ColumnInfo(name = "TEMPERATURE")
    public int temp;

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

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public City Copy(long id) {
        City newCity = new City();
        newCity.setDate(this.getDate());
        newCity.setName(this.getName());
        newCity.setTemp(this.getTemp());
        newCity.setId(id);
        return newCity;
    }
}
