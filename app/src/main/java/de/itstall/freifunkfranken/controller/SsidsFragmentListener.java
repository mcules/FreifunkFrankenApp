package de.itstall.freifunkfranken.controller;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.view.SsidsFragment;

import static android.content.Context.WIFI_SERVICE;

// listener for ssid fragment
public class SsidsFragmentListener implements View.OnClickListener {
    private static final String TAG = SsidsFragmentListener.class.getSimpleName();
    private View view;
    private WifiManager wifiManager;

    // if button clicked, add ssids to mobile
    @Override
    public void onClick(View view) {
        this.view = view;
        addSsidsToMobile();
    }

    /*
     * add ssid list to mobile
     *
     * checking if sdk version above 29 or not.
     * In sdk 29 the api has changed so from now we have to use wifiSuggestions.
     * Bevore we can still use wifiManager to add ssids
     */
    private void addSsidsToMobile() {
        WifiConfiguration wifiConfig;
        final List<WifiNetworkSuggestion> suggestionList = new ArrayList<>();

        if (Build.VERSION.SDK_INT < 29) {
            showDialog(view.getContext().getResources().getString(R.string.ssidAddSsidsTitle), view.getContext().getResources().getString(R.string.ssidAddSsidsDescription));
        }

        for (int i = 0; i < SsidsFragment.ssidList.size(); i++) {
            // create suggestion list for adding to mobile
            if (Build.VERSION.SDK_INT >= 29) {
                WifiNetworkSuggestion wifiNetworkSuggestion = new WifiNetworkSuggestion.Builder()
                        .setSsid(SsidsFragment.ssidList.get(i).getSsid())
                        .build();

                suggestionList.add(wifiNetworkSuggestion);
            } else {
                // add ssids with wifiManager
                wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = String.format("\"%s\"", SsidsFragment.ssidList.get(i).getSsid());
                wifiConfig.preSharedKey = String.format("\"%s\"", SsidsFragment.ssidList.get(i).getKey());
                wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

                wifiManager = (WifiManager) view.getContext().getApplicationContext().getSystemService(WIFI_SERVICE);

                assert wifiManager != null;
                wifiManager.setWifiEnabled(true);
                wifiManager.addNetwork(wifiConfig);
            }
        }

        // add suggestionlist to mobile
        if (Build.VERSION.SDK_INT >= 29) {
            deleteSuggestions();
            wifiManager = (WifiManager) view.getContext().getApplicationContext().getSystemService(WIFI_SERVICE);

            assert wifiManager != null;
            if (wifiManager.addNetworkSuggestions(suggestionList) != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
                Log.e(TAG, "Add wifi suggestion list failed");
            } else {
                showDialog(view.getContext().getResources().getString(R.string.ssidSuggestionsAddedTitle), view.getContext().getResources().getString(R.string.ssidSuggestionsAddedDescription));
            }

            final IntentFilter intentFilter = new IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION);
            final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    intent.getAction();
                }
            };
            view.getContext().registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    // for sdk >= 29 we should delete suggestion list initial to avoid problems
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void deleteSuggestions() {
        final List<WifiNetworkSuggestion> suggestionList = new ArrayList<>();

        wifiManager = (WifiManager) view.getContext().getApplicationContext().getSystemService(WIFI_SERVICE);

        assert wifiManager != null;
        wifiManager.removeNetworkSuggestions(suggestionList);
    }

    // show dialog to inform user what is happening
    private void showDialog(String title, String description) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(view.getContext().getResources().getString(R.string.okay), (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
