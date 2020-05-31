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

    public History Copy(long id) {
        History newHistory = new History();
        newHistory.setDate(this.getDate());
        newHistory.setName(this.getName());
        newHistory.setTemp(this.getTemp());
        newHistory.setId(id);
        return newHistory;
    }
}
