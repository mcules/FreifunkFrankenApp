package de.itstall.freifunkfranken.controller;

import android.content.Intent;
import android.location.Location;
import android.provider.Settings;

import com.google.android.gms.maps.GoogleMap;

import de.itstall.freifunkfranken.view.MapsFragment;

public class MapsFragmentListener implements GoogleMap.OnCameraIdleListener {
    private final MapsFragment mapsFragment;

    public MapsFragmentListener(MapsFragment mapsFragment) {
        this.mapsFragment = mapsFragment;
    }

    public CustomLocationListenerInterface CustomLocationListenerInterface() {
        return new CustomLocationListenerInterface() {
            @Override
            public void onLocationChanged(Location location) {
                mapsFragment.changeLocation(location);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mapsFragment.startActivity(intent);
            }
        };
    }

    @Override
    public void onCameraIdle() {
        mapsFragment.sharedPreferences.edit()
                .putInt("mapZoom", (int) mapsFragment.mMap.getCameraPosition().zoom)
                .apply();
    }
}
