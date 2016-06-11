package com.reactiverobot.latecounter;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LateCounterPrefsRoboTest {

    private Context context;

    @Before
    public void setupContext() {
        context = RuntimeEnvironment.application;
    }

    @After
    public void cleanup() {
        Robolectric.reset();
    }

    @Test
    public void testCanGetLateCount() {

    }
}
