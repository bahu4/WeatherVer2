package com.example.weatherver2.data.dataRoom;

import com.example.weatherver2.App;
import com.example.weatherver2.Repository;

public class RoomRepository implements Repository {

    private HistoryDAO historyDAO;

    public RoomRepository() {
        historyDAO = App.getInstance().getHistoryDao();
    }

    @Override
    public History add(History history) {
        long id = historyDAO.insertHistory(history);
        history.setId(id);
        return history;
    }

    @Override
    public void update(History history) {
        historyDAO.update(history);
    }

    @Override
    public History get(int position) {
        return historyDAO.getHistory(position);
    }

    @Override
    public void delete(History history) {
        historyDAO.delete(history);
    }

    @Override
    public void deleteAll() {
        historyDAO.deleteAll();
    }

    @Override
    public int getCount() {
        return historyDAO.getCount();
    }
}
