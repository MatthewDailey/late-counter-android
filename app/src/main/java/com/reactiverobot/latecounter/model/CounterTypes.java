package com.reactiverobot.latecounter.model;


import org.roboguice.shaded.goole.common.base.Optional;

import java.util.List;

public interface CounterTypes {

    Optional<CounterType> getType(String description);

    Optional<CounterType> getTypeForWidget(int widgetId);

    List<CounterType> loadTypesWithNoWidget();

    List<CounterType> loadAllTypes();

    void removeWidgetId(int widgetId);

    CounterType createUniqueTypeForWidget(String description, int widgetId, int colorId)
            throws CounterTypesException;

    /**
     * Updates the app widget associated with a given type. Creates the type with color BLACK
     * if no such type exists.
     *
     * @param description Description of the type to be created.
     * @param widgetId Id of the app widget newly associated with the type.
     * @return {@link CounterType} which was updated or newly created.
     */
    CounterType updateWidgetForType(String description, int widgetId);

    void deleteWithDescription(String description);

    class CounterTypesException extends Exception {
        public final String message;

        public CounterTypesException(String message) {
            this.message = message;
        }
    }
}
