package com.reactiverobot.latecounter.model;


import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ModelRoboTest extends AbstractRoboTest{

    @Override
    protected void setup() {
        // Do nothing.
    }

    @Override
    protected Module[] getModules() {
        return new Module[] {new ModelModule()};
    }

    @Test
    public void testCounterRecordsInjected() {
        assertThat(injector.getInstance(CounterRecords.class), is(notNullValue()));
    }

    @Test
    public void testCounterTypesInjected() {
        assertThat(injector.getInstance(CounterTypes.class), is(notNullValue()));
    }
}
