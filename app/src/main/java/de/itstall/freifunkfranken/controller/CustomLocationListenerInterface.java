package de.itstall.freifunkfranken.controller;

import android.location.Location;

// custom location listener interface to use location in different classes and implement methods
public class CustomLocationListenerInterface implements LocationListenerInterface {
    // location has changed
    @Override
    public void onLocationChanged(Location location) {

    }

    // status has changed
    @SuppressWarnings("unused")
    @Override
    public void onStatusChanged(String provider) {

    }

    // provider enabled now
    @SuppressWarnings("unused")
    @Override
    public void onProviderEnabled(String provider) {

    }

    // provider disabled now
    @SuppressWarnings("unused")
    @Override
    public void onProviderDisabled(String provider) {

    }
}
