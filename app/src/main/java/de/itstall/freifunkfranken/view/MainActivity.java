package de.itstall.freifunkfranken.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.FileDownloader;

// main activity, no more explanation required i think
public class MainActivity extends AppCompatActivity {
    private final String timestampUrl = "https://fff-app.itstall.de/timestamp.txt";
    public SharedPreferences sharedPreferences;
    private final String timestampFile = "timestamp.txt";
    private int timestamp = 0;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get preferences
        sharedPreferences = this.getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), 0);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.newsNav,
                R.id.nextApNav,
                R.id.karteNav,
                R.id.ssidsNav,
                R.id.wikiNav,
                R.id.vpnNav)
                .setDrawerLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateData();
    }

    // creates the options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    // option menu item clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionMenuBtnAbout:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.optionMenuBtnSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.optionMenuBtnUpdate:
                updateData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // download is done, check what is to do now
    public void downloadDone(String url) {
        String downloadUrl = "https://fff-app.itstall.de/data.json";
        if (url.equals(this.timestampUrl)) {
            timestamp = getTimestampFromFile();

            if (timestamp > sharedPreferences.getInt("DataFileTimestamp", 0)) {
                String downloadFile = "data.json";
                new FileDownloader(this, downloadUrl, downloadFile, getResources().getString(R.string.messageDownloadingData)).execute();
            } else {
                onResume();
            }
        } else if (url.equals(downloadUrl)) {
            sharedPreferences.edit().putInt("DataFileTimestamp", this.timestamp).apply();
            onResume();
        }
    }

    // get local saved Datafile timestamp
    private int getTimestampFromFile() {
        StringBuilder stringBuilder = new StringBuilder();
        File dataFile = new File(this.getFilesDir(), timestampFile);

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            Log.e("ReadWriteFile", "Unable to read file: " + timestampFile);
        }

        return Integer.parseInt(stringBuilder.toString());
    }

    // download datafile with content for app
    private void updateData() {
        new FileDownloader(this, timestampUrl, timestampFile, getResources().getString(R.string.messageCheckingDataVersion)).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
