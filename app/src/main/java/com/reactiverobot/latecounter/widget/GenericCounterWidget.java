package com.reactiverobot.latecounter.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.util.Log;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;


public class GenericCounterWidget extends AdvancedRoboAppWidgetProvider {

    @Inject
    CounterTypes counterTypes;

    public void onHandleUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetIndex = 0; widgetIndex < appWidgetIds.length; widgetIndex++) {
            int appWidgetId = appWidgetIds[widgetIndex];

            Optional<CounterType> typeForWidget = counterTypes.getTypeForWidget(appWidgetId);

            Log.d("GenericCounterWidget", typeForWidget.toString());
        }
    }
}
