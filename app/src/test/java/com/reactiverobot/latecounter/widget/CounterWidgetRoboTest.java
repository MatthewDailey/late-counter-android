package com.reactiverobot.latecounter.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;

import com.google.common.base.Optional;
import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.MockModelModule;
import com.reactiverobot.latecounter.prefs.PrefsModule;

import org.junit.Rule;
import org.junit.Test;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowAppWidgetManager;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CounterWidgetRoboTest extends AbstractRoboTest {

    private ShadowAppWidgetManager shadowAppWidgetManager;
    private int widgetId;

    @Rule
    public MockModelModule mockModelModule = new MockModelModule();

    @Override
    protected void setup() {
        when(mockModelModule.counterTypes.getTypeForWidget(anyInt()))
                .thenReturn(Optional.<CounterType>absent());

        createWidget();
    }

    private void createWidget() {
        shadowAppWidgetManager = Shadows
                .shadowOf(AppWidgetManager.getInstance(context));
        widgetId = shadowAppWidgetManager
                .createWidget(GenericCounterWidget.class, R.layout.counter_widget);
    }

    @Override
    protected Module[] getModules() {
        return new Module[]{mockModelModule, new PrefsModule()};
    }

    @Test
    public void testInstantiateWidget() {
        AppWidgetProvider appWidgetProvider =
                shadowAppWidgetManager.getAppWidgetProviderFor(widgetId);
        assertThat(appWidgetProvider, is(instanceOf(GenericCounterWidget.class)));
    }

    @Test
    public void testLoadWidgetCounterTypeOnUpdate() {
        // Update is called by default when creating the widget.

        verify(mockModelModule.counterTypes).getTypeForWidget(widgetId);
    }

}
