package com.example.weatherver2;

import com.example.weatherver2.data.dataRoom.History;

public interface Repository {
    History add(History history);
    void update(History history);
    History get(int position);
    void delete(History history);
    void deleteAll();
    int getCount();
}