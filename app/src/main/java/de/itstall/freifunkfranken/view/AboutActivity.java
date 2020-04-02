package de.itstall.freifunkfranken.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.itstall.freifunkfranken.R;

public class AboutActivity extends AppCompatActivity {
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button aboutBtnClose = findViewById(R.id.AboutBtnClose);
        TextView aboutTvLicencesApp = findViewById(R.id.aboutTvLizencesApp);

        aboutTvLicencesApp.setText(Html.fromHtml(getResources().getString(R.string.aboutTvAppLizence)));
        aboutTvLicencesApp.setMovementMethod(LinkMovementMethod.getInstance());

        aboutBtnClose.setOnClickListener(v -> finish());
    }
}
