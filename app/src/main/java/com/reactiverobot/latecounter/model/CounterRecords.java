package com.reactiverobot.latecounter.model;

import java.util.Date;
import java.util.List;

public interface CounterRecords {
    CounterRecord create(CounterType type, Date date, int count);

    CounterRecord getTodaysCount(CounterType counterType);

    void incrementTodaysCount(CounterType counterType);

    List<CounterRecord> loadAllForTypeOrderedByDate(CounterType counterType);
}
