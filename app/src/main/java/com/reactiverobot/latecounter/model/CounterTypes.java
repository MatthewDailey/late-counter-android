package com.reactiverobot.latecounter.model;


import org.roboguice.shaded.goole.common.base.Optional;

import java.util.List;

public interface CounterTypes {

    /**
     * Load the {@link CounterType} by description, if such a type exists.
     *
     * @param description The description of the app widget.
     * @return {@link CounterType} associated with the given description, or absent if no such
     * type exists.
     */
    Optional<CounterType> getType(String description);

    /**
     * Load the {@link CounterType} associated to an app widget, if such a type exists.
     *
     * @param appWidgetId The id of the app widget.
     * @return {@link CounterType} associated with the given app widget id, or absent if no such
     * type exists.
     */
    Optional<CounterType> getTypeForWidget(int appWidgetId);

    /**
     * Load a list of all {@link CounterType} that have no associated app widget.
     *
     * @return List of all {@link CounterType} that have no associated app widget.
     */
    List<CounterType> loadTypesWithNoWidget();

    /**
     * Load all {@link CounterType}s.
     *
     * @return List of all counter types.
     */
    List<CounterType> loadAllTypes();

    /**
     * Dissociate the given app widget id from all {@link CounterType}s.
     * @param appWidgetId
     */
    void removeWidgetId(int appWidgetId);

    /**
     * Create new {@link CounterType} which must have a unique description and must be associated
     * with a new app widget (one which doesn't have an associated type).
     *
     * @param description Unique identifier of the new type. Must be non-empty.
     * @param appWidgetId Id of the app widget. Must not be shared by any other types.
     * @param colorId Color used to draw graphics associated with this type.
     * @return The new {@link CounterType}.
     * @throws CounterTypesException with user displayable message if:
     * <ul>
     *     <li>Description is empty.</li>
     *     <li>A type with description already exists.</li>
     *     <li>A type with appWidgetId already exists.</li>
     * </ul>
     */
    CounterType createUniqueTypeForWidget(String description, int appWidgetId, int colorId)
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

    /**
     * Deletes any {@link CounterType} which has description.
     *
     * @param description Unique identifying description of type to delete.
     */
    void deleteWithDescription(String description);

    /**
     * Updates the color associated with the given type. Will create a type with no widget if no
     * such type exists.
     *
     * @param description Description of the type to be created.
     * @param counterColorId New color id.
     * @return CounterType which was just updated or created.
     */
    CounterType updateColorForType(String description, int counterColorId);

    class CounterTypesException extends Exception {
        public CounterTypesException(String message) {
            super(message);
        }
    }
}
