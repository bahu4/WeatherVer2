package com.example.weatherver2.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.weatherver2.HistoryLogic;
import com.example.weatherver2.R;
import com.example.weatherver2.Repository;
import com.example.weatherver2.data.Constants;
import com.example.weatherver2.data.RetrofitRequest;
import com.example.weatherver2.data.dataRoom.History;
import com.example.weatherver2.data.dataRoom.RoomRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment implements Constants, RetrofitRequest.RetrofitCallback {

    private GoogleMap mMap;
    private TextInputEditText address;
    private Button search;
    private Button back;
    private Button findOutTheWeather;
    private Marker marker;
    private List<Marker> markers = new ArrayList<Marker>();
    float zoom = 12;
    String pointLatitude;
    String pointLongitude;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            LatLng sydney = new LatLng(-34, 151);
            marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Текущая позиция")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    addMarker(latLng);
                    getAddress(latLng);
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps_main, container, false);

        initView(view);
        requestPermissionsForGMap();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void initView(View view) {
        RetrofitRequest.RetrofitCallback retrofitCallback = this;
        MapsFragment mapsFragment = this;
        address = view.findViewById(R.id.citySearchOnMap);
        search = view.findViewById(R.id.searchOnMapBtn);
        findOutTheWeather = view.findViewById(R.id.findWeatherBtnOnMap);
        findOutTheWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitRequest retrofitRequest = new RetrofitRequest(getPointLatitude(), getPointLongitude(), mapsFragment, retrofitCallback);
                retrofitRequest.initRetrofit();
                retrofitRequest.requestWithCoord();
            }
        });
        back = view.findViewById(R.id.backBtn);
        btnWeatherClick(back);
        initSearchByAddress();
    }

    private void initSearchByAddress() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Geocoder geocoder = new Geocoder(getContext());
                final String searchText = address.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(searchText, 1);
                            if (addresses.size() > 0) {
                                final LatLng location = new LatLng(addresses.get(0).getLatitude(),
                                        addresses.get(0).getLongitude());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMap.addMarker(new MarkerOptions()
                                                .position(location)
                                                .title(searchText)
                                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Не нашли", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void requestPermissionsForGMap() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    LatLng currentPosition = new LatLng(lat, lng);
                    marker.setPosition(currentPosition);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, zoom));
                }


                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void getAddress(final LatLng location) {
        final Geocoder geocoder = new Geocoder(getContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                    address.post(new Runnable() {
                        @Override
                        public void run() {
                            address.setText(addresses.get(0).getAddressLine(0));
                            double lat = location.latitude;
                            DecimalFormat df = new DecimalFormat("#.####");
                            df.setRoundingMode(RoundingMode.CEILING);
                            String latitude = df.format(lat);
                            setPointLatitude(latitude);

                            double lng = location.longitude;
                            String longitude = df.format(lng);
                            setPointLongitude(longitude);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void addMarker(LatLng location) {
        String title = Integer.toString(markers.size());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));
        markers.add(marker);
    }


    public String getPointLatitude() {
        return pointLatitude;
    }

    public void setPointLatitude(String pointLatitude) {
        this.pointLatitude = pointLatitude;
    }

    public String getPointLongitude() {
        return pointLongitude;
    }

    public void setPointLongitude(String pointLongitude) {
        this.pointLongitude = pointLongitude;
    }

    private void btnWeatherClick(Button btn) {
        btn.setOnClickListener((v) -> {
            StartFragment startFragment = new StartFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, startFragment)
                    .commit();
        });
    }

    @Override
    public void callingBack(float temp, String name, float windSpeed, float pressure, String weather) {
        WeatherFragment weatherFragment = new WeatherFragment();
        FragmentManager fragmentManager = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString(TEMPERATURE, String.format("%.0f", temp));
        bundle.putString(WIND_SPEED, String.format("%.0f", windSpeed));
        bundle.putString(PRESSURE, String.format("%.0f", pressure));
        bundle.putString(WEATHER_CONDITIONS, weather);
        bundle.putString(CITY_NAME, name.substring(0, 1).toUpperCase() + name.substring(1));
        weatherFragment.setArguments(bundle);
        addToDatabase(temp, name, windSpeed, pressure, weather);
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, weatherFragment).commit();
    }

    private void addToDatabase(float temp, String name, float windSpeed, float pressure, String weather) {
        Repository repository = new RoomRepository();
        HistoryLogic historyLogic = new HistoryLogic(repository);
        History history = new History();
        HistoryAdapter adapter = new HistoryAdapter(historyLogic.getRepository());
        historyLogic.setAdapter(adapter);
        history.setWeather(weather);
        history.setName(name.substring(0, 1).toUpperCase() + name.substring(1));
        history.setDate(DateFormat.getDateInstance().format(new Date()));
        history.setTemp(String.format("%.0f", temp) + " °C");
        history.setPressure(String.format("%.0f", pressure));
        history.setwSpeed(String.format("%.0f", windSpeed));
        historyLogic.addHistory(history);
    }

    @Override
    public void errorDialog(int dialogId) {
        DialogBuilderFragment dialogBuilderFragment = new DialogBuilderFragment(dialogId, this);
        dialogBuilderFragment.show(getFragmentManager(), "dBuilder");
    }
}