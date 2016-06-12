package com.reactiverobot.latecounter.model;


import org.junit.Test;

import java.util.Date;

import io.realm.Realm;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

public class CounterRecordsRealmImplFastTest extends RealmMockingTest {

    @Test
    public void testCreate() {
        CounterType counterType = mock(CounterType.class);
        CounterRecord counterRecord = mock(CounterRecord.class);

        when(realm.createObject(CounterRecord.class)).thenReturn(counterRecord);

        Date testDate = new Date();
        int testCount = 2;

        new CounterRecordsRealmImpl(realmSupplier).create(counterType, testDate, testCount);

        verify(counterRecord).setDate(testDate);
        verify(counterRecord).setCount(testCount);
        verify(counterRecord).setCounterType(counterType);

        verify(realm).executeTransaction(any(Realm.Transaction.class));
        verify(realm).createObject(CounterRecord.class);
        verify(realm).close();
    }

}
