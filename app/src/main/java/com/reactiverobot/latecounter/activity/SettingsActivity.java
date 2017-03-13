package com.reactiverobot.latecounter.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.analytics.CounterzAnalytics;
import com.reactiverobot.latecounter.notifications.ReminderReceiver;
import com.reactiverobot.latecounter.prefs.CounterzPrefs;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class SettingsActivity extends RoboActionBarActivity  {

    @Inject CounterzPrefs prefs;
    @Inject CounterzAnalytics analytics;

    @InjectView(R.id.premium_checkbox) CheckBox premiumCheckbox;
    @InjectView(R.id.bar_chart_checkbox) CheckBox barChartCheckbox;
    @InjectView(R.id.notification_checkbox) CheckBox notificationsCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        analytics.reportSettingsActivity();

        setContentView(R.layout.activity_settings);

        setupPremiumCheckbox();
        setupNotificationsCheckBox();
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
                analytics.reportToggledUseBarChart();
                prefs.setShouldUseBarChart(!prefs.shouldUseBarChart());
            }
        });
    }

    private void setupNotificationsCheckBox() {
        notificationsCheckbox.setChecked(prefs.isNotificationEnabled());
        notificationsCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newIsNotificationsEnabled = !prefs.isNotificationEnabled();

                if (newIsNotificationsEnabled) {
                    startReminderNotifications();
                } else {
                    cancelReminderNotifications();
                }

                prefs.setNotificationEnabled(newIsNotificationsEnabled);
            }
        });
    }

    private PendingIntent getAlarmIntent() {
        Intent reminderIntent = new Intent(this, ReminderReceiver.class);
        return PendingIntent.getBroadcast(this, 0, reminderIntent, 0);
    }

    private void startReminderNotifications() {
        Log.d("SettingsActivity", "start reminders");

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 3000,
            3000,
            getAlarmIntent());
    }

    private void cancelReminderNotifications() {
        Log.d("SettingsActivity", "cancel reminders");

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getAlarmIntent());
    }
}
