package com.reactiverobot.latecounter.activity;

import android.os.Bundle;

import com.reactiverobot.latecounter.R;

import roboguice.activity.RoboActivity;

public class PickCounterColorActivity extends RoboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pick_color);
    }
}
