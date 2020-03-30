package de.itstall.freifunkfranken.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.MyLocationListener;
import de.itstall.freifunkfranken.model.AccessPoint;
import de.itstall.freifunkfranken.model.RequestAps;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = MapsFragment.class.getSimpleName();
    static MyLocationListener myLocationListener;
    private GoogleMap mMap;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.maps_fragment, container, false);

        //myLocationListener = new MyLocationListener(rootView);
        //myLocationListener.getLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        LatLng schonungen = new LatLng(50.0544, 10.3128);
        mMap.addMarker(new MarkerOptions().position(schonungen).title("Marker in Schonungen"));
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
        List<AccessPoint> accessPointList = new RequestAps(Objects.requireNonNull(this.getContext())).getAccessPointList();
        for(int i = 0; i < accessPointList.size(); i++) {
            if(accessPointList.get(i).isOnline()) {
                LatLng ap = new LatLng(accessPointList.get(i).getLat(), accessPointList.get(i).getLon());
                MarkerOptions markerOptions = new MarkerOptions().position(ap).title(accessPointList.get(i).getName());
                mMap.addMarker(markerOptions);
            }
        }
    }

    private void getDeviceLocation() {

    }

    private void updateLocationUI() {

    }
}
