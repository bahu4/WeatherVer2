package com.example.weatherver2;

import com.example.weatherver2.data.dataRoom.History;
import com.example.weatherver2.ui.AdapterChangeable;

public class HistoryLogic {
    private Repository repository;
    private AdapterChangeable adapter;

    public HistoryLogic(Repository repository) {
        this.repository = repository;
    }

    public void setAdapter(AdapterChangeable adapter) {
        this.adapter = adapter;
    }

    public Repository getRepository() {
        return repository;
    }

    private void updateHistory() {
        adapter.notifyDataChange();
    }

    public void addHistory(History history) {
        getRepository().add(history);
        updateHistory();
    }

    public void deleteHistory(History history) {
        getRepository().delete(history);
        updateHistory();
    }
}
