package de.itstall.freifunkfranken.controller;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.model.AccessPoint;
import de.itstall.freifunkfranken.view.NextApFragment;

// request next accesspoint list from datafile
public class NextApsRequest {
    private final List<AccessPoint> accessPointList = new ArrayList<>();
    private static final String TAG = NextApsRequest.class.getSimpleName();

    // constructor
    public NextApsRequest(Context context) {
        String filename = "data.json";
        StringBuilder stringBuilder = new StringBuilder();
        File dataFile = new File(context.getFilesDir(), filename);

        // read whole data file
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            Log.e("ReadWriteFile", "Unable to read file: " + filename);
        }

        // get ssid array and parse into list
        try {
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray nodes = jsonObject.getJSONArray("nodes");
            JSONObject node;
            AccessPoint ap;
            for (int i = 0; i < nodes.length(); i++) {
                node = nodes.getJSONObject(i);
                ap = new AccessPoint(
                        node.getString("name"),
                        node.getDouble("lat"),
                        node.getDouble("lon"),
                        node.getBoolean("online")
                );
                accessPointList.add(ap);
            }
        } catch (JSONException e) {
            Log.e("JSONException:", Objects.requireNonNull(e.getMessage()));
        }
    }

    // return ssidlist
    public List<AccessPoint> getSortedList(boolean showOffline, int routerCount) {
        List<AccessPoint> routerList = new ArrayList<>();

        // get distance for each accesspoint to current location
        for (int i = 0; i < accessPointList.size(); i++) {
            if(showOffline) {
                if (NextApFragment.location != null)
                    accessPointList.get(i).setDistance(NextApFragment.location);
                routerList.add(accessPointList.get(i));
            } else {
                if (accessPointList.get(i).isOnline()) {
                    if (NextApFragment.location != null)
                        accessPointList.get(i).setDistance(NextApFragment.location);
                    routerList.add(accessPointList.get(i));
                }
            }
        }

        // sort list by distance
        Collections.sort(routerList, (o1, o2) -> Integer.compare(o1.getDistance(), o2.getDistance()));

        // returns the passed number of accesspoints
        if (routerCount > 0) {
            List<AccessPoint> resultList = new ArrayList<>();
            for (int i = 0; i < routerCount; i++) {
                resultList.add(routerList.get(i));
            }
            return resultList;
        } else {
            return routerList;
        }
    }
}
