package com.reactiverobot.latecounter.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterTypes;
import com.reactiverobot.latecounter.model.MockModelModule;

import org.junit.Rule;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.mockito.Mockito.verify;

public class CreateCounterTypeActivityTest extends AbstractRoboTest {

    @Rule
    public MockModelModule mockModelModule = new MockModelModule();

    @Override
    protected Module[] getModules() {
        return new Module[]{mockModelModule};
    }

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
    public void testCreateButtonCreatesCounterType() throws CounterTypes.FailureCreatingCounterTypeException {
        newCounterNameText.setText("new type");

        submitButton.performClick();

        verify(mockModelModule.counterTypes).createUniqueTypeForWidget("new type", testAppWidgetId);
    }

}
