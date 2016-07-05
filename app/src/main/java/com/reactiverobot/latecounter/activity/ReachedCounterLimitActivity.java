package com.reactiverobot.latecounter.activity;

import android.os.Bundle;
import android.view.View;

import com.reactiverobot.latecounter.R;

import roboguice.activity.RoboActivity;

public class ReachedCounterLimitActivity extends RoboActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reached_counter_limit);

        findViewById(R.id.reached_limit_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
