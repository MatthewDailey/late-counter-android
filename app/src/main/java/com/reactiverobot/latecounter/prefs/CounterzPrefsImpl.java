package com.reactiverobot.latecounter.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.inject.Inject;

class CounterzPrefsImpl implements CounterzPrefs {

    public static final String PREMIUM_ENABLED_PREF = "premium_enabled";
    private static final String USE_BAR_CHART_PREF = "use_bar_chart";
    private static final String IS_NOTIFICATION_ENABLED_PREF = "is_notifications_enabled";
    private final Context context;

    @Inject
    public CounterzPrefsImpl(Context context) {
        this.context = context;
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences("counterz_prefs", Context.MODE_PRIVATE);
    }

    @Override
    public boolean isPremiumEnabled() {
        return getPrefs().getBoolean(PREMIUM_ENABLED_PREF, false);
    }

    @Override
    public void enablePremium() {
        getPrefs().edit().putBoolean(PREMIUM_ENABLED_PREF, true).commit();
    }

    @Override
    public int getCounterLimit() {
        return 3;
    }

    @Override
    public void setShouldUseBarChart(boolean shouldUseBarChart) {
        getPrefs().edit().putBoolean(USE_BAR_CHART_PREF, shouldUseBarChart).commit();
    }

    @Override
    public boolean shouldUseBarChart() {
        return getPrefs().getBoolean(USE_BAR_CHART_PREF, false);
    }

    @Override
    public boolean isNotificationEnabled() {
        return getPrefs().getBoolean(IS_NOTIFICATION_ENABLED_PREF, false);
    }

    @Override
    public void setNotificationEnabled(boolean isNotificationEnabled) {
        getPrefs().edit().putBoolean(IS_NOTIFICATION_ENABLED_PREF, isNotificationEnabled).commit();
    }

    @Override
    public int getHoursBetweenNotifications() {
        return 6;
    }
}
