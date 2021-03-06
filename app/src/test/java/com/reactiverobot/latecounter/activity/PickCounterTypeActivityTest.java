package com.reactiverobot.latecounter.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterRecord;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.prefs.CounterzPrefs;
import com.reactiverobot.latecounter.widget.GenericCounterWidget;

import org.junit.Test;
import org.roboguice.shaded.goole.common.base.Optional;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowAppWidgetManager;

import java.util.Date;

import static com.reactiverobot.latecounter.activity.PickCounterTypeActivity.WIDGET_ID_EXTRA;
import static com.reactiverobot.latecounter.activity.PickCounterTypeActivity.getStartIntent;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

public class PickCounterTypeActivityTest extends AbstractRoboTest {

    private ShadowAppWidgetManager shadowAppWidgetManager;
    private int widgetId;
    private Activity pickCounterTypeActivity;
    private CounterzPrefs prefs;

    @Override
    protected void setup() {
        when(mockModelModule.mockCounterTypes.getTypeForWidget(anyInt()))
                .thenReturn(Optional.<CounterType>absent());

        shadowAppWidgetManager = shadowOf(AppWidgetManager.getInstance(context));
        widgetId = shadowAppWidgetManager.createWidget(GenericCounterWidget.class, R.layout.counter_widget);
        prefs = injector.getInstance(CounterzPrefs.class);
    }

    private void setupActivity() {
        pickCounterTypeActivity = Robolectric.buildActivity(PickCounterTypeActivity.class)
                .withIntent(getStartIntent(context, widgetId))
                .create()
                .get();
    }

    @Test
    public void testActivityCreated() {
        setupActivity();

        assertNotNull(pickCounterTypeActivity);

        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);

        assertThat(typeList.getAdapter().getCount(), is(equalTo(2)));
    }

    @Test
    public void testHasTitleItem() {
        setupActivity();

        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);

        TextView headerTextView = (TextView) typeList.getAdapter()
                .getView(0, null, null)
                .findViewById(R.id.counter_type_list_item_name);
        assertThat(headerTextView.getText().toString(), is(equalTo("Pick a counter:")));
    }

    @Test
    public void testHasAddTypeFooter() {
        setupActivity();

        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);

        TextView headerTextView = (TextView) typeList.getAdapter()
                .getView(1, null, null)
                .findViewById(R.id.counter_type_list_item_name);
        assertThat(headerTextView.getText().toString(), is(equalTo("Add a new counter.")));
    }

    @Test
    public void testShowsCounterType() {
        when(mockModelModule.mockCounterTypes.loadTypesWithNoWidget())
                .thenReturn(Lists.newArrayList(CounterType.withDescription("type")));

        setupActivity();

        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);

        assertThat(typeList.getAdapter().getCount(), is(equalTo(3)));

        TextView headerTextView = (TextView) typeList.getAdapter()
                .getView(1, null, null)
                .findViewById(R.id.counter_type_list_item_name);
        assertThat(headerTextView.getText().toString(), is(equalTo("type")));
    }

    @Test
    public void testSetWidgetIdForTypeWhenClicked() {
        CounterType type = CounterType.withDescription("type");
        when(mockModelModule.mockCounterTypes.loadTypesWithNoWidget())
                .thenReturn(Lists.newArrayList(type));
        when(mockModelModule.mockCounterTypes.getTypeForWidget(widgetId))
                .thenReturn(Optional.of(type));
        when(mockModelModule.mockCounterRecords.getTodaysCount(type))
                .thenReturn(CounterRecord.create(new Date(), 1, type));

        setupActivity();

        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);

        typeList.getAdapter().getView(1, null, null).performClick();

        verify(mockModelModule.mockCounterTypes).updateWidgetForType("type", widgetId);

        View widgetView = shadowAppWidgetManager.getViewFor(widgetId);
        TextView countView = (TextView) widgetView.findViewById(R.id.count_text);
        assertThat(countView.getText().toString(), is(equalTo("1")));
        TextView countTitle = (TextView) widgetView.findViewById(R.id.count_description);
        assertThat(countTitle.getText().toString(), is(equalTo("type")));
    }

    @Test
    public void testClickingOnFooterLaunchesCreateCounterWhenNoTypesExist() {
        setupActivity();

        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);

        typeList.getAdapter().getView(1, null, null).performClick();

        Intent launchedIntent = shadowOf(pickCounterTypeActivity)
                .getNextStartedActivityForResult()
                .intent;
        assertThat(CreateCounterTypeActivity.class.getName(),
                is(equalTo(launchedIntent.getComponent().getClassName())));
        assertThat(launchedIntent.getIntExtra(WIDGET_ID_EXTRA, -1), is(equalTo(widgetId)));
    }

    @Test
    public void testClickingOnFooterLaunchesCreateCounterWhenPremium() {
        when(mockModelModule.mockCounterTypes.loadAllTypes())
                .thenReturn(Lists.newArrayList(
                        CounterType.withDescription("type1"),
                        CounterType.withDescription("type2"),
                        CounterType.withDescription("type3")));

        setupActivity();

        prefs.enablePremium();

        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);

        typeList.getAdapter().getView(1, null, null).performClick();

        Intent launchedIntent = shadowOf(pickCounterTypeActivity)
                .getNextStartedActivityForResult()
                .intent;
        assertThat(CreateCounterTypeActivity.class.getName(),
                is(equalTo(launchedIntent.getComponent().getClassName())));
        assertThat(launchedIntent.getIntExtra(WIDGET_ID_EXTRA, -1), is(equalTo(widgetId)));
    }

    @Test
    public void testClickingOnFooterLaunchesReachedLimitWithoutPremium() {
        when(mockModelModule.mockCounterTypes.loadAllTypes())
                .thenReturn(Lists.newArrayList(
                        CounterType.withDescription("type1"),
                        CounterType.withDescription("type2"),
                        CounterType.withDescription("type3")));

        setupActivity();

        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);

        typeList.getAdapter().getView(1, null, null).performClick();

        Intent launchedIntent = shadowOf(pickCounterTypeActivity)
                .getNextStartedActivityForResult()
                .intent;
        assertThat(ReachedCounterLimitActivity.class.getName(),
                is(equalTo(launchedIntent.getComponent().getClassName())));
    }

    @Test
    public void testReceiveOkCreateCounterTypeResult() {
        setupActivity();

        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);
        typeList.getAdapter().getView(1, null, null).performClick();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(WIDGET_ID_EXTRA, widgetId);

        shadowOf(pickCounterTypeActivity).receiveResult(
                new Intent(pickCounterTypeActivity, CreateCounterTypeActivity.class),
                Activity.RESULT_OK,
                resultIntent);

        assertTrue(pickCounterTypeActivity.isFinishing());

        assertThat(shadowOf(pickCounterTypeActivity).getBroadcastIntents().size(), is(equalTo(1)));
    }

    @Test
    public void testReceiveCancelledCreateCounterTypeResult() {
        setupActivity();

        ListView typeList = (ListView) pickCounterTypeActivity.findViewById(R.id.counter_type_list_view);
        typeList.getAdapter().getView(1, null, null).performClick();

        shadowOf(pickCounterTypeActivity).receiveResult(
                new Intent(pickCounterTypeActivity, CreateCounterTypeActivity.class),
                Activity.RESULT_CANCELED,
                new Intent());

        assertFalse(pickCounterTypeActivity.isFinishing());
    }
}
