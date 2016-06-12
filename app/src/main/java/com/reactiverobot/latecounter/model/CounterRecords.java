package com.reactiverobot.latecounter.model;

import java.util.Date;

public interface CounterRecords {
    void create(CounterType type, Date date, int count);
}
