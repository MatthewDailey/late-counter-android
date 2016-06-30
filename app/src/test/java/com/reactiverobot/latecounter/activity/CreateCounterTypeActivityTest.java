package com.reactiverobot.latecounter.activity;

import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.MockModelModule;

import org.junit.Rule;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class CreateCounterTypeActivityTest extends AbstractRoboTest {

    @Rule
    public MockModelModule mockModelModule = new MockModelModule();

    @Override
    protected Module[] getModules() {
        return new Module[]{mockModelModule};
    }

    private CreateCounterTypeActivity activity;

    @Override
    protected void setup() {
        activity = Robolectric.setupActivity(CreateCounterTypeActivity.class);
    }

    @Test
    public void testCanStartCreateCounterTypeActivity() {
        assertNotNull(activity);

        assertThat(activity.findViewById(R.id.create_counter_text), is(instanceOf(EditText.class)));
        assertThat(activity.findViewById(R.id.create_counter_submit), is(instanceOf(Button.class)));
        assertThat(activity.findViewById(R.id.create_counter_cancel), is(instanceOf(Button.class)));
    }

}
