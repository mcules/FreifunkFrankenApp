package de.itstall.freifunkfranken.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestAps {
    private List<AccessPoint> accessPointList = new ArrayList<>();

    public RequestAps(Context context) {
        String filename = "data.json";
        StringBuilder stringBuilder = new StringBuilder();
        File dataFile = new File(context.getFilesDir(), filename);

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
            JSONObject jsonString = new JSONObject(stringBuilder.toString());
            JSONArray nodes = jsonString.getJSONArray("nodes");
            JSONObject node;
            AccessPoint ap;
            for (int i = 0; i < nodes.length(); i++) {
                node = nodes.getJSONObject(i);
                Log.i("node: ", node.getString("name"));
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

    public List<AccessPoint> getAccessPointList() {
        return accessPointList;
    }
}
