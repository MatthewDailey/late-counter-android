package com.reactiverobot.latecounter.model;


import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.Date;

import io.realm.RealmResults;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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

        verify(realm).beginTransaction();
        verify(realm).commitTransaction();
        verify(realm).createObject(CounterRecord.class);
        verify(realm).close();
    }

    @Test
    public void testGetTodaysCount() {
        CounterType counterType = new CounterType();
        counterType.setDescription("test description");

        RealmResults realmResults = PowerMockito.mock(RealmResults.class);
        CounterRecord counterRecord = mock(CounterRecord.class);

        CounterRecords counterRecords = new CounterRecordsRealmImpl(realmSupplier);

        when(realm.where(CounterRecord.class)
                .greaterThanOrEqualTo("date", new Date())
                .equalTo("counterType.description",
                        counterType.getDescription())
                .findAll())
                .thenReturn(realmResults);

        when(realmResults.isEmpty()).thenReturn(false);
        when(realmResults.first()).thenReturn(counterRecord);

        CounterRecord todaysRecord = counterRecords.getTodaysCount(counterType);

        assertThat(todaysRecord, is(equalTo(counterRecord)));
    }

}
