package de.itstall.freifunkfranken.view;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.RequestSsids;
import de.itstall.freifunkfranken.controller.SsidsAdapter;
import de.itstall.freifunkfranken.controller.SsidsFragmentListener;
import de.itstall.freifunkfranken.model.Ssid;

public class SsidsFragment extends androidx.fragment.app.Fragment {
    private static final String TAG = SsidsFragment.class.getSimpleName();
    public static List<Ssid> ssidList;
    private RecyclerView rvSsids;
    private View rootView;
    private SsidsFragmentListener ssidsFragmentListener;
    public static final int REQUEST_ID_ACCESS_WIFI_STATE = 102;
    public static final int REQUEST_ID_CHANGE_WIFI_STATE = 103;
    public static final int REQUEST_ID_WRITE_SETTINGS = 104;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ssids_fragment, container, false);
        rvSsids = rootView.findViewById(R.id.rvSsids);
        rvSsids.setLayoutManager(new LinearLayoutManager(getActivity()));

        ssidsFragmentListener = new SsidsFragmentListener(this);

        Button ssidsFragmentBtnAddAll = rootView.findViewById(R.id.ssidsFragmentBtnAddAll);
        ssidsFragmentBtnAddAll.setOnClickListener(ssidsFragmentListener);

        ssidList = new RequestSsids(Objects.requireNonNull(this.getContext())).getSsidList();
        showSsidList(ssidList);

        return rootView;
    }

    private void showSsidList(List<Ssid> ssidList) {
        SsidsAdapter ssidsAdapter = new SsidsAdapter(ssidList);
        rvSsids.setAdapter(ssidsAdapter);
        rvSsids.setItemAnimator(new DefaultItemAnimator());
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
            Log.d(TAG, "Permission " + permission + " already granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_ID_ACCESS_WIFI_STATE:
                // Checking whether user granted the permission or not.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission " + permissions[requestCode] + " granted");
                    Toast.makeText(
                            getActivity(),
                            "Access Wifi state Permission Granted",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Log.d(TAG, "Permission " + permissions[requestCode] + " denied");
                    Toast.makeText(getActivity(),
                            "Access Wifi state Permission Denied",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            case REQUEST_ID_CHANGE_WIFI_STATE:
                // Checking whether user granted the permission or not.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission " + permissions[requestCode] + " granted");
                    Toast.makeText(
                            getActivity(),
                            "Change Wifi Permission Granted",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Log.d(TAG, "Permission " + permissions[requestCode] + " denied");
                    Toast.makeText(getActivity(),
                            "Change Wifi Permission Denied",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            case REQUEST_ID_WRITE_SETTINGS:
                // Checking whether user granted the permission or not.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission " + permissions[requestCode] + " granted");
                    Toast.makeText(
                            getActivity(),
                            "Write Settings Permission Granted",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Log.d(TAG, "Permission " + permissions[requestCode] + " denied");
                    Toast.makeText(getActivity(),
                            "Write Settings Permission Denied",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }
}