package de.itstall.freifunkfranken.controller;


import com.google.android.material.tabs.TabLayout;

import de.itstall.freifunkfranken.view.MainActivity;

public class MainActivityListener {
    private MainActivity mainActivity;

    public MainActivityListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public TabLayout.OnTabSelectedListener onTabSelectedListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainActivity.selectedTab = tab.getPosition();
                if (mainActivity.downloadDone)
                    mainActivity.loadFragment(mainActivity.getFragment(mainActivity.selectedTab));
                mainActivity.sharedPreferences.edit().putInt("selectedTab", mainActivity.selectedTab).apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
    }
}
