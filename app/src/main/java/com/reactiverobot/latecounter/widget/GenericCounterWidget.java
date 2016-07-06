package com.reactiverobot.latecounter.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.activity.PickCounterTypeActivity;
import com.reactiverobot.latecounter.analytics.CounterzAnalytics;
import com.reactiverobot.latecounter.model.CounterRecord;
import com.reactiverobot.latecounter.model.CounterRecords;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;

import org.roboguice.shaded.goole.common.base.Optional;

import java.util.Calendar;


public class GenericCounterWidget extends AdvancedRoboAppWidgetProvider {

    private static final String INCREMENT_COUNT_ACTION = "increment_count_action";
    private static final String WIDGET_ID_EXTRA = "widget_id_extra";
    public static final String TAG = "GenericCounterWidget";

    @Inject CounterTypes counterTypes;
    @Inject CounterRecords counterRecords;
    @Inject CounterzAnalytics analytics;

    public void onHandleUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetIndex = 0; widgetIndex < appWidgetIds.length; widgetIndex++) {
            int widgetId = appWidgetIds[widgetIndex];

            Optional<CounterType> typeForWidget = counterTypes.getTypeForWidget(widgetId);

            if (typeForWidget.isPresent()) {
                CounterRecord todaysCount = counterRecords.getTodaysCount(typeForWidget.get());

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widget);

                int widgetColorId = typeForWidget.get().getColorId();
                int widgetColor = context.getResources().getColor(widgetColorId);

                views.setTextViewText(R.id.count_description, typeForWidget.get().getDescription());
                views.setTextColor(R.id.count_description, widgetColor);

                views.setViewVisibility(R.id.count_text, View.VISIBLE);
                views.setTextViewText(R.id.count_text, String.valueOf(todaysCount.getCount()));
                views.setTextColor(R.id.count_text, widgetColor);

                views.setInt(R.id.widget_view_group,
                        "setBackgroundResource",
                        getWidgetRippleBackgroundId(widgetColorId));

                Intent intent = new Intent(context, GenericCounterWidget.class);
                intent.setAction(INCREMENT_COUNT_ACTION);
                intent.putExtra(WIDGET_ID_EXTRA, widgetId);
                PendingIntent incrementIntent = PendingIntent.getBroadcast(context, widgetId, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                views.setOnClickPendingIntent(R.id.whole_widget, incrementIntent);

                scheduleUpdateAtMidnight(context, widgetId);

                appWidgetManager.updateAppWidget(widgetId, views);
            } else {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widget);

                int blackColor = context.getResources().getColor(android.R.color.black);

                views.setTextViewText(R.id.count_description, "Click to choose counter type.");
                views.setTextColor(R.id.count_description, blackColor);

                views.setViewVisibility(R.id.count_text, View.GONE);
                views.setTextViewText(R.id.count_text, "");
                views.setTextColor(R.id.count_text, blackColor);

                PendingIntent pendingIntent = PendingIntent.getActivity(
                        context,
                        widgetId,
                        PickCounterTypeActivity.getStartIntent(context, widgetId),
                        PendingIntent.FLAG_UPDATE_CURRENT);

                views.setOnClickPendingIntent(R.id.whole_widget, pendingIntent);

                appWidgetManager.updateAppWidget(widgetId, views);
            }
        }

        analytics.synchronizeUserProperties();
    }

    private int getWidgetRippleBackgroundId(int widgetColorId) {
        switch (widgetColorId) {
            case R.color.red:
                return R.drawable.widget_ripple_red;
            case R.color.yellow:
                return R.drawable.widget_ripple_yellow;
            case R.color.green:
                return R.drawable.widget_ripple_green;
            case R.color.blue:
                return R.drawable.widget_ripple_blue;
            case android.R.color.black:
                return R.drawable.widget_ripple_black;
            default:
                return R.drawable.widget_ripple_white;
        }
    }

    @Override
    public void onHandleReceived(Context context, Intent intent) {
        Log.d(TAG, "Received intent: " + intent.getAction());

        if (INCREMENT_COUNT_ACTION.equals(intent.getAction())) {
            analytics.reportCounterClicked();

            int appWidgetId = intent.getIntExtra(WIDGET_ID_EXTRA, -1);
            if (appWidgetId >= 0) {
                Optional<CounterType> typeForWidget = counterTypes.getTypeForWidget(appWidgetId);
                if (typeForWidget.isPresent()) {
                    counterRecords.incrementTodaysCount(typeForWidget.get());

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
                updateAtMidnightIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcastIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcastIntent);
        }
    }
}
