package com.reactiverobot.latecounter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.analytics.CounterzAnalytics;

import roboguice.activity.RoboActivity;

public class PickCounterColorActivity extends RoboActivity {

    public static final int PICK_COLOR_REQUEST_CODE = 13857;
    public static final String COLOR_ID_EXTRA = "color_id_extra";

    @Inject
    CounterzAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        analytics.reportPickCounterColorActivity();

        setContentView(R.layout.activity_pick_color);

        setResultValueForButton(R.id.choose_black_button, android.R.color.black);
        setResultValueForButton(R.id.choose_blue_button, R.color.blue);
        setResultValueForButton(R.id.choose_red_button, R.color.red);
        setResultValueForButton(R.id.choose_green_button, R.color.green);
        setResultValueForButton(R.id.choose_yellow_button, R.color.yellow);
    }

    private void setResultValueForButton(int colorButton, final int color) {
        findViewById(colorButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(COLOR_ID_EXTRA, color);

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
