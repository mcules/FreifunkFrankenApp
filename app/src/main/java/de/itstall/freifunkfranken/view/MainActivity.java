package de.itstall.freifunkfranken.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.FileDownloader;
import de.itstall.freifunkfranken.controller.MainActivityListener;

// main activity, no more explanation required i think
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Fragment fragment = null;
    public boolean downloadDone = false;
    public int selectedTab;
    public SharedPreferences sharedPreferences;
    private TabLayout tabLayout;

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
        new FileDownloader(this, "https://fff-app.itstall.de/data.json", "data.json").execute();
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
}
