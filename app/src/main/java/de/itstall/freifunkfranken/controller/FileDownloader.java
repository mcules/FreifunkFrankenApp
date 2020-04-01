package de.itstall.freifunkfranken.controller;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class FileDownloader extends AsyncTask<String, Void, String> {
    private final String filename;
    private final String downloadUrl;
    private ProgressDialog progressDialog = null;
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;

    public FileDownloader(MainActivity mainActivity, String downloadUrl, String filename) {
        this.mainActivity = mainActivity;
        this.downloadUrl = downloadUrl;
        this.filename = filename;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        StringBuilder stringBuilder = new StringBuilder();
        URL url;
        File saveFile;
        BufferedWriter writer;
        BufferedReader reader = null;
        String line;
        InputStream inputStream;

        try {
            url = new URL(downloadUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            reader = null;

            try {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuilder = new StringBuilder();
                line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                Log.i("length", stringBuilder.toString().length() + "");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) reader.close();

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

    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Downloading Data...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                de.itstall.freifunkfranken.controller.FileDownloader.this.cancel(true);
            }
        });
    }

    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        if (progressDialog != null) progressDialog.dismiss();
        mainActivity.downloadDone = true;
        mainActivity.loadFragment(mainActivity.getFragment(mainActivity.selectedTab));
    }
}
