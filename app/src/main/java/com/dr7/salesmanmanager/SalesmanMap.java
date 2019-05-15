package com.dr7.salesmanmanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SalesmanMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(currentTimeAndDate);

        List<SalesmanStations> stations = new DatabaseHandler(SalesmanMap.this).getAllSalesmanSatation(Login.salesMan, today);

        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = 0; i < stations.size(); i++) {
            for (int j = 0; j < stations.size(); j++) {
                if (stations.get(j).getSerial() == i + 1) {
                    LatLng latLng = new LatLng(Double.parseDouble(stations.get(j).getLatitude()), Double.parseDouble(stations.get(j).getLongitude()));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("(" + stations.get(j).getSerial() + ") " + stations.get(j).getCustName());

                    if (stations.get(j).getSerial() == 1)
                        mMap.addMarker(markerOptions).showInfoWindow();
                    else
                        mMap.addMarker(markerOptions);

                    mMap.setMaxZoomPreference(20);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

                    polylineOptions.add(latLng)
                            .color(Color.RED)
                            .width(2);
                    mMap.addPolyline(polylineOptions);
                    break;
                }
            }
        }
    }


}
