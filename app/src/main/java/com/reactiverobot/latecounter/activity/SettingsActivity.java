package com.reactiverobot.latecounter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.prefs.CounterzPrefs;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class SettingsActivity extends RoboActionBarActivity  {

    @Inject CounterzPrefs prefs;

    @InjectView(R.id.premium_checkbox) CheckBox premiumCheckbox;
    @InjectView(R.id.bar_chart_checkbox) CheckBox barChartCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        setupPremiumCheckbox();

        setupShouldUseBarChartCheckBox();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.isPremiumEnabled()) {
            premiumCheckbox.setChecked(true);
            premiumCheckbox.setClickable(false);
        }
    }

    private void setupPremiumCheckbox() {
        if (prefs.isPremiumEnabled()) {
            premiumCheckbox.setChecked(true);
            premiumCheckbox.setClickable(false);
        } else {
            premiumCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    premiumCheckbox.setChecked(false);
                    startActivity(new Intent(SettingsActivity.this, ReachedCounterLimitActivity.class));
                }
            });
        }
    }

    private void setupShouldUseBarChartCheckBox() {
        barChartCheckbox.setChecked(prefs.shouldUseBarChart());
        barChartCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setShouldUseBarChart(!prefs.shouldUseBarChart());
            }
        });
    }

}
