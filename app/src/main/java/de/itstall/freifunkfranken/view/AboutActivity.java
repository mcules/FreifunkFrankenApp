package de.itstall.freifunkfranken.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
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
        TextView aboutTvLizencesApp = findViewById(R.id.aboutTvLizencesApp);

        aboutTvLizencesApp.setText(Html.fromHtml(getResources().getString(R.string.aboutTvAppLizence)));
        aboutTvLizencesApp.setMovementMethod(LinkMovementMethod.getInstance());

        aboutBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
