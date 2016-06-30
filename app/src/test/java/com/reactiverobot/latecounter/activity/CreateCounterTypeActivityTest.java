package com.reactiverobot.latecounter.activity;

import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.model.MockModelModule;

import org.junit.Rule;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertNotNull;

public class CreateCounterTypeActivityTest extends AbstractRoboTest {

    @Rule
    public MockModelModule mockModelModule = new MockModelModule();

    @Override
    protected Module[] getModules() {
        return new Module[]{mockModelModule};
    }

    @Override
    protected void setup() {

    }

    @Test
    public void testCanStartCreateCounterTypeActivity() {
        assertNotNull(Robolectric.setupActivity(CreateCounterTypeActivity.class));
    }

}
