package com.reactiverobot.latecounter.prefs;

import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
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

    @Test
    public void testCanCheckIfPremium() {
        assertThat(instance.isPremiumEnabled(), is(false));
    }

    @Test
    public void testCanEnablePremium() {
        instance.enablePremium();
        assertThat(instance.isPremiumEnabled(), is(true));
    }

    @Test
    public void testGetCounterLimit() {
        assertThat(instance.getCounterLimit(), is(equalTo(3)));
    }

    @Test
    public void testUseBarChart() {
        assertThat(instance.shouldUseBarChart(), is(false));
    }

    @Test
    public void testSetUseBarChart() {
        instance.setShouldUseBarChart(true);
        assertThat(instance.shouldUseBarChart(), is(true));
    }

    @Test
    public void testNotificationsEnabled() {
        assertThat(instance.isNotificationEnabled(), is(false));
    }

    @Test
    public void testSetNotificationsEnabled() {
        instance.setNotificationEnabled(true);
        assertThat(instance.isNotificationEnabled(), is(true));
    }
}
