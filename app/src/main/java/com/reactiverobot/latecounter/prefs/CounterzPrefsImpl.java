package com.reactiverobot.latecounter.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.inject.Inject;

class CounterzPrefsImpl implements CounterzPrefs {

    private final Context context;

    @Inject
    public CounterzPrefsImpl(Context context) {
        this.context = context;
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences("counterz_prefs", Context.MODE_PRIVATE);
    }


}
