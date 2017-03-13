package com.reactiverobot.latecounter.prefs;

public interface CounterzPrefs {
    boolean isPremiumEnabled();
    void enablePremium();

    int getCounterLimit();

    void setShouldUseBarChart(boolean shouldUseBarChart);
    boolean shouldUseBarChart();

    boolean isNotificationEnabled();
    void setNotificationEnabled(boolean isNotificationEnabled);

    int getHoursBetweenNotifications();
}
