package com.reactiverobot.latecounter;

import android.app.Application;

import com.google.inject.Module;
import com.reactiverobot.latecounter.model.MockModelModule;
import com.reactiverobot.latecounter.prefs.PrefsModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;

import static org.mockito.Mockito.spy;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public abstract class AbstractRoboTest {

    @Rule
    public MockModelModule mockModelModule = new MockModelModule();

    protected Application context;
    protected RoboInjector injector;

    @Before
    public void setupContext() {
        context = spy(RuntimeEnvironment.application);

        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, getModules());
        injector = RoboGuice.getInjector(context);

        setup();
    }

    protected abstract void setup();

    /**
     * Override this to enable more detailed dependency injection mocks.
     */
    protected Module[] getModules() {
        return new Module[]{mockModelModule, new PrefsModule()};
    }

    @After
    public void cleanup() {
        Robolectric.reset();
    }
}
