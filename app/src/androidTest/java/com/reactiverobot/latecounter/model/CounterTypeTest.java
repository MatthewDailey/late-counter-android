package com.reactiverobot.latecounter.model;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.roboguice.shaded.goole.common.base.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CounterTypeTest {

    @Rule
    public RealmSupplierRule realm = new RealmSupplierRule();

    private CounterTypes counterTypes;

    @Before
    public void setupCounterTypes() {
        counterTypes = new CounterTypesRealmImpl(realm.getRealmSupplier());
    }

    @Test
    public void testCreateCounterType() {
        counterTypes.createSafely("new-type");
        assertTrue(counterTypes.getType("new-type").isPresent());
    }

    @Test
    public void testReadNonExistantType() {
        assertFalse(counterTypes.getType("new-type").isPresent());
    }

    @Test
    public void testCreateSafely() {
        counterTypes.createSafely("new-type");
        counterTypes.createSafely("new-type");
        assertTrue(counterTypes.getType("new-type").isPresent());
    }

    @Test
    public void testCreateWithWidgetId() {
        CounterType type = counterTypes.createSafelyWithWidgetId("new-type", 1);
        Optional<CounterType> typeOptional = counterTypes.getType("new-type");
        assertThat(type, is(equalTo(typeOptional.get())));
    }

    @Test
    public void testCreateWithPreExistingWidgetId() {
        counterTypes.createSafelyWithWidgetId("new-type", 1);
        counterTypes.createSafelyWithWidgetId("new-type", 2);
        Optional<CounterType> type = counterTypes.getType("new-type");
        assertThat(type.get().getWidgetId(), is(equalTo(2)));
    }

    @Test
    public void testLoadTypeByWidgetId() {
        CounterType originalType = counterTypes.createSafelyWithWidgetId("new-type", 1);

        Optional<CounterType> type = counterTypes.getTypeForWidget(1);
        assertThat(type.get(), is(equalTo(originalType)));
    }

    @Test
    public void testLoadNonExistantTypeByWidgetId() {
        counterTypes.createSafelyWithWidgetId("new-type", 1);

        Optional<CounterType> type = counterTypes.getTypeForWidget(2);
        assertThat(type.isPresent(), is(false));
    }

    @Test
    public void testLoadAllTypesWithNoWidget() {
        counterTypes.createSafely("no-widget");
        counterTypes.createSafelyWithWidgetId("widget", 1);

        assertThat(counterTypes.loadTypesWithNoWidget().size(), is(equalTo(1)));
    }

    @Test
    public void testRemoveWidgetIdForType() {
        CounterType type = counterTypes.createSafelyWithWidgetId("widget", 1);

        assertThat(counterTypes.loadTypesWithNoWidget().size(), is(equalTo(0)));

        counterTypes.removeWidgetId(type.getWidgetId());

        assertThat(counterTypes.loadTypesWithNoWidget().size(), is(equalTo(1)));
    }

    @Test
    public void testCreateUnique() throws CounterTypes.FailureCreatingCounterTypeException {
        counterTypes.createUniqueTypeForWidget("new-type", 1, android.R.color.black);

        Optional<CounterType> type = counterTypes.getType("new-type");
        assertThat(type.get().getWidgetId(), is(equalTo(1)));
        assertThat(type.get().getColorId(), is(equalTo(android.R.color.black)));
    }

    @Test(expected = CounterTypes.FailureCreatingCounterTypeException.class)
    public void testCreateUniqueWithDuplicateDescription()
            throws CounterTypes.FailureCreatingCounterTypeException {
        counterTypes.createUniqueTypeForWidget("new-type", 1, android.R.color.black);
        counterTypes.createUniqueTypeForWidget("new-type", 2, 2);
    }

    @Test(expected = CounterTypes.FailureCreatingCounterTypeException.class)
    public void testCreateUniqueWithDuplicateWidgetid()
            throws CounterTypes.FailureCreatingCounterTypeException {
        counterTypes.createUniqueTypeForWidget("new-type", 1, android.R.color.black);
        counterTypes.createUniqueTypeForWidget("different-new-type", 1, android.R.color.black);
    }

    @Test(expected = CounterTypes.FailureCreatingCounterTypeException.class)
    public void testCreateUniqueWithEmptyDescription()
            throws CounterTypes.FailureCreatingCounterTypeException {
        counterTypes.createUniqueTypeForWidget("", 1, android.R.color.black);
    }

    @Test
    public void testDeleteByDescription() {
        counterTypes.createSafely("type");

        assertThat(counterTypes.getType("type").isPresent(), is(true));

        counterTypes.deleteWithDescription("diff-type");

        assertThat(counterTypes.getType("type").isPresent(), is(true));

        counterTypes.deleteWithDescription("type");

        assertThat(counterTypes.getType("type").isPresent(), is(false));
    }
}
