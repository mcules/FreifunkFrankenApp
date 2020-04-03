package de.itstall.freifunkfranken.controller;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import de.itstall.freifunkfranken.view.MainActivity;

// file downloader class. Downloads url to filename
public class FileDownloader extends AsyncTask<String, Void, String> {
    private final String filename;
    private final String downloadUrl;
    private ProgressDialog progressDialog = null;
    @SuppressLint("StaticFieldLeak")
    private final MainActivity mainActivity;

    // constructor
    public FileDownloader(MainActivity mainActivity, String downloadUrl, String filename) {
        this.mainActivity = mainActivity;
        this.downloadUrl = downloadUrl;
        this.filename = filename;
    }

    // start task in background
    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        StringBuilder stringBuilder = new StringBuilder();
        URL url;
        File saveFile;
        BufferedWriter writer;
        BufferedReader reader;
        String line;
        InputStream inputStream;

        // try to download file from url
        try {
            url = new URL(downloadUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            reader = null;

            try {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                Log.i("length", stringBuilder.toString().length() + "");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) reader.close();

                // save file to local storage
                try {
                    saveFile = new File(Objects.requireNonNull(mainActivity).getFilesDir(), filename);
                    if (!saveFile.exists()) saveFile.createNewFile();

                    writer = new BufferedWriter(new FileWriter(saveFile, false));
                    writer.write(stringBuilder.toString());
                    writer.close();
                } catch (IOException e) {
                    Log.e("ReadWriteFile", "Unable to write to file: " + filename);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }

        return null;
    }

    // start dialog to inform that a download is started now
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Downloading Data...");
        progressDialog.show();
        progressDialog.setOnCancelListener(arg0 -> FileDownloader.this.cancel(true));
    }

    // download was finished. Close dialog and load fragment
    protected void onPostExecute(String string) {
        super.onPostExecute(string);

        if (progressDialog != null) progressDialog.dismiss();

        mainActivity.downloadDone = true;
        mainActivity.loadFragment(mainActivity.getFragment(mainActivity.selectedTab));
    }
}
