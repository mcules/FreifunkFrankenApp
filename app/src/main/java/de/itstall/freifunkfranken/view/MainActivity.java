package de.itstall.freifunkfranken.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Objects;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.FileDownloader;
import de.itstall.freifunkfranken.controller.MainActivityListener;

// main activity, no more explanation required i think
public class MainActivity extends AppCompatActivity {
    private final String timestampUrl = "https://fff-app.itstall.de/timestamp.txt";
    public boolean downloadDone = false;
    public int selectedTab;
    public SharedPreferences sharedPreferences;
    private TabLayout tabLayout;
    private final String timestampFile = "timestamp.txt";
    // --Commented out by Inspection (05.04.2020 22:12):private static final String TAG = MainActivity.class.getSimpleName();
    private Fragment fragment = null;
    private int timestamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityListener mainActivityListener = new MainActivityListener(this);

        // get preferences
        sharedPreferences = this.getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), 0);
        tabLayout = findViewById(R.id.tabLayout);

        // create app tabs
        createTab(getResources().getString(R.string.tabNews));
        createTab(getResources().getString(R.string.tabNextAp));
        createTab(getResources().getString(R.string.tabKarte));
        createTab(getResources().getString(R.string.tabSsids));

        tabLayout.addOnTabSelectedListener(mainActivityListener.onTabSelectedListener());

        // download datafile with content for app
        new FileDownloader(this, timestampUrl, timestampFile, getResources().getString(R.string.messageCheckingDataVersion)).execute();
    }

    // create new tab and add to layout
    private void createTab(String tabName) {
        TabLayout.Tab newTab = tabLayout.newTab();
        newTab.setText(tabName);
        tabLayout.addTab(newTab);
    }

    // creates the options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        return true;
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // check which fragment was clicked and return fragment class
    public Fragment getFragment(int selectedTab) {
        switch (selectedTab) {
            case 0:
                fragment = new NewsFragment();
                break;
            case 1:
                fragment = new NextApFragment();
                break;
            case 2:
                fragment = new MapsFragment();
                break;
            case 3:
                fragment = new SsidsFragment();
        }

        return fragment;
    }

    // load given fragment
    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // get saved tab from preferences
        int savedTab = sharedPreferences.getInt("selectedTab", 0);

        // reload Tab
        if (savedTab == 0) Objects.requireNonNull(tabLayout.getTabAt(1)).select();
        else Objects.requireNonNull(tabLayout.getTabAt(0)).select();

        // select tab
        Objects.requireNonNull(tabLayout.getTabAt(savedTab)).select();
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
                downloadDone = true;
                loadFragment(getFragment(selectedTab));
            }
        } else if (url.equals(downloadUrl)) {
            downloadDone = true;
            sharedPreferences.edit().putInt("DataFileTimestamp", this.timestamp).apply();
            loadFragment(getFragment(selectedTab));
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
}
