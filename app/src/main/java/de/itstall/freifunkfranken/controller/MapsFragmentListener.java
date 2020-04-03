package de.itstall.freifunkfranken.controller;

import android.content.Intent;
import android.location.Location;
import android.provider.Settings;

import com.google.android.gms.maps.GoogleMap;

import de.itstall.freifunkfranken.view.MapsFragment;

// listener for maps fragment
public class MapsFragmentListener implements GoogleMap.OnCameraIdleListener {
    private final MapsFragment mapsFragment;

    // consturctor adds mapsFragment to class variable
    public MapsFragmentListener(MapsFragment mapsFragment) {
        this.mapsFragment = mapsFragment;
    }

    // create custom listener interface for recyclerview
    public CustomLocationListenerInterface CustomLocationListenerInterface() {
        return new CustomLocationListenerInterface() {
            // location has changed. Give that information back to parent class
            @Override
            public void onLocationChanged(Location location) {
                mapsFragment.changeLocation(location);
            }

            // provider was disabled. Show intent with location settings
            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mapsFragment.startActivity(intent);
            }
        };
    }

    // camera was moved and is idle now. Update camera zoom to preferences for later use.
    @Override
    public void onCameraIdle() {
        mapsFragment.sharedPreferences.edit()
                .putInt("mapZoom", (int) mapsFragment.mMap.getCameraPosition().zoom)
                .apply();
    }
}
