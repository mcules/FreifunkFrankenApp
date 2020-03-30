package de.itstall.freifunkfranken.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.FileDownloader;
import de.itstall.freifunkfranken.controller.MyLocationListener;

public class MainActivity extends AppCompatActivity {
    public Fragment fragment = null;
    public boolean downloadDone = false;
    TabLayout tabLayout;
    MyLocationListener myLocationListener;
    Button btnAboutClose;
    //MainActivityListener mainActivityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);

        final TabLayout.Tab nextApTab = tabLayout.newTab();
        TabLayout.Tab karteTab = tabLayout.newTab();
        karteTab.setText(getResources().getString(R.string.tabKarte));
        tabLayout.addTab(karteTab);
        nextApTab.setText(getResources().getString(R.string.tabNextAp));
        tabLayout.addTab(nextApTab);
        TabLayout.Tab ssidsTab = tabLayout.newTab();
        ssidsTab.setText(getResources().getString(R.string.tabSsids));
        tabLayout.addTab(ssidsTab);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new MapsFragment();
                        break;
                    case 1:
                        fragment = new NextApFragment();
                        break;
                    case 2:
                        fragment = new SsidsFragment();
                        break;
                }
                if (downloadDone) loadFragment(fragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        //btnAboutClose = findViewById(R.id.AboutBtnClose);
        //btnAboutClose.setOnClickListener(mainActivityListener);

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

    public void downloadFiles() {
        FileDownloader fileDownloader = new FileDownloader(this, "https://fff-app.itstall.de/data.json", "data.json");
        fileDownloader.execute();
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        assert fragment != null;
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        //myLocationListener = new MyLocationListener(this);
        //myLocationListener.getLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Objects.requireNonNull(tabLayout.getTabAt(1)).select();
        Objects.requireNonNull(tabLayout.getTabAt(0)).select();
    }
}
