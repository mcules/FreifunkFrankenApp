package de.itstall.freifunkfranken.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.recyclerview.widget.DefaultItemAnimator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.controller.SsidsAdapter;

public class RequestSsids {
    private List<Ssid> ssidList = new ArrayList<>();

    public RequestSsids(Context context) {
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
            JSONArray jsonArray = jsonString.getJSONArray("ssids");
            for (int i = 0; i < jsonArray.length(); i++) {
                ssidList.add(new Ssid(jsonArray.getString(i)));
            }
        } catch (JSONException e) {
            Log.e("JSONException:", Objects.requireNonNull(e.getMessage()));
        }
    }

    public List<Ssid> getSsidList() {
        return ssidList;
    }
}