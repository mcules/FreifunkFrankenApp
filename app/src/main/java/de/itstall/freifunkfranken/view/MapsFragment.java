package de.itstall.freifunkfranken.view;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.RequestAps;
import de.itstall.freifunkfranken.model.AccessPoint;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = MapsFragment.class.getSimpleName();
    private GoogleMap mMap;
    private View rootView;
    private SharedPreferences sharedPreferences;
    //private MyLocationProvider mLocationProvider;
    public static final int REQUEST_ID_ACCESS_FINE_LOCATION = 100;
    public static final int REQUEST_ID_ACCESS_COARSE_LOCATION = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.maps_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);

        sharedPreferences = rootView.getContext().getSharedPreferences("FreifunkFrankenApp", 0);

        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_ID_ACCESS_COARSE_LOCATION);
        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_ID_ACCESS_COARSE_LOCATION);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(50.0544, 10.3128);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = mMap.getCameraPosition().target;
                //Toast.makeText(rootView.getContext(), latLng.latitude + "+" + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });
        showApsOnMap();
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

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                rootView.getContext(),
                permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                    Objects.requireNonNull(getActivity()),
                    new String[]{permission},
                    requestCode);
        } else {
            Toast.makeText(
                    rootView.getContext(),
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_ID_ACCESS_FINE_LOCATION:
                // Checking whether user granted the permission or not.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Showing the toast message
                    Toast.makeText(
                            getActivity(),
                            "Fine Location Permission Granted",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getActivity(),
                            "Fine Location Permission Denied",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            case REQUEST_ID_ACCESS_COARSE_LOCATION:
                // Checking whether user granted the permission or not.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Showing the toast message
                    Toast.makeText(
                            getActivity(),
                            "Coarse Location Permission Granted",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getActivity(),
                            "Coarse Location Permission Denied",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }
}
