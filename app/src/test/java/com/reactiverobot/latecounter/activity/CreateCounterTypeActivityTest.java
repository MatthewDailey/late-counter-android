package com.reactiverobot.latecounter.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterTypes;

import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowToast;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

public class CreateCounterTypeActivityTest extends AbstractRoboTest {

    private final int testAppWidgetId = 2;

    private CreateCounterTypeActivity activity;
    private EditText newCounterNameText;
    private Button submitButton;
    private Button cancelButton;

    @Override
    protected void setup() {
        Intent startCreateCounterTypeActivityIntent = new Intent(context,
                CreateCounterTypeActivity.class);
        startCreateCounterTypeActivityIntent.putExtra(PickCounterTypeActivity.WIDGET_ID_EXTRA, testAppWidgetId);
        activity = Robolectric.buildActivity(CreateCounterTypeActivity.class)
            .withIntent(startCreateCounterTypeActivityIntent)
            .create()
            .get();

        newCounterNameText = (EditText) activity.findViewById(R.id.create_counter_text);
        submitButton = (Button) activity.findViewById(R.id.create_counter_submit);
        cancelButton = (Button) activity.findViewById(R.id.create_counter_cancel);
    }

    @Test
    public void testCreateButtonCreatesCounterType() throws CounterTypes.CounterTypesException {
        newCounterNameText.setText("new type");

        submitButton.performClick();

        verify(mockModelModule.counterTypes).createUniqueTypeForWidget("new type", testAppWidgetId, android.R.color.black);
    }

    @Test
    public void testShowToastWithErrorMessageIfFailToCreate()
            throws CounterTypes.CounterTypesException {
        newCounterNameText.setText("new type");

        doThrow(new CounterTypes.CounterTypesException("test message"))
            .when(mockModelModule.counterTypes).createUniqueTypeForWidget("new type", testAppWidgetId, android.R.color.black);

        submitButton.performClick();

        assertThat(ShadowToast.getTextOfLatestToast(), is(equalTo("test message")));
    }

    @Test
    public void testCancelButtonFinishes() {
        cancelButton.performClick();

        assertTrue(activity.isFinishing());
    }

    @Test
    public void testReturnsResultOkOnComplete() {
        newCounterNameText.setText("new type");

        submitButton.performClick();

        assertTrue(activity.isFinishing());
        assertThat(shadowOf(activity).getResultCode(), is(equalTo(Activity.RESULT_OK)));
        assertThat(shadowOf(activity)
                .getResultIntent()
                .getIntExtra(PickCounterTypeActivity.WIDGET_ID_EXTRA, -1),
                is(equalTo(testAppWidgetId)));
    }
}
