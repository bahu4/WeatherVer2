package com.example.weatherver2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.weatherver2.R;
import com.example.weatherver2.data.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityListFragment extends Fragment implements Constants {

    private Button cancelCityBtn;
    private Button mscBtn;
    private Button spbBtn;
    private Button arkhBtn;

    public CityListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);

        initCityField(view);
        btnCancelClick(cancelCityBtn);
        btnClick(mscBtn);
        btnClick(spbBtn);
        btnClick(arkhBtn);

        return view;
    }

    private void btnClick(Button btn) {
        btn.setOnClickListener((v) -> {
            StartFragment startFragment = new StartFragment();
            Bundle bundle = new Bundle();
            bundle.putString(CITY_NAME, btn.getText().toString());
            startFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, startFragment).commit();
        });
    }

    private void btnCancelClick(Button btn) {
        btn.setOnClickListener((v) -> {
            StartFragment startFragment = new StartFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, startFragment).commit();
        });
    }


    private void initCityField(View view) {
        cancelCityBtn = view.findViewById(R.id.cancelCityBtn);
        mscBtn = view.findViewById(R.id.cityMscBtn);
        spbBtn = view.findViewById(R.id.ctySpbBtn);
        arkhBtn = view.findViewById(R.id.cityArkhBtn);
    }
}
