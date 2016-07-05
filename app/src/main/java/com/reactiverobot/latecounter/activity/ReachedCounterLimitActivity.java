package com.reactiverobot.latecounter.activity;

import android.os.Bundle;

import com.reactiverobot.latecounter.R;

import roboguice.activity.RoboActivity;

public class ReachedCounterLimitActivity extends RoboActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reached_counter_limit);
    }
}
