package com.reactiverobot.latecounter.model;


import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;


public class CounterTypeRealmImplFastTest extends RealmMockingTest {

    @Test
    public void testCreate() {
        CounterType counterType = mock(CounterType.class);

        when(realm.createObject(CounterType.class)).thenReturn(counterType);

        new CounterTypesRealmImpl(realmSupplier).createSafely("sweet description");

        verify(counterType).setDescription("sweet description");

        verify(realm).executeTransaction(any(Realm.Transaction.class));
        verify(realm).createObject(CounterType.class);
        verify(realm).close();
    }

    @Test
    public void testPrimaryKeyFailiure() {
        CounterType counterType = PowerMockito.mock(CounterType.class);
        when(realm.createObject(CounterType.class)).thenReturn(counterType);

        PowerMockito.doThrow(new RealmPrimaryKeyConstraintException("Expected error, should be caught."))
                .when(counterType)
                .setDescription(any(String.class));

        new CounterTypesRealmImpl(realmSupplier).createSafely("sweet description");

        verify(counterType).setDescription("sweet description");
        verify(counterType).deleteFromRealm();
        verify(realm).executeTransaction(any(Realm.Transaction.class));
        verify(realm).createObject(CounterType.class);
        verify(realm).close();
    }
}
