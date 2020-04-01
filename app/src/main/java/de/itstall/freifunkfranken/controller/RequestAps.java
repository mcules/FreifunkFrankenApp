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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.model.AccessPoint;
import de.itstall.freifunkfranken.view.NextApFragment;

public class RequestAps {
    private List<AccessPoint> accessPointList = new ArrayList<>();
    private static final String TAG = RequestAps.class.getSimpleName();
    private Context context;

    public RequestAps(Context context) {
        String filename = "data.json";
        StringBuilder stringBuilder = new StringBuilder();
        File dataFile = new File(context.getFilesDir(), filename);
        this.context = context;

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

    public List<AccessPoint> getSortedList(boolean showOffline, int routerCount) {
        List<AccessPoint> routerList = new ArrayList<>();
        int counter = 0;

        for (int i = 0; i < accessPointList.size(); i++) {
            if(showOffline) {
                if (NextApFragment.location != null)
                    accessPointList.get(i).setDistance(NextApFragment.location);
                routerList.add(accessPointList.get(i));
                counter++;
            } else {
                if (accessPointList.get(i).isOnline()) {
                    if (NextApFragment.location != null)
                        accessPointList.get(i).setDistance(NextApFragment.location);
                    routerList.add(accessPointList.get(i));
                    counter++;
                }
            }
        }

        Collections.sort(routerList, new Comparator<AccessPoint>() {
            @Override
            public int compare(AccessPoint o1, AccessPoint o2) {
                return Integer.compare(o1.getDistance(), o2.getDistance());
            }
        });

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
