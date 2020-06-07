package com.example.weatherver2;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Constants {

    private GoogleMap mMap;
    private TextInputEditText address;
    private Button search;
    private Button back;
    private Marker marker;
    private List<Marker> markers = new ArrayList<Marker>();
    float zoom = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initView();
        requestPermissions();

    }

    private void initView() {
        address = findViewById(R.id.citySearchOnMap);
        search = findViewById(R.id.searchOnMapBtn);
        back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initSearchByAddress();
    }

    private void initSearchByAddress() {
//        address = findViewById(R.id.citySearchOnMap);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Geocoder geocoder = new Geocoder(MapsActivity.this);
                final String searchText = address.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(searchText, 1);
                            if (addresses.size() > 0) {
                                final LatLng location = new LatLng(addresses.get(0).getLatitude(),
                                        addresses.get(0).getLongitude());
                                runOnUiThread(new Runnable() {
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Не нашли", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Текущая позиция"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addMarker(latLng);
                getAddress(latLng);
//                drawLine();
            }
        });
    }

    private void getAddress(final LatLng location) {
        final Geocoder geocoder = new Geocoder(this);
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

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            // Запрашиваем эти два Permission’а у пользователя
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 20, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    String latitude = Double.toString(lat);
//                    textLatitude.setText(latitude);

                    double lng = location.getLongitude();
                    String longitude = Double.toString(lng);
//                    textLongitude.setText(longitude);

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

    private void addMarker(LatLng location) {
        String title = Integer.toString(markers.size());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));
        markers.add(marker);
    }
}