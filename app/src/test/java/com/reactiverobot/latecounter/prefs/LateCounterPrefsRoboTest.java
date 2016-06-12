package com.reactiverobot.latecounter.prefs;

import android.app.Application;

import com.reactiverobot.latecounter.BuildConfig;

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
                new PrefsModule() /* note that modules can be chained here */);

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
