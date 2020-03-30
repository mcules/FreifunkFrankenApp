package de.itstall.freifunkfranken.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;

import de.itstall.freifunkfranken.view.MainActivity;

public class MyLocationListener {
    public static LocationManager locationManager;
    public static Location myLocation;
    public static double longitude;
    public static double latitude;
    private static Location gps_loc;
    private static Location network_loc;
    private static Location final_loc;
    private MainActivity mainActivity;

    public MyLocationListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
        myLocation = getLocation();
    }

    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
        }
        ActivityCompat.requestPermissions(mainActivity, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE }, 1);

        try {
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }


        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

        Location gpsLocation = new Location("");
        gpsLocation.setLatitude(latitude);
        gpsLocation.setLongitude(longitude);

        return gpsLocation;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            if(MyLocationListener.latitude != location.getLatitude() && MyLocationListener.longitude != location.getLongitude()) {
                MyLocationListener.latitude = location.getLatitude();
                MyLocationListener.longitude = location.getLongitude();
                Log.v("LOCATION UPDATE", "IN ON LOCATION CHANGE, lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
            }

            /*
            LatLng userPosition = new LatLng(location.getLatitude(),location.getLongitude());

            googleMap.addMarker(new MarkerOptions()
                    .position(userPosition)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_fill))
                    .title("Test Pin"));*/
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
    };
}
