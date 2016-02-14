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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends MapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private User[] users;
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
        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        final MiitApi mApi = new MiitApi(getActivity());
        LocationListener ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.v("com.32s.miit", "newloc");
                mApi.updateLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.v("com.32s.miit", "stc");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.v("com.32s.miit", "enabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.v("com.tts.miit", "disabled");
            }
        };


        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.v("com.32s.miit", "noperm");
            return;
        }
        Log.v("com.32s.miit", provider);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, ll);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, ll);
        updateAnnotations();
        // Add a marker in Sydney and move the camera

    }

    public void updateAnnotations(){
        if (mMap != null){
            for (User user:users) {

                mMap.addMarker(new MarkerOptions()
                    .position(user.location)
                    .title(user.username));
            }
        }
    }
    public void updateUsers(User[] users){
        this.users = users;
        updateAnnotations();
    }
}
