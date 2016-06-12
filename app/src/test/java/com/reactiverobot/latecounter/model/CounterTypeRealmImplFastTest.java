package com.reactiverobot.latecounter.model;


import org.junit.Test;

import io.realm.Realm;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

public class CounterTypeRealmImplFastTest extends RealmMockingTest {

    @Test
    public void testCreate() {
        CounterType counterType = mock(CounterType.class);
        when(realm.createObject(CounterType.class)).thenReturn(counterType);

        new CounterTypesRealmImpl(realmSupplier).create("sweet description");

        verify(counterType).setDescription("sweet description");
        verify(realm).executeTransaction(any(Realm.Transaction.class));
        verify(realm).createObject(CounterType.class);
        verify(realm).close();
    }
}
