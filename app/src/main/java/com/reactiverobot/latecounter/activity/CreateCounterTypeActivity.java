package com.reactiverobot.latecounter.activity;

import android.os.Bundle;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterTypes;

import roboguice.activity.RoboActivity;

public class CreateCounterTypeActivity extends RoboActivity {

    @Inject
    CounterTypes counterTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_counter_type_layout);
    }
}
