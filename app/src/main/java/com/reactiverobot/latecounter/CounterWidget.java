package com.reactiverobot.latecounter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class CounterWidget extends AppWidgetProvider {

    public static final String INCREMENT_COUNT = "increment_late_count";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Log.d("LateCounter-Widget", "Called onUpdate for " + N + " widgets");
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            Log.d("LateCounter-Widget", "Updating " + i);
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, CounterWidget.class);
            intent.setAction(INCREMENT_COUNT);
            intent.addFlags(appWidgetId);
            PendingIntent incrementIntent = PendingIntent.getBroadcast(context, appWidgetId, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            int todaysLateCount = new LateCounterPrefs(context).getTodaysLateCount();

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widget);
            views.setOnClickPendingIntent(R.id.whole_widget, incrementIntent);

            views.setTextViewText(R.id.count_text, String.valueOf(todaysLateCount));
            Log.d("LateCounter-Widget", "setting count to " + todaysLateCount);


            // Tell the AppWidgetManager to perform an update on the current app widget
            // (mdailey) Note that we must pass the actually widget component name rather than id.
            ComponentName thisWidget = new ComponentName(context, CounterWidget.class );
            appWidgetManager.updateAppWidget(thisWidget, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent != null && intent.getAction() != null && intent.getAction().equals(INCREMENT_COUNT)) {
            Log.d("LateCounter-Widget", "Received increment broadcast.");
            new LateCounterPrefs(context).incrementLateCount();

            Log.d("LateCounter-Widget", "Count : " + new LateCounterPrefs(context).getTodaysLateCount());

            int appWidgetId = intent.getFlags();
            int[] ids = {appWidgetId};

            onUpdate(context, AppWidgetManager.getInstance(context), ids);
        }
    }
}
