package com.reactiverobot.latecounter.model;

import android.content.Context;
import android.view.View;

import java.util.Date;

public interface CounterRecords {
    CounterRecord create(CounterType type, Date date, int count);

    CounterRecord getTodaysCount(CounterType counterType);

    void incrementTodaysCount(CounterType counterType);

    View getPlotViewForType(Context context, CounterType counterType);
}
