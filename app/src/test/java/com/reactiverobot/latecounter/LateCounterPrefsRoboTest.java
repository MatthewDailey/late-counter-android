package com.reactiverobot.latecounter;

import android.app.Application;

import com.reactiverobot.latecounter.prefs.LateCounterPrefs;
import com.reactiverobot.latecounter.roboguice.LateCounterInjectionModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import roboguice.RoboGuice;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LateCounterPrefsRoboTest {

    private Application context;

    @Before
    public void setupContext() {
        context = RuntimeEnvironment.application;

        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application,
                new LateCounterInjectionModule());

        instance = RoboGuice.getInjector(context).getInstance(LateCounterPrefs.class);
    }

    @After
    public void cleanup() {
        Robolectric.reset();
    }

    private LateCounterPrefs instance;

    @Test
    public void testCanGetLateCount() {
        assertThat(instance.getTodaysLateCount(), is(equalTo(0)));
    }

}
