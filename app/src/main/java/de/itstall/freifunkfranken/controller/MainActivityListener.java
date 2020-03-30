package de.itstall.freifunkfranken.controller;

import android.view.View;

import de.itstall.freifunkfranken.R;

public class MainActivityListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AboutBtnClose:
                break;
            case R.id.SettingsBtnClose:
                break;
        }
    }
}
