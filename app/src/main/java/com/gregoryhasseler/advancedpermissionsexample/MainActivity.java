package com.gregoryhasseler.advancedpermissionsexample;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_FINE_LOCATION_ACCESS = 1;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener = new MyLocationListener();
    private TextView mLocationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a handle to the Location Manager
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Get a handle to our location field so we can update it later
        mLocationField = (TextView) findViewById(R.id.location_field);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        checkPermissions();

        // Check to see if we have permissions first
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Register to get location updates
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);
        }
    }

    private void stopLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (permissions.length == 0) {
            // Somewhere a cancellation occurred. Return early
            return;
        }
        switch (requestCode) {
            case REQUEST_FINE_LOCATION_ACCESS:

                for (int i = 0; i < permissions.length; i++) {
                    switch (permissions[i]) {
                        case Manifest.permission.ACCESS_FINE_LOCATION:
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                // Permission has been granted!
                            } else {
                                // No luck, permission not granted
                            }
                    }
                }
        }
    }

    private void checkPermissions() {
        // Capture if we already have the permission
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        // If we don't already have the permission, let's request it
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show explanation of why the permission needs to be granted
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Some very good reason for location access.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Ask again now that the rationale has been shared
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_FINE_LOCATION_ACCESS);
                            }
                            })
                        .show();


            } else {

                // Request the permission we want
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION_ACCESS);
            }
        }
    }

    /**
     * Our custom location listener that will update our location field.
     */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // We have a new location, let's update our text field
            String formattedLocation = location.getLatitude() + "," + location.getLongitude();
            mLocationField.setText(formattedLocation);
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
    }
}
