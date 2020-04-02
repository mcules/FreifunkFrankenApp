package de.itstall.freifunkfranken.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

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
    String locationProvider;

    private CustomLocationListenerInterface locationListener = new CustomLocationListenerInterface() {
        @Override
        public void onLocationChanged(Location location) {
            if (mMap != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(
                                location.getLatitude(),
                                location.getLongitude()),
                        mMap.getCameraPosition().zoom)
                );
                sharedPreferences.edit()
                        .putInt("mapZoom", (int) mMap.getCameraPosition().zoom).apply();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        rootView = inflater.inflate(R.layout.maps_fragment, container, false);

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage(getResources().getString(R.string.mapLoading));
        progressDialog.show();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);

        sharedPreferences = rootView.getContext()
                .getSharedPreferences(getResources().getString(R.string.app_name), 0);

        getLocation();

        return rootView;
    }

    private void showApsOnMap() {
        List<AccessPoint> accessPointList = new RequestAps(
                Objects.requireNonNull(
                        this.getContext()))
                .getSortedList(
                        sharedPreferences.getBoolean(
                                "MapOfflineRouter",
                                false)
                        , 0);

        for (int i = 0; i < accessPointList.size(); i++) {
            LatLng ap = new LatLng(
                    accessPointList.get(i).getLat(), accessPointList.get(i).getLon());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(ap)
                    .title(accessPointList.get(i).getName());
            if (accessPointList.get(i).isOnline())
                markerOptions.icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            else
                markerOptions.icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(50.0489, 10.2301);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(sharedPreferences.getInt("mapZoom", 10))
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setMyLocationEnabled(true);

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
        getLocation();
    }

    private void getLocation() {
        customLocationListener = new CustomLocationListener(locationListener);
        locationManager = (LocationManager) rootView.getContext()
                .getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        Dexter.withActivity((Activity) rootView.getContext())
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                                  @Override
                                  public void onPermissionsChecked(
                                          MultiplePermissionsReport report
                                  ) {
                                      if (report.areAllPermissionsGranted()) {
                                          if (
                                                  ActivityCompat.checkSelfPermission(
                                                          rootView.getContext(),
                                                          Manifest
                                                                  .permission
                                                                  .ACCESS_FINE_LOCATION
                                                  ) == PackageManager.PERMISSION_GRANTED
                                                          && ActivityCompat.checkSelfPermission(
                                                          rootView.getContext(),
                                                          Manifest
                                                                  .permission
                                                                  .ACCESS_COARSE_LOCATION
                                                  ) == PackageManager.PERMISSION_GRANTED
                                          ) {
                                              locationProvider = getEnabledLocationProvider();

                                              assert locationProvider != null;
                                              locationManager
                                                      .requestLocationUpdates(
                                                              locationProvider,
                                                              3000,
                                                              25,
                                                              customLocationListener);
                                          }
                                      }
                                  }

                                  @Override
                                  public void onPermissionRationaleShouldBeShown(
                                          List<PermissionRequest> permissions,
                                          PermissionToken token
                                  ) {
                                      token.continuePermissionRequest();
                                  }
                              }
                )
                .check();
    }

    private String getEnabledLocationProvider() {
        LocationManager locationManager = (LocationManager) rootView.getContext()
                .getSystemService(Context.LOCATION_SERVICE);

        //Kriterien um den LocationProvider zu finden
        Criteria criteria = new Criteria();

        //Gebe Namen des Providers zurück, der auf die Kriterien am besten passt
        String bestProvider = locationManager.getBestProvider(criteria, true);

        boolean enabled = locationManager.isProviderEnabled(bestProvider);

        if (!enabled) {
            Toast.makeText(
                    rootView.getContext(),
                    "No location provider enabled!",
                    Toast.LENGTH_LONG
            ).show();

            return null;
        }
        return bestProvider;
    }
}
