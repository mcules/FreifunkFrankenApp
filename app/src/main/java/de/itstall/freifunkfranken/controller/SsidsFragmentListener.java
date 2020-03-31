package de.itstall.freifunkfranken.controller;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.view.SsidsFragment;

import static android.content.Context.WIFI_SERVICE;

public class SsidsFragmentListener implements View.OnClickListener {
    private static final String TAG = SsidsFragmentListener.class.getSimpleName();
    private View view;
    private SsidsFragment ssidsFragment;

    public SsidsFragmentListener(SsidsFragment ssidsFragment) {
        this.ssidsFragment = ssidsFragment;
    }

    @Override
    public void onClick(View view) {
        this.view = view;
        ssidsFragment.checkPermission(Manifest.permission.ACCESS_WIFI_STATE, ssidsFragment.REQUEST_ID_ACCESS_WIFI_STATE);
        ssidsFragment.checkPermission(Manifest.permission.CHANGE_WIFI_STATE, ssidsFragment.REQUEST_ID_CHANGE_WIFI_STATE);
        ssidsFragment.checkPermission(Manifest.permission.WRITE_SETTINGS, ssidsFragment.REQUEST_ID_WRITE_SETTINGS);
        try {
            if(checkSystemWritePermission()) {
                Log.d(TAG, "Allow modify system settings is on");
                addSsidsToMobile();
            } else {
                Log.d(TAG, "Allow modify system settings is off");
                Toast.makeText(view.getContext(), "Allow modify system settings => ON", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.i(TAG, e.toString());
            Toast.makeText(view.getContext(), "unable to set SSIDs", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkSystemWritePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.System.canWrite(view.getContext())) {
                return true;
            } else {
                openAndroidPermissionsMenu();
            }
        }
        return false;
    }

    private void openAndroidPermissionsMenu() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + view.getContext().getPackageName()));
            view.getContext().startActivity(intent);
        }
    }

    private void addSsidsToMobile() {
        WifiConfiguration wifiConfig;
        WifiManager wifiManager;
        WifiNetworkSpecifier wifiNetworkSpecifier;
        NetworkRequest networkRequest;
        ConnectivityManager connectivityManager;
        WifiNetworkSuggestion wifiNetworkSuggestion;
        final List<WifiNetworkSuggestion> suggestionList = new ArrayList<>();
        int netId;

        for(int i = 0; i < SsidsFragment.ssidList.size(); i++) {
            if (Build.VERSION.SDK_INT >= 29) {
                /*wifiNetworkSpecifier = new WifiNetworkSpecifier.Builder()
                        .setSsid(SsidsFragment.ssidList.get(i).getSsid())
                        .build();
                networkRequest = new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .setNetworkSpecifier(wifiNetworkSpecifier)
                        .build();
                connectivityManager = (ConnectivityManager) view.getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                assert connectivityManager != null;
                connectivityManager.requestNetwork(networkRequest, new ConnectivityManager.NetworkCallback());*/
                wifiNetworkSuggestion = new WifiNetworkSuggestion.Builder()
                        .setSsid(SsidsFragment.ssidList.get(i).getSsid())
                        .setIsAppInteractionRequired(false)
                        .build();

                suggestionList.add(wifiNetworkSuggestion);
            } else {
                wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = String.format("\"%s\"", SsidsFragment.ssidList.get(i).getSsid());
                wifiConfig.preSharedKey = String.format("\"%s\"", SsidsFragment.ssidList.get(i).getKey());
                wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

                wifiManager = (WifiManager) view.getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
                //remember id
                assert wifiManager != null;
                // Turn wifi connection on
                wifiManager.setWifiEnabled(true);
                netId = wifiManager.addNetwork(wifiConfig);
                Log.e(TAG, "adding ssid:" + SsidsFragment.ssidList.get(i).getSsid() + " returned netId: " + netId);
            }
        }
        if(Build.VERSION.SDK_INT >= 29) {
            Log.d(TAG, "Array size: " + suggestionList.size());
            wifiManager = (WifiManager) view.getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
            assert wifiManager != null;
            final int status = wifiManager.addNetworkSuggestions(suggestionList);
            if(status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
                Log.e(TAG, "Add wifi suggestion list failed " + status);
            }

            final IntentFilter intentFilter = new IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION);
            final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(!Objects.equals(intent.getAction(), WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)) return;
                }
            };
            view.getContext().registerReceiver(broadcastReceiver, intentFilter);
        }
        /*wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();*/
    }
}
