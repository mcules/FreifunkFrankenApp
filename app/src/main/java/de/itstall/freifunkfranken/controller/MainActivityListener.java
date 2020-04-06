package de.itstall.freifunkfranken.controller;


import com.google.android.material.tabs.TabLayout;

import de.itstall.freifunkfranken.view.MainActivity;

// listener for mainactivity
public class MainActivityListener {
    private final MainActivity mainActivity;

    // consturctor adds mainactivity to class variable
    public MainActivityListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    // listener for selecting tabs
    public TabLayout.OnTabSelectedListener onTabSelectedListener() {
        return new TabLayout.OnTabSelectedListener() {
            // tab was selected
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainActivity.selectedTab = tab.getPosition();
                if (mainActivity.downloadDone)
                    //mainActivity.loadFragment(mainActivity.getFragment(mainActivity.selectedTab));
                    // save new tab to preferences
                mainActivity.sharedPreferences.edit().putInt("selectedTab", mainActivity.selectedTab).apply();
            }

            // tab was unselected
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            // tab was selected bevore and now again
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
    }
}
