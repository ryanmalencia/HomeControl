package com.example.ryanm.homecontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Settings extends AppCompatActivity {
    private SettingsHelper settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.settings));
        settings = new SettingsHelper(this.getApplicationContext());
        ((EditText)findViewById(R.id.serverip)).setText(settings.getIP());
    }

    public void saveSettings(View v) {
        String entry = ((EditText)findViewById(R.id.serverip)).getText().toString();
        settings.setIP(entry);
        boolean result = settings.saveSettings();
        if(!result) {
            findViewById(R.id.error).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.error).setVisibility(View.INVISIBLE);
        }
    }
}
