package de.itstall.freifunkfranken.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import de.itstall.freifunkfranken.model.AccessPoint;
import de.itstall.freifunkfranken.controller.RequestAps;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = MapsFragment.class.getSimpleName();
    private GoogleMap mMap;
    private View rootView;
    private SharedPreferences sharedPreferences;

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

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateLocationUI();
        getDeviceLocation();

        LatLng schonungen = new LatLng(50.0544, 10.3128);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(schonungen, 10));
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = mMap.getCameraPosition().target;
                Toast.makeText(rootView.getContext(), latLng.latitude + "+" + latLng.longitude, Toast.LENGTH_SHORT).show();
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

    private void getDeviceLocation() {

    }

    private void updateLocationUI() {

    }
}
