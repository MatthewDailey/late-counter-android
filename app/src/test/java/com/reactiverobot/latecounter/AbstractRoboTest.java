package com.reactiverobot.latecounter;

import android.app.Application;

import com.google.inject.Module;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public abstract class AbstractRoboTest {

    protected Application context;
    protected RoboInjector injector;

    @Before
    public void setupContext() {
        context = RuntimeEnvironment.application;

        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, getModules());
        injector = RoboGuice.getInjector(context);

        setup();
    }

    protected abstract void setup();

    protected abstract Module[] getModules();

    @After
    public void cleanup() {
        Robolectric.reset();
    }
}
