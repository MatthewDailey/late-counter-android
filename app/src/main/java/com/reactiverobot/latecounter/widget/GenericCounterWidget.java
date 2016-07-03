package com.reactiverobot.latecounter.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.activity.PickCounterTypeActivity;
import com.reactiverobot.latecounter.model.CounterRecord;
import com.reactiverobot.latecounter.model.CounterRecords;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;

import org.roboguice.shaded.goole.common.base.Optional;

import java.util.Calendar;


public class GenericCounterWidget extends AdvancedRoboAppWidgetProvider {

    private static final String INCREMENT_COUNT_ACTION = "increment_count_action";
    private static final String WIDGET_ID_EXTRA = "widget_id_extra";

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
                views.setViewVisibility(R.id.count_text, View.VISIBLE);
                views.setTextViewText(R.id.count_text, String.valueOf(todaysCount.getCount()));

                Intent intent = new Intent(context, GenericCounterWidget.class);
                intent.setAction(INCREMENT_COUNT_ACTION);
                intent.putExtra(WIDGET_ID_EXTRA, widgetId);
                PendingIntent incrementIntent = PendingIntent.getBroadcast(context, widgetId, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                views.setOnClickPendingIntent(R.id.whole_widget, incrementIntent);

                appWidgetManager.updateAppWidget(widgetId, views);
            } else {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widget);

                views.setTextViewText(R.id.count_description, "Click to choose counter type.");
                views.setViewVisibility(R.id.count_text, View.GONE);
                views.setTextViewText(R.id.count_text, "");

                PendingIntent pendingIntent = PendingIntent.getActivity(
                        context,
                        widgetId,
                        PickCounterTypeActivity.getStartIntent(context, widgetId),
                        PendingIntent.FLAG_UPDATE_CURRENT);

                views.setOnClickPendingIntent(R.id.whole_widget, pendingIntent);

                appWidgetManager.updateAppWidget(widgetId, views);
            }
        }
    }

    @Override
    public void onHandleReceived(Context context, Intent intent) {
        Log.d("GenericCounterWidget", "Received intent: " + intent.getAction());

        if (INCREMENT_COUNT_ACTION.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra(WIDGET_ID_EXTRA, -1);
            if (appWidgetId >= 0) {
                Optional<CounterType> typeForWidget = counterTypes.getTypeForWidget(appWidgetId);
                if (typeForWidget.isPresent()) {
                    counterRecords.incrementTodaysCount(typeForWidget.get());

                    scheduleUpdateAtMidnight(context, appWidgetId);

                    onHandleUpdate(context, AppWidgetManager.getInstance(context), new int[]{appWidgetId});
                } else {
                    Log.e("GenericCounterWidget", "Attempted to increment widget without type.");
                }
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            counterTypes.removeWidgetId(appWidgetId);
        }
    }

    private void scheduleUpdateAtMidnight(Context context, int widgetId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        Intent updateAtMidnightIntent = new Intent(context, GenericCounterWidget.class);
        updateAtMidnightIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateAtMidnightIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{widgetId});

        PendingIntent broadcastIntent = PendingIntent.getBroadcast(context, widgetId + 1,
                updateAtMidnightIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcastIntent);
    }
}
