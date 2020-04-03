package de.itstall.freifunkfranken.view;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import de.itstall.freifunkfranken.R;

/*
 * Settings for the application
 */
public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Switch settingsSwOfflineRouter, settingsSwMapOfflineRouter;
    private EditText settingsTxtRouterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), 0);
        settingsSwOfflineRouter = findViewById(R.id.settingsSwOfflineRouter);
        settingsSwMapOfflineRouter = findViewById(R.id.settingsSwMapOfflineRouter);
        settingsTxtRouterCount = findViewById(R.id.settingsTxtRouterCount);

        // Set saved preferences to fields
        settingsTxtRouterCount.setText(String.valueOf(sharedPreferences.getInt("RouterCount", 10)));
        settingsSwOfflineRouter.setChecked(sharedPreferences.getBoolean("OfflineRouter", false));
        settingsSwMapOfflineRouter.setChecked(sharedPreferences.getBoolean("MapOfflineRouter", false));

        Button settingsBtnClose = findViewById(R.id.SettingsBtnClose);
        // save and close activity
        settingsBtnClose.setOnClickListener(v -> {
            saveSettings();
            finish();
        });
    }

    /*
     * save settings to preferences
     */
    private void saveSettings() {
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("OfflineRouter", settingsSwOfflineRouter.isChecked());
        editor.putBoolean("MapOfflineRouter", settingsSwMapOfflineRouter.isChecked());
        editor.putInt("RouterCount", Integer.parseInt(settingsTxtRouterCount.getText().toString()));
        editor.apply();
    }
}