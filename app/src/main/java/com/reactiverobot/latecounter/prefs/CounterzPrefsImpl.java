package com.reactiverobot.latecounter.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.inject.Inject;

class CounterzPrefsImpl implements CounterzPrefs {

    public static final String PREMIUM_ENABLED_PREF = "premium_enabled";
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
}
