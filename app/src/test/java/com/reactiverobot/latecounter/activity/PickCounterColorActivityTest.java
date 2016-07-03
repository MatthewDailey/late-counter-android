package com.reactiverobot.latecounter.activity;

import android.app.Activity;

import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.MockModelModule;

import org.junit.Rule;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

public class PickCounterColorActivityTest extends AbstractRoboTest {

    @Rule
    public MockModelModule mockModelModule = new MockModelModule();

    @Override
    protected Module[] getModules() {
        return new Module[]{mockModelModule};
    }

    @Override
    protected void setup() {
        activity = Robolectric.setupActivity(PickCounterColorActivity.class);
    }

    private PickCounterColorActivity activity;

    @Test
    public void testActivityStartedAndHasButtons() {
        assertNotNull(activity.findViewById(R.id.choose_black_button));
        assertNotNull(activity.findViewById(R.id.choose_yellow_button));
        assertNotNull(activity.findViewById(R.id.choose_red_button));
        assertNotNull(activity.findViewById(R.id.choose_green_button));
        assertNotNull(activity.findViewById(R.id.choose_blue_button));
    }

    @Test
    public void testReturnsResultWithColorValue_Black() {
        testClickingColorReturnsValue(R.id.choose_black_button, android.R.color.black);
    }

    @Test
    public void testReturnsResultWithColorValue_Blue() {
        testClickingColorReturnsValue(R.id.choose_blue_button, R.color.blue);
    }

    @Test
    public void testReturnsResultWithColorValue_Green() {
        testClickingColorReturnsValue(R.id.choose_green_button, R.color.green);
    }

    @Test
    public void testReturnsResultWithColorValue_Yellow() {
        testClickingColorReturnsValue(R.id.choose_yellow_button, R.color.yellow);
    }

    @Test
    public void testReturnsResultWithColorValue_Red() {
        testClickingColorReturnsValue(R.id.choose_red_button, R.color.red);
    }

    private void testClickingColorReturnsValue(int blackButton, int colorBlack) {
        activity.findViewById(blackButton).performClick();

        assertTrue(activity.isFinishing());
        assertThat(shadowOf(activity).getResultCode(), is(equalTo(Activity.RESULT_OK)));

        assertThat(shadowOf(activity)
                        .getResultIntent()
                        .getIntExtra(PickCounterColorActivity.COLOR_ID_EXTRA, -1),
                is(equalTo(colorBlack)));
    }
}
