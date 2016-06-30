package com.reactiverobot.latecounter.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.activity.PickCounterTypeActivity;
import com.reactiverobot.latecounter.model.CounterRecord;
import com.reactiverobot.latecounter.model.CounterRecords;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;

import org.roboguice.shaded.goole.common.base.Optional;


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

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 21312,
                        new Intent(context, PickCounterTypeActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
                views.setOnClickPendingIntent(R.id.whole_widget, pendingIntent);

                appWidgetManager.updateAppWidget(widgetId, views);
            } else {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widget);

                context.startActivity(new Intent(context, PickCounterTypeActivity.class));

                appWidgetManager.updateAppWidget(widgetId, views);
            }
        }
    }

    @Override
    public void onHandleReceived(Context context, Intent intent) {
        Log.d("GenericCoutnerWidget", "Received intent: " + intent.getAction());
    }
}
