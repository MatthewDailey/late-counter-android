package com.reactiverobot.latecounter.activity;

import android.app.Activity;
import android.widget.ListView;
import android.widget.TextView;

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

public class PickCounterTypeActivityTest extends AbstractRoboTest {

    @Rule
    public MockModelModule mockModelModule = new MockModelModule();

    private final int widgetId = 1;

    private Activity pickCounterTypeActivity;

    @Override
    protected void setup() {
        pickCounterTypeActivity = Robolectric.buildActivity(PickCounterTypeActivity.class)
                .withIntent(PickCounterTypeActivity.getStartIntent(context, widgetId))
                .create()
                .get();
    }

    @Override
    protected Module[] getModules() {
        return new Module[]{mockModelModule};
    }

    @Test
    public void testActivityCreated() {
        assertNotNull(pickCounterTypeActivity);

        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);

        assertThat(typeList.getAdapter().getCount(), is(equalTo(2)));
    }

    @Test
    public void testHasTitleItem() {
        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);

        TextView headerTextView = (TextView) typeList.getAdapter()
                .getView(0, null, null)
                .findViewById(R.id.counter_type_list_item_name);;
        assertThat(headerTextView.getText().toString(), is(equalTo("Pick a counter:")));
    }



}
