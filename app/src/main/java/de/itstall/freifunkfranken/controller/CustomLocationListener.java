package de.itstall.freifunkfranken.controller;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class CustomLocationListener implements LocationListener {
    private final LocationListenerInterface listenerInterface;

    public CustomLocationListener(LocationListenerInterface locationListenerInterface) {
        this.listenerInterface = locationListenerInterface;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.listenerInterface.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        this.listenerInterface.onStatusChanged(provider);

    }

    @Override
    public void onProviderEnabled(String provider) {
        this.listenerInterface.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        this.listenerInterface.onProviderDisabled(provider);
    }
}
