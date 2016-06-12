package com.reactiverobot.latecounter.prefs;

public interface LateCounterPrefs {
    int getTodaysLateCount();
    void incrementLateCount();
}
