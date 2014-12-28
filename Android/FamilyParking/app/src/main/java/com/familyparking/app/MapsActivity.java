package com.familyparking.app;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.familyparking.app.serverClass.Car;
import com.familyparking.app.service.LocationService;
import com.familyparking.app.utility.AsyncTaskPosition;
import com.familyparking.app.utility.Tools;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

public class MapsActivity extends FragmentActivity {

    private GoogleMap googleMap;
    private LocationService locationService;
    private double[] position;
    private int position_attempt = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpFragmentMap();

        locationService = new LocationService(this);

        position = Tools.getLocationAlert(locationService, this);

        if(position != null){
            setUpMap();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        for(int i=0; i<position_attempt; i++) {
            if (position == null) {

                position = Tools.getLocation(locationService, this);

                if (position != null) {
                    setUpMap();
                    break;
                }
            }
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #googleMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpFragmentMap() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }
    }

    private void setUpMap() {
        LatLng position = new LatLng(this.position[0],this.position[1]);
        googleMap.addMarker(new MarkerOptions().position(position).title("My car"));

        CameraUpdate center = CameraUpdateFactory.newLatLng(position);

        googleMap.moveCamera(center);
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 5000, null);
    }

    public void sendPosition(View v) {
        String[] email = {"f.nobilia@gmail.com","nazzareno.marziale@gmail.com"};
        Car car = new Car(this.position,email);

        ArrayList<Object> params = new ArrayList<>();
        params.add(this);
        params.add(car);

        new AsyncTaskPosition().execute(params);
    }
}
