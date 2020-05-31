package com.example.weatherver2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherver2.HistoryLogic;
import com.example.weatherver2.R;
import com.example.weatherver2.Repository;
import com.example.weatherver2.data.dataRoom.History;
import com.example.weatherver2.data.dataRoom.RoomRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private HistoryAdapter adapter;
    private HistoryLogic logic;
    private Repository repository;
    private Button back;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        initBackBtn(view);
        initList(view);
        repository = new RoomRepository();

        return view;
    }

    private void initBackBtn(View view) {
        back = view.findViewById(R.id.historyBackBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartFragment startFragment = new StartFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.nav_host_fragment, startFragment).commit();
            }
        });
    }

    private void initList(View view) {
        logic = new HistoryLogic(repository);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerHistory);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryAdapter(logic.getRepository());
        logic.setAdapter(adapter);
        adapter.setOnMenuItemClickListener(new HistoryAdapter.OnMenuItemClickListener() {
            @Override
            public void onItemDeleteClick(History history) {
                logic.deleteHistory(history);
            }

            @Override
            public void onItemDeleteAllClick(History history) {
                logic.deleteAll();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}