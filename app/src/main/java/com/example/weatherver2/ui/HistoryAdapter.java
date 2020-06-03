package com.example.weatherver2.ui;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherver2.R;
import com.example.weatherver2.Repository;
import com.example.weatherver2.data.dataRoom.History;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements AdapterChangeable {

    private Repository repository;
    private OnMenuItemClickListener itemClickListener;

    public HistoryAdapter(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void notifyDataChange() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        holder.bind(repository.get(position));
    }

    @Override
    public int getItemCount() {
        return repository.getCount();
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.itemClickListener = onMenuItemClickListener;
    }

    public interface OnMenuItemClickListener {
        void onItemDeleteClick(History history);

        void onItemDeleteAllClick(History history);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView historyWeather;
        private TextView historyDate;
        private TextView historyCityName;
        private TextView historyTemp;
        private TextView historyWindSpeed;
        private TextView historyPressure;
        private TextView historyId;
        private History history;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initField(itemView);
            historyCityName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        showPopupMeny(historyCityName);
                        return true;
                    }
                    return false;
                }
            });
        }

        private void initField(@NonNull View itemView) {
            historyWeather = itemView.findViewById(R.id.historyWeather);
            historyDate = itemView.findViewById(R.id.historyDate);
            historyCityName = itemView.findViewById(R.id.historyCityName);
            historyTemp = itemView.findViewById(R.id.historyTemp);
            historyWindSpeed = itemView.findViewById(R.id.historyWindSpeed);
            historyPressure = itemView.findViewById(R.id.historyPressure);
            historyId = itemView.findViewById(R.id.historyId);
        }

        public void bind(History history) {
            this.history = history;
            historyDate.setText(history.getDate());
            historyCityName.setText((history.getName()));
            historyTemp.setText(history.getTemp());
            historyWeather.setText(history.getWeather());
            historyPressure.setText(history.getPressure());
            historyWindSpeed.setText(history.getwSpeed());
        }

        private void showPopupMeny(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.context_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_delete:
                            itemClickListener.onItemDeleteClick(history);
                            return true;
                        case R.id.menu_deleteAll:
                            itemClickListener.onItemDeleteAllClick(history);
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    }
}