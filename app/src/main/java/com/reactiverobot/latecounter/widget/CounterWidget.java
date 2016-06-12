package com.reactiverobot.latecounter.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.prefs.LateCounterPrefs;

import java.util.Calendar;

public class CounterWidget extends AdvancedRoboAppWidgetProvider {

    public static final String INCREMENT_COUNT = "increment_late_count";
    public static final String UPDATE_COUNTER = "update_counter";
    public static final String WIDGET_ID = "widget_id";

    @Inject
    LateCounterPrefs prefs;

    public void onHandleUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Log.d("LateCounter-Widget", "Called onUpdate for " + N + " widgets");
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int widgetIndex = 0; widgetIndex < N; widgetIndex++) {
            int widgetId = appWidgetIds[widgetIndex];
            Log.d("LateCounter-Widget", "Updating widget index: " + widgetIndex + " with id: " +
                    widgetId);

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, CounterWidget.class);
            intent.setAction(INCREMENT_COUNT);
            intent.putExtra(WIDGET_ID, widgetId);
            PendingIntent incrementIntent = PendingIntent.getBroadcast(context, widgetId, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            int todaysLateCount = prefs.getTodaysLateCount();

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widget);
            views.setOnClickPendingIntent(R.id.whole_widget, incrementIntent);

            views.setTextViewText(R.id.count_text, String.valueOf(todaysLateCount));
            Log.d("LateCounter-Widget", "setting count to " + todaysLateCount);


            // Tell the AppWidgetManager to perform an update on the current app widget
            // (mdailey) Note that we must pass the actually widget component name rather than id.
            ComponentName thisWidget = new ComponentName(context, CounterWidget.class);
            appWidgetManager.updateAppWidget(thisWidget, views);
        }
    }

    public void onHandleReceived(Context context, Intent intent) {
        Log.d("LateCounter-Widget", "Received intent: " + intent.getAction());

        if (intent != null && intent.getAction() != null && intent.hasExtra(WIDGET_ID)) {
            handleActionForWidget(context, intent.getAction(), intent.getIntExtra(WIDGET_ID, -1));
        }
    }

    private void handleActionForWidget(Context context, String action, int widgetId) {
        if (action.equals(INCREMENT_COUNT)) {
            Log.d("LateCounter-Widget", "Received increment broadcast.");
            prefs.incrementLateCount();
            Log.d("LateCounter-Widget", "Count : " + prefs.getTodaysLateCount());

            updateWidgetFromIntentFlags(context, widgetId);
            scheduleUpdateAtMidnight(context, widgetId);
        } else if (action.equals(UPDATE_COUNTER)) {
            updateWidgetFromIntentFlags(context, widgetId);
        }
    }

    private void updateWidgetFromIntentFlags(Context context, int appWidgetId) {
        int[] ids = {appWidgetId};
        onUpdate(context, AppWidgetManager.getInstance(context), ids);
    }

    private void scheduleUpdateAtMidnight(Context context, int widgetId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        Intent updateAtMidnightIntent = new Intent(context, CounterWidget.class);
        updateAtMidnightIntent.setAction(UPDATE_COUNTER);
        updateAtMidnightIntent.putExtra(WIDGET_ID, widgetId);

        PendingIntent broadcastIntent = PendingIntent.getBroadcast(context, 32342,
                updateAtMidnightIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcastIntent);
    }
}
