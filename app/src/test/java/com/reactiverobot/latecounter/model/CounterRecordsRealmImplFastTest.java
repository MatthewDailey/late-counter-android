package com.reactiverobot.latecounter.model;


import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import io.realm.Realm;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

public class CounterRecordsRealmImplFastTest extends RealmMockingTest {

    private CounterType counterType;

    @Before
    public void setup() {
        counterType = mock(CounterType.class);
    }

    @Test
    public void testCreate() {
        CounterRecords counterRecords = new CounterRecordsRealmImpl(realmSupplier);

        when(realm.createObject(CounterRecord.class)).thenReturn(new CounterRecord());

        counterRecords.create(counterType, new Date(), 2);

        verify(realm).executeTransaction(any(Realm.Transaction.class));
        verify(realm).createObject(CounterRecord.class);
        verify(realm).close();
    }

}
