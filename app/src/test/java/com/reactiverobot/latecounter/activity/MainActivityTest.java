package com.reactiverobot.latecounter.activity;

import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.model.MockModelModule;

import org.junit.Rule;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MainActivityTest extends AbstractRoboTest {

    @Rule
    public MockModelModule mockModelModule = new MockModelModule();

    @Override
    protected Module[] getModules() {
        return new Module[]{mockModelModule};
    }

    private MainActivity activity;

    @Override
    protected void setup() {
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void testCanSetupActivity() {
        assertThat(activity, is(notNullValue()));
    }
}
