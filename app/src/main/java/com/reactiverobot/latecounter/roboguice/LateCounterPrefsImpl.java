package com.reactiverobot.latecounter.roboguice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.reactiverobot.latecounter.prefs.LateCounterPrefs;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class LateCounterPrefsImpl implements LateCounterPrefs {

    public static final String TODAYS_LATE_COUNT_PREF = "todays_late_count";
    private static final String TODAY_STRING_PREF = "today_string";

    private final Context context;

    @Inject
    public LateCounterPrefsImpl(Context context) {
        this.context = context;
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences("late_counter_prefs", Context.MODE_PRIVATE);
    }

    public int getTodaysLateCount() {
        String todayString = new SimpleDateFormat("MM/dd/yyyy").format(new Date()).toString();

        String lastRecordedIncrement = getPrefs().getString(TODAY_STRING_PREF, "unknown");

        Log.d("LateCounter-Prefs", "Historic counts: " + getPrefs().getString("all_counts", null));

        if (todayString.equals(lastRecordedIncrement)) {
            return getPrefs().getInt(TODAYS_LATE_COUNT_PREF, 0);
        } else {
            int yesterdayCount = getPrefs().getInt(TODAYS_LATE_COUNT_PREF, 0);

            if (yesterdayCount > 0) {
                List<Pair<Date, Integer>> dateToCount = getDateCountList();
                dateToCount.add(Pair.create(getYesterday(), yesterdayCount));

                Gson gson = new Gson();
                String historicDateToCount = gson.toJson(dateToCount);


                getPrefs().edit().putString("all_counts", historicDateToCount).apply();
            }

            getPrefs().edit()
                    .putString(TODAY_STRING_PREF, todayString)
                    .putInt(TODAYS_LATE_COUNT_PREF, 0)
                    .apply();
            return 0;
        }
    }

    private List<Pair<Date, Integer>> getDateCountList() {
        Type dateAndCountListType = new TypeToken<ArrayList<Pair<Date, Integer>>>() {}.getType();
        String allCountsList = getPrefs().getString("all_counts", null);
        if (allCountsList == null) {
            return new ArrayList<>();
        } else {
            Gson gson = new Gson();
            return gson.fromJson(allCountsList, dateAndCountListType);
        }
    }

    private Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    public void incrementLateCount() {
        Log.d("LateCounter-Prefs", "Called increment late count");
        int lateCount = getTodaysLateCount();
        getPrefs().edit().putInt(TODAYS_LATE_COUNT_PREF, lateCount + 1).apply();
    }


}
