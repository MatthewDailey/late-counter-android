package com.reactiverobot.latecounter.model;


import com.google.common.base.Optional;

public interface CounterTypes {
    CounterType createSafely(String description);

    CounterType createSafelyWithWidgetId(String description, int widgetId);

    Optional<CounterType> getType(String description);

    Optional<CounterType> getTypeForWidget(int widgetId);
}
