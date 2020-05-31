package com.example.weatherver2.ui;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherver2.Repository;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements AdapterChangeable {

    private Repository repository;
    private MenuItem.OnMenuItemClickListener itemClickListener;

    public HistoryAdapter(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void notifyDataChange() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}