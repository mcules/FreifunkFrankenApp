package de.itstall.freifunkfranken.controller;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

// custom location listener interface to use location in different classes
public class CustomLocationListener implements LocationListener {
    private final LocationListenerInterface listenerInterface;

    // constructor
    public CustomLocationListener(LocationListenerInterface locationListenerInterface) {
        this.listenerInterface = locationListenerInterface;
    }

    // location has changed. Returns new location
    @Override
    public void onLocationChanged(Location location) {
        this.listenerInterface.onLocationChanged(location);
    }

    // status has changed. returns provider
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        this.listenerInterface.onStatusChanged(provider);

    }

    // provider enabled now. Returns provider
    @Override
    public void onProviderEnabled(String provider) {
        this.listenerInterface.onProviderEnabled(provider);
    }

    // provider disabled now. Returns provider
    @Override
    public void onProviderDisabled(String provider) {
        this.listenerInterface.onProviderDisabled(provider);
    }
}
