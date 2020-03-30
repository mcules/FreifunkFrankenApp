package de.itstall.freifunkfranken.view;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import de.itstall.freifunkfranken.R;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Switch settingsSwOfflineRouter, settingsSwMapOfflineRouter;
    EditText settingsTxtRouterCount;
    Button settingsBtnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getApplicationContext().getSharedPreferences("FreifunkFrankenApp", 0);
        settingsSwOfflineRouter = (Switch) findViewById(R.id.settingsSwOfflineRouter);
        settingsSwMapOfflineRouter = (Switch) findViewById(R.id.settingsSwMapOfflineRouter);
        settingsTxtRouterCount = (EditText) findViewById(R.id.settingsTxtRouterCount);

        settingsTxtRouterCount.setText(String.valueOf(sharedPreferences.getInt("RouterCount", 10)));
        settingsBtnClose = (Button) findViewById(R.id.SettingsBtnClose);
        settingsSwOfflineRouter.setChecked(sharedPreferences.getBoolean("OfflineRouter", false));
        settingsSwMapOfflineRouter.setChecked(sharedPreferences.getBoolean("MapOfflineRouter", false));
        settingsBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                finish();
            }
        });
    }

    public void saveSettings() {
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("OfflineRouter", settingsSwOfflineRouter.isChecked());
        editor.putBoolean("MapOfflineRouter", settingsSwMapOfflineRouter.isChecked());
        editor.putInt("RouterCount", Integer.parseInt(settingsTxtRouterCount.getText().toString()));
        editor.apply();
    }
}