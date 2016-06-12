package com.reactiverobot.latecounter;

public interface ILateCounterPrefs {
    int getTodaysLateCount();
    void incrementLateCount();
}
