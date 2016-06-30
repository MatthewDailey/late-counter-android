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

    CounterType createUniqueTypeForWidget(String description, int widgetId)
            throws FailureCreatingCounterTypeException;


    class FailureCreatingCounterTypeException extends Exception {
        public final String message;

        public FailureCreatingCounterTypeException(String message) {
            this.message = message;
        }
    }
}
