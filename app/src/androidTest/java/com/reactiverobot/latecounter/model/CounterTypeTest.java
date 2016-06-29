package com.reactiverobot.latecounter.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.roboguice.shaded.goole.common.base.Optional;

import static org.junit.Assert.assertFalse;

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

    }

    @Test
    public void testReadNonExistantType() {
        Optional<CounterType> counterTypeOption = counterTypes.getType("new-type");
        assertFalse(counterTypeOption.isPresent());
    }
}
