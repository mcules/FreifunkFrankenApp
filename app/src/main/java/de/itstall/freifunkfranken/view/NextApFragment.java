package de.itstall.freifunkfranken.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.NextApAdapter;
import de.itstall.freifunkfranken.controller.NextApsRequest;
import de.itstall.freifunkfranken.model.AccessPoint;

public class NextApFragment extends Fragment implements NextApAdapter.OnItemClicked {
    public static Location location;
    private RecyclerView rvAps;
    private View rootView;
    private LocationManager locationManager;
    private List<AccessPoint> accessPointList;
    private String locationProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nextap_fragment, container, false);

        SharedPreferences sharedPreferences = rootView.getContext()
                .getSharedPreferences(getResources().getString(R.string.app_name), 0);

        rvAps = rootView.findViewById(R.id.rvAps);
        rvAps.setLayoutManager(new LinearLayoutManager(getActivity()));

        locationManager = (LocationManager) rootView.getContext().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        checkPermissions();

        accessPointList = new NextApsRequest(
                Objects.requireNonNull(this.getContext())).
                getSortedList(
                        sharedPreferences.getBoolean("OfflineRouter", false),
                        sharedPreferences.getInt("RouterCount", 10)
                );

        showApList(accessPointList);

        return rootView;
    }

    private void checkPermissions() {
        Dexter.withActivity((Activity) rootView.getContext())
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                                  @Override
                                  public void onPermissionsChecked(MultiplePermissionsReport report) {
                                      if (report.areAllPermissionsGranted()) {
                                          if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                              locationProvider = getEnabledLocationProvider();

                                              assert locationProvider != null;
                                              location = locationManager.getLastKnownLocation(locationProvider);
                                          }
                                      }
                                  }

                                  @Override
                                  public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                      token.continuePermissionRequest();
                                  }
                              }
                )
                .check();
    }

    private void showApList(List<AccessPoint> accessPointList) {
        NextApAdapter nextApAdapter = new NextApAdapter(accessPointList);
        rvAps.setAdapter(nextApAdapter);
        rvAps.setItemAnimator(new DefaultItemAnimator());
        nextApAdapter.setOnClick(this);
    }

    @Override
    public void onItemClick(int position) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" +
                accessPointList.get(position).getLat() + "," +
                accessPointList.get(position).getLon());

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(Objects.requireNonNull(getContext()).getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private String getEnabledLocationProvider() {
        LocationManager locationManager = (LocationManager) rootView
                .getContext()
                .getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        assert locationManager != null;
        String bestProvider = locationManager.getBestProvider(criteria, true);

        assert bestProvider != null;
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
