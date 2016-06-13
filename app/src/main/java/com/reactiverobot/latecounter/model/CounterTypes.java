package com.reactiverobot.latecounter.model;


public interface CounterTypes {
    CounterType createSafely(String description);
    void dump();
}
