package com.reactiverobot.latecounter.model;


import org.roboguice.shaded.goole.common.base.Optional;

import java.util.List;

public interface CounterTypes {
    CounterType createSafely(String description);

    CounterType createSafelyWithWidgetId(String description, int widgetId);

    Optional<CounterType> getType(String description);

    Optional<CounterType> getTypeForWidget(int widgetId);

    List<CounterType> loadTypesWithNoWidget();

    void removeWidgetId(int widgetId);
}
