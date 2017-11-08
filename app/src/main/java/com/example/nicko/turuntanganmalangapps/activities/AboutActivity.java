package com.example.nicko.turuntanganmalangapps.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nicko.turuntanganmalangapps.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ttm_white_action_bar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
}
