package com.reactiverobot.latecounter.activity;

import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.R;

import org.junit.Test;
import org.robolectric.Robolectric;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ReachedCounterLimitActivityTest extends AbstractRoboTest {

    private ReachedCounterLimitActivity activity;

    @Override
    protected void setup() {
        activity = Robolectric.setupActivity(ReachedCounterLimitActivity.class);
    }

    @Test
    public void testSetContent() {
        assertThat(activity.findViewById(R.id.reached_limit_buy_button), is(notNullValue()));
    }

}
