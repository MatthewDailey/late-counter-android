package com.reactiverobot.latecounter.prefs;

import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;

import org.junit.Test;

import roboguice.RoboGuice;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class LateCounterPrefsRoboTest extends AbstractRoboTest{

    private LateCounterPrefs instance;

    @Override
    protected void setup() {
        instance = injector.getInstance(LateCounterPrefs.class);
    }

    @Override
    protected Module[] getModules() {
        return new Module[] {new PrefsModule()};
    }

    @Test
    public void testCanGetLateCount() {
        assertThat(instance.getTodaysLateCount(), is(equalTo(0)));
    }


}
