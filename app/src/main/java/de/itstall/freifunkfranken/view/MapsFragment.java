package de.itstall.freifunkfranken.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.CustomLocationListener;
import de.itstall.freifunkfranken.controller.CustomLocationListenerInterface;
import de.itstall.freifunkfranken.controller.RequestAps;
import de.itstall.freifunkfranken.model.AccessPoint;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = MapsFragment.class.getSimpleName();
    private GoogleMap mMap;
    private View rootView;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog = null;
    private LocationManager locationManager;
    private CustomLocationListener customLocationListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private CustomLocationListenerInterface locationListener = new CustomLocationListenerInterface() {
        @Override
        public void onLocationChanged(Location location) {
            if (mMap != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), mMap.getCameraPosition().zoom)
                );
                sharedPreferences.edit().putInt("mapZoom", (int) mMap.getCameraPosition().zoom).apply();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.maps_fragment, container, false);

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage(getResources().getString(R.string.mapLoading));
        progressDialog.show();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);

        sharedPreferences = rootView.getContext().getSharedPreferences(getResources().getString(R.string.app_name), 0);

        customLocationListener = new CustomLocationListener(locationListener);
        locationManager = (LocationManager) rootView.getContext().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Dexter.withActivity((Activity) rootView.getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 25, customLocationListener);
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();

        return rootView;
    }

    private void showApsOnMap() {
        List<AccessPoint> accessPointList = new RequestAps(Objects.requireNonNull(this.getContext())).getSortedList(sharedPreferences.getBoolean("MapOfflineRouter", false), 0);

        for(int i = 0; i < accessPointList.size(); i++) {
            LatLng ap = new LatLng(accessPointList.get(i).getLat(), accessPointList.get(i).getLon());
            MarkerOptions markerOptions = new MarkerOptions().position(ap).title(accessPointList.get(i).getName());
            if(accessPointList.get(i).isOnline()) markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            else markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(50.0489, 10.2301),
                sharedPreferences.getInt("mapZoom", 10)));
        showApsOnMap();

        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onPause() {
        locationManager.removeUpdates(customLocationListener);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Dexter.withActivity((Activity) rootView.getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 25, customLocationListener);
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
}
