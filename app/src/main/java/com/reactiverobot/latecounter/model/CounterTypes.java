package com.reactiverobot.latecounter.model;


import org.roboguice.shaded.goole.common.base.Optional;

public interface CounterTypes {
    CounterType createSafely(String description);
    void dump();

    Optional<CounterType> getType(String description);
}
