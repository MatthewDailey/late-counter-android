package com.reactiverobot.latecounter.activity;

import android.os.Bundle;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.prefs.CounterzPrefs;

import roboguice.activity.RoboActionBarActivity;

public class SettingsActivity extends RoboActionBarActivity  {

    @Inject CounterzPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
    }

}
