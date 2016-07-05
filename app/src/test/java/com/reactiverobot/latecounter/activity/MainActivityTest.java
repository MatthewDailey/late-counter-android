package com.reactiverobot.latecounter.activity;

import com.reactiverobot.latecounter.AbstractRoboTest;

import org.junit.Test;
import org.robolectric.Robolectric;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MainActivityTest extends AbstractRoboTest {

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
