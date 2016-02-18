package com.thirtytwostudios.miit;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapsActivity extends MapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private User[] users;
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_google_maps);

        this.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Show the users own location
        mMap.setMyLocationEnabled(true);

        final MiitApi mApi = new MiitApi(getActivity());

        updateAnnotations();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.v("com.32s.miit","following user was clicked: "+marker.getTitle());
            }
        });
        // Add a marker in Sydney and move the camera

    }

    public void updateAnnotations(){
        if (mMap != null && users != null){
            for (Marker cMark : markers){
                cMark.remove();
            }
            for (User user:users) {

                Marker mark = mMap.addMarker(new MarkerOptions()
                        .position(user.location)
                        .title(user.username)
                        .anchor((float)0.5, (float)0.5)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)) );
                markers.add(mark);
            }
        }
    }
    public void updateUsers(User[] users){
        this.users = users;
        updateAnnotations();
    }
    public void focusOnUser(User user){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user.location,15),1000,null);
        for (Marker marker: markers){
            if (user.username.equals(marker.getTitle())){
                marker.showInfoWindow();
            }
        }
    }
}
