package de.itstall.freifunkfranken.controller;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.view.MainActivity;

// file downloader class. Downloads url to filename
public class FileDownloader extends AsyncTask<String, Void, String> {
    private static final String TAG = FileDownloader.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private final MainActivity mainActivity;
    private final String downloadUrl;
    private final String filename;
    private ProgressDialog progressDialog = null;
    private final String message;

    // constructor
    public FileDownloader(MainActivity mainActivity, String downloadUrl, String filename, String message) {
        this.mainActivity = mainActivity;
        this.downloadUrl = downloadUrl;
        this.filename = filename;
        this.message = message;
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

        if (!checkUrl()) return null;

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
        progressDialog.setMessage(this.message);
        progressDialog.show();
        progressDialog.setOnCancelListener(arg0 -> FileDownloader.this.cancel(true));
    }

    // download was finished. Close dialog and load fragment
    protected void onPostExecute(String string) {
        super.onPostExecute(string);

        if (progressDialog != null) progressDialog.dismiss();

        mainActivity.downloadDone(downloadUrl);
    }

    // check if url is reachable
    private boolean checkUrl() {
        int code = 0;

        try {
            URL url = new URL(this.downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            code = connection.getResponseCode();
            connection.disconnect();
        } catch (IOException e) {
            Toast
                    .makeText(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getResources().getString(R.string.errorUrlMalformed), Toast.LENGTH_LONG)
                    .show();
        }

        return code == 200;
    }
}
