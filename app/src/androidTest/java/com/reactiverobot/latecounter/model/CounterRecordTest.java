package com.reactiverobot.latecounter.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CounterRecordTest {

    @Rule
    public RealmSupplierRule realm = new RealmSupplierRule();

    private CounterRecords counterRecords;

    @Before
    public void setupCounterTypes() {
        counterRecords = new CounterRecordsRealmImpl(realm.getRealmSupplier());
    }

    @Test
    public void testGetTodaysCountStartsAtZero() {
        CounterType counterType = CounterType.withDescription("test-desc");
        CounterRecord todaysCount = counterRecords.getTodaysCount(counterType);
        assertThat(todaysCount.getCount(), is(equalTo(0)));
    }

    @Test
    public void testIncrementTodaysCount() {
        CounterType counterType = CounterType.withDescription("test-desc");
        counterRecords.incrementTodaysCount(counterType);
        CounterRecord todaysCount = counterRecords.getTodaysCount(counterType);
        assertThat(todaysCount.getCount(), is(equalTo(1)));
    }

    @Test
    public void testDoubleIncrementTodaysCount() {
        CounterType counterType = CounterType.withDescription("test-desc");
        counterRecords.incrementTodaysCount(counterType);
        counterRecords.incrementTodaysCount(counterType);
        CounterRecord todaysCount = counterRecords.getTodaysCount(counterType);
        assertThat(todaysCount.getCount(), is(equalTo(2)));
    }

    @Test
    public void testDeleteAllByType() {
        CounterType typeOne = CounterType.withDescription("type-one");
        CounterType typeTwo = CounterType.withDescription("type-two");

        counterRecords.incrementTodaysCount(typeOne);
        counterRecords.incrementTodaysCount(typeTwo);

        assertThat(counterRecords.getTodaysCount(typeOne).getCount(), is(equalTo(1)));
        assertThat(counterRecords.getTodaysCount(typeTwo).getCount(), is(equalTo(1)));

        counterRecords.deleteType(typeOne);

        assertThat(counterRecords.getTodaysCount(typeOne).getCount(), is(equalTo(0)));
        assertThat(counterRecords.getTodaysCount(typeTwo).getCount(), is(equalTo(1)));
    }
}
