package de.itstall.freifunkfranken.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.FileDownloader;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public Fragment fragment = null;
    public boolean downloadDone = false;
    TabLayout tabLayout;
    public int selectedTab;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), 0);

        tabLayout = findViewById(R.id.tabLayout);

        final TabLayout.Tab nextApTab = tabLayout.newTab();
        TabLayout.Tab karteTab = tabLayout.newTab();
        karteTab.setText(getResources().getString(R.string.tabKarte));
        nextApTab.setText(getResources().getString(R.string.tabNextAp));
        TabLayout.Tab ssidsTab = tabLayout.newTab();
        ssidsTab.setText(getResources().getString(R.string.tabSsids));
        TabLayout.Tab newsTab = tabLayout.newTab();
        newsTab.setText(getResources().getString(R.string.tabNews));
        tabLayout.addTab(newsTab);
        tabLayout.addTab(nextApTab);
        tabLayout.addTab(karteTab);
        //tabLayout.addTab(ssidsTab);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab = tab.getPosition();
                if (downloadDone) loadFragment(getFragment(selectedTab));
                @SuppressLint("CommitPrefEdits") Editor editor = sharedPreferences.edit();
                editor.putInt("selectedTab", selectedTab);
                editor.apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        downloadFiles();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        return true;
    }

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

    public void downloadFiles() {
        FileDownloader fileDownloader = new FileDownloader(this, "https://fff-app.itstall.de/data.json", "data.json");
        fileDownloader.execute();
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int savedTab = sharedPreferences.getInt("selectedTab", 0);

        // reload Tab
        if (savedTab == 0) tabLayout.getTabAt(1).select();
        else tabLayout.getTabAt(0).select();

        tabLayout.getTabAt(savedTab).select();
    }
}
