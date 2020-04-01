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
import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.model.News;

public class RequestNews {
    private static final String TAG = RequestNews.class.getSimpleName();
    private List<News> newsList = new ArrayList<>();

    public RequestNews(Context context) {
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
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray newsArray = jsonObject.getJSONArray("news");
            JSONObject item;
            News news;
            for (int i = 0; i < newsArray.length(); i++) {
                item = newsArray.getJSONObject(i);
                news = new News(
                        item.getString("title"),
                        item.getString("pubDate"),
                        item.getString("description"),
                        item.getString("link")
                );
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e("JSONException:", Objects.requireNonNull(e.getMessage()));
        }
    }

    public List<News> getNewsList() {
        return newsList;
    }
}
