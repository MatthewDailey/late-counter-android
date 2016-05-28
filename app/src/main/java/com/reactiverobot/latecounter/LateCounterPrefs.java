package com.reactiverobot.latecounter;

import android.content.Context;
import android.content.SharedPreferences;

public class LateCounterPrefs {

    public static final String TODAYS_LATE_COUNT = "todays_late_count";
    private final Context context;

    public LateCounterPrefs(Context context) {
        this.context = context;
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences("late_counter_prefs", Context.MODE_PRIVATE);
    }

    public int getTodaysLateCount() {
        return getPrefs().getInt(TODAYS_LATE_COUNT, 0);
    }

    public void incrementLateCount() {
        int lateCount = getTodaysLateCount();
        getPrefs().edit().putInt(TODAYS_LATE_COUNT, lateCount + 1);
    }

    public void clearLateCount() {
        getPrefs().edit().remove(TODAYS_LATE_COUNT);
    }
}
