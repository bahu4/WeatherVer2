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

import com.example.weatherver2.R;
import com.example.weatherver2.data.Constants;
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
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment implements Constants {

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
            LatLng arkh = new LatLng(64, 40);
            marker = mMap.addMarker(new MarkerOptions().position(arkh).title("Текущая позиция"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(arkh));
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    addMarker(latLng);
                    getAddress(latLng);
                }
            });
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

    private void requestPermissionsForGMap() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
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

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 20, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    String latitude = Double.toString(lat);
                    setPointLatitude(latitude);

                    double lng = location.getLongitude();
                    String longitude = Double.toString(lng);
                    setPointLongitude(longitude);

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

    private void initView(View view) {
        address = view.findViewById(R.id.citySearchOnMap);
        search = view.findViewById(R.id.searchOnMapBtn);
        findOutTheWeather = view.findViewById(R.id.findWeatherBtnOnMap);
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

    private void btnWeatherClick(Button btn) {
        btn.setOnClickListener((v) -> {
            StartFragment startFragment = new StartFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, startFragment)
                    .commit();
        });
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
}