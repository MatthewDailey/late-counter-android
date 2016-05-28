package com.reactiverobot.latecounter;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LateCounterPrefs {

    public static final String TODAYS_LATE_COUNT_PREF = "todays_late_count";
    private static final String TODAY_STRING_PREF = "today_string";

    private final Context context;

    public LateCounterPrefs(Context context) {
        this.context = context;
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences("late_counter_prefs", Context.MODE_PRIVATE);
    }

    public int getTodaysLateCount() {
        String todayString = new SimpleDateFormat("MM/dd/yyyy").format(new Date()).toString();

        String lastRecordedIncrement = getPrefs().getString(TODAY_STRING_PREF, "unknown");

        if (todayString.equals(lastRecordedIncrement)) {
            return getPrefs().getInt(TODAYS_LATE_COUNT_PREF, 0);
        } else {
            getPrefs().edit()
                    .putString(TODAY_STRING_PREF, todayString)
                    .putInt(TODAYS_LATE_COUNT_PREF, 0)
                    .apply();
            return 0;
        }
    }

    public void incrementLateCount() {
        int lateCount = getTodaysLateCount();
        getPrefs().edit().putInt(TODAYS_LATE_COUNT_PREF, lateCount + 1).apply();
    }

}
