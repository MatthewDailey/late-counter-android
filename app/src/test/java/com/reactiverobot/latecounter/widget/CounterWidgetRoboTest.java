package com.reactiverobot.latecounter.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.activity.PickCounterTypeActivity;
import com.reactiverobot.latecounter.model.CounterRecord;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.MockModelModule;
import com.reactiverobot.latecounter.prefs.PrefsModule;

import org.junit.Rule;
import org.junit.Test;
import org.roboguice.shaded.goole.common.base.Optional;
import org.robolectric.shadows.ShadowAppWidgetManager;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

public class CounterWidgetRoboTest extends AbstractRoboTest {

    private CounterRecord counterRecord;
    private CounterType counterType;

    private ShadowAppWidgetManager shadowAppWidgetManager;
    private int widgetId;

    @Rule
    public MockModelModule mockModelModule = new MockModelModule();
    private Intent nextStartedActivity;
    private Intent broadcastIntent;

    @Override
    protected void setup() {
        counterType = new CounterType();
        counterType.setDescription("desc");
        counterType.setWidgetId(1);

        counterRecord = new CounterRecord();
        counterRecord.setCount(10);

        when(mockModelModule.counterTypes.getTypeForWidget(anyInt()))
                .thenReturn(Optional.<CounterType>absent());
    }

    private void createWidget() {
        shadowAppWidgetManager =
                shadowOf(AppWidgetManager.getInstance(context));
        widgetId = shadowAppWidgetManager
                .createWidget(GenericCounterWidget.class, R.layout.counter_widget);
    }

    @Override
    protected Module[] getModules() {
        return new Module[]{mockModelModule, new PrefsModule()};
    }

    @Test
    public void testInstantiateWidget() {
        createWidget();

        AppWidgetProvider appWidgetProvider =
                shadowAppWidgetManager.getAppWidgetProviderFor(widgetId);
        assertThat(appWidgetProvider, is(instanceOf(GenericCounterWidget.class)));
    }

    @Test
    public void testLaunchDialogWhenNoTypeSet() {
        createWidget();

        verify(mockModelModule.counterTypes).getTypeForWidget(widgetId);

        assertThat(shadowOf(context).getNextStartedActivity(), is(equalTo(null)));

        shadowAppWidgetManager.getViewFor(widgetId).performClick();

        nextStartedActivity = shadowOf(context).getNextStartedActivity();
        assertThat(nextStartedActivity.getComponent(),
                is(equalTo(new Intent(context, PickCounterTypeActivity.class).getComponent())));
        assertThat(nextStartedActivity.getIntExtra(PickCounterTypeActivity.WIDGET_ID_EXTRA, -1),
                is(equalTo(widgetId)));
    }

    @Test
    public void testSetViewFromTodaysCount() {
        when(mockModelModule.counterTypes.getTypeForWidget(anyInt()))
                .thenReturn(Optional.of(counterType));
        when(mockModelModule.mockCounterRecords.getTodaysCount(counterType))
                .thenReturn(counterRecord);

        createWidget();

        verify(mockModelModule.mockCounterRecords).getTodaysCount(counterType);

        View widgetView = shadowAppWidgetManager.getViewFor(widgetId);
        TextView countView = (TextView) widgetView.findViewById(R.id.count_text);
        assertThat(countView.getText().toString(), is(equalTo(String.valueOf(counterRecord.getCount()))));
        TextView countTitle = (TextView) widgetView.findViewById(R.id.count_description);
        assertThat(countTitle.getText().toString(), is(equalTo("desc")));
    }

    @Test
    public void testIncrementCountBroadcastWhenCounterClicked() {
        when(mockModelModule.counterTypes.getTypeForWidget(anyInt()))
                .thenReturn(Optional.of(counterType));
        when(mockModelModule.mockCounterRecords.getTodaysCount(counterType))
                .thenReturn(counterRecord);

        createWidget();

        shadowAppWidgetManager.getViewFor(widgetId).performClick();

        broadcastIntent = shadowOf(context).getBroadcastIntents().get(0);
        assertThat(GenericCounterWidget.class.getName(),
                is(equalTo(broadcastIntent.getComponent().getClassName())));
        assertThat("increment_count_action", is(equalTo(broadcastIntent.getAction())));
    }

    @Test
    public void testIncrementCountWhenIncrementBroadcastReceived() {
        when(mockModelModule.counterTypes.getTypeForWidget(anyInt()))
                .thenReturn(Optional.of(counterType));
        when(mockModelModule.mockCounterRecords.getTodaysCount(counterType))
                .thenReturn(counterRecord);

        // Create a widget for the AppWidgetManager to interact with. It's ok to do this
        // since we aren't testing that part of the code.
        createWidget();
        GenericCounterWidget genericCounterWidget = new GenericCounterWidget();

        Intent intent = new Intent(context, GenericCounterWidget.class);
        intent.setAction("increment_count_action");
        intent.putExtra("widget_id_extra", widgetId);

        genericCounterWidget.onReceive(context, intent);

        verify(mockModelModule.mockCounterRecords).incrementTodaysCount(counterType);
    }

}
