package com.artifact.events;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.artifact.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by girish.kulkarni on 10/9/15.
 */
public class EventMapActivity extends Activity {

    // Google Map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_map_layout);

        try {
            // Loading map
            initializeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            // latitude and longitude
            double latitude = 12.971599;
            double longitude = 77.594563;

            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps");

            // Changing marker icon
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));

            // adding marker
            googleMap.addMarker(marker);

            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(12).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initializeMap();
    }

}
