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
        assertThat(type.get().getWidgetid(), is(equalTo(2)));
    }
}
