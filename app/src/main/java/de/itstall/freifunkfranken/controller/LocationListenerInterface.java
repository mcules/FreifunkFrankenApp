package de.itstall.freifunkfranken.controller;

import android.location.Location;

// interface for location listener
interface LocationListenerInterface {
    // location has changed
    void onLocationChanged(Location location);

    // status has changed
    @SuppressWarnings("EmptyMethod")
    void onStatusChanged(@SuppressWarnings("unused") String provider);

    // provider enabled
    @SuppressWarnings("EmptyMethod")
    void onProviderEnabled(@SuppressWarnings("unused") String provider);

    // provider disabled
    @SuppressWarnings("unused")
    void onProviderDisabled(@SuppressWarnings("unused") String provider);
}
