package de.itstall.freifunkfranken.controller;

import android.location.Location;

interface LocationListenerInterface {
    void onLocationChanged(Location location);

    void onStatusChanged(String provider);

    void onProviderEnabled(String provider);

    void onProviderDisabled(String provider);
}
