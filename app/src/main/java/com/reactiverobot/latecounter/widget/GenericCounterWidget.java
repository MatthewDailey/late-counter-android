package com.reactiverobot.latecounter.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterRecord;
import com.reactiverobot.latecounter.model.CounterRecords;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;


public class GenericCounterWidget extends AdvancedRoboAppWidgetProvider {

    @Inject CounterTypes counterTypes;
    @Inject CounterRecords counterRecords;

    public void onHandleUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetIndex = 0; widgetIndex < appWidgetIds.length; widgetIndex++) {
            int widgetId = appWidgetIds[widgetIndex];

            Optional<CounterType> typeForWidget = counterTypes.getTypeForWidget(widgetId);

            if (typeForWidget.isPresent()) {
                CounterRecord todaysCount = counterRecords.getTodaysCount(typeForWidget.get());

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widget);
                views.setTextViewText(R.id.count_description, typeForWidget.get().getDescription());
                views.setTextViewText(R.id.count_text, String.valueOf(todaysCount.getCount()));

                appWidgetManager.updateAppWidget(widgetId, views);
            } else {
                // TODO: Launch type selector.
            }

            Log.d("GenericCounterWidget", typeForWidget.toString());
        }
    }
}
