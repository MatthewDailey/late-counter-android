package com.reactiverobot.latecounter.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;

import com.google.inject.Module;
import com.reactiverobot.latecounter.AbstractRoboTest;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.ModelModule;
import com.reactiverobot.latecounter.prefs.PrefsModule;

import org.junit.Test;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowAppWidgetManager;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CounterWidgetRoboTest extends AbstractRoboTest {
    @Override
    protected void setup() {

    }

    @Override
    protected Module[] getModules() {
        return new Module[]{new ModelModule(), new PrefsModule()};
    }

    @Test
    public void testInstantiateWidget() {
        ShadowAppWidgetManager shadowAppWidgetManager = Shadows
                .shadowOf(AppWidgetManager.getInstance(context));
        int widgetId = shadowAppWidgetManager
                .createWidget(GenericCounterWidget.class, R.layout.counter_widget);

        AppWidgetProvider appWidgetProvider =
                shadowAppWidgetManager.getAppWidgetProviderFor(widgetId);
        assertThat(appWidgetProvider, is(instanceOf(GenericCounterWidget.class)));
    }

}
