package com.reactiverobot.latecounter.prefs;

import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CounterzPrefsRoboTest extends AbstractRoboTest{

    private CounterzPrefs instance;

    @Override
    protected void setup() {
        instance = injector.getInstance(CounterzPrefs.class);
    }

    @Override
    protected Module[] getModules() {
        return new Module[] {new PrefsModule()};
    }

    @Test
    public void testCanInject() {
        assertThat(instance, is(notNullValue()));
    }


}
