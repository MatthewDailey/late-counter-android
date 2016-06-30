package com.reactiverobot.latecounter.activity;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.model.CounterTypes;

import roboguice.activity.RoboActivity;

public class CreateCounterTypeActivity extends RoboActivity {

    @Inject
    CounterTypes counterTypes;
}
