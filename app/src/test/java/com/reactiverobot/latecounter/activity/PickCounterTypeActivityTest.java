package com.reactiverobot.latecounter.activity;

import android.app.Activity;

import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.model.MockModelModule;

import org.junit.Rule;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertNotNull;

public class PickCounterTypeActivityTest extends AbstractRoboTest {

    @Rule
    public MockModelModule mockModelModule = new MockModelModule();

    private final int widgetId = 1;

    private Activity pickCounterTypeActivity;

    @Override
    protected void setup() {
        pickCounterTypeActivity =Robolectric.buildActivity(PickCounterTypeActivity.class)
                .withIntent(PickCounterTypeActivity.getStartIntent(context, widgetId))
                .create()
                .get();
    }

    @Override
    protected Module[] getModules() {
        return new Module[]{mockModelModule};
    }

    @Test
    public void testActivityCreated() {
        assertNotNull(pickCounterTypeActivity);
    }
}
