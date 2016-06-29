package com.reactiverobot.latecounter.model;

import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleRealmSmokeTest {

    @Rule
    public RealmSupplierRule realm = new RealmSupplierRule();

    @Test
    public void simpleRealmTest() {
        CounterType counterType = CounterType.withDescription("desc");

        CounterRecordsRealmImpl counterRecords = new CounterRecordsRealmImpl(realm.getRealmSupplier());

        CounterRecord counterRecordYesterday = counterRecords.create(counterType, new Date(-1), 0);

        CounterRecord counterRecordToday = counterRecords.create(counterType, new Date(), 0);

        CounterRecord todaysCount = counterRecords.getTodaysCount(counterType);

        assertThat(todaysCount, is(equalTo(counterRecordToday)));
    }
}
