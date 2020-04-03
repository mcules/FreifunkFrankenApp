package de.itstall.freifunkfranken.controller;

import android.location.Location;

// interface for location listener
interface LocationListenerInterface {
    // location has changed
    void onLocationChanged(Location location);

    // status has changed
    void onStatusChanged(String provider);

    // provider enabled
    void onProviderEnabled(String provider);

    // provider disabled
    void onProviderDisabled(String provider);
}
