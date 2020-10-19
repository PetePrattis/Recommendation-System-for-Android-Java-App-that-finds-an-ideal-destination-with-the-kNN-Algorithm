package com.unipi.p15013p15120.kastropoliteiesv2;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationPage helper = new LocationPage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Location you make a call from and move the camera
        LatLng topothesia;
        if (helper.lat != null && helper.lon != null)
            topothesia= new LatLng(helper.lat, helper.lon);
        else
            topothesia = new LatLng(0,0);


        //lat, lon and location name
        mMap.addMarker(new MarkerOptions().position(topothesia).title(getIntent().getStringExtra("locname")));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(topothesia));
    }
}
