package com.reactiverobot.latecounter.model;

import java.util.Date;

public interface CounterRecords {
    CounterRecord create(CounterType type, Date date, int count);
    CounterRecord getTodaysCount(CounterType counterType);
}
