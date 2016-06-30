package com.reactiverobot.latecounter.widget;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.google.inject.Injector;

import roboguice.RoboGuice;
import roboguice.receiver.RoboAppWidgetProvider;

/**
 * Expanded Roboguice {@link android.appwidget.AppWidgetProvider} to include more methods than just
 * the <em>onUpdate</em>.
 */
abstract class AdvancedRoboAppWidgetProvider extends RoboAppWidgetProvider {

    private void doInjection(Context context) {
        final Injector injector = RoboGuice.getOrCreateBaseApplicationInjector(
                (Application) context.getApplicationContext());

        injector.injectMembers(this);
    }

    @Override
    public final void onReceive(Context context, Intent intent) {
        doInjection(context);

        super.onReceive(context, intent);

        onHandleReceived(context, intent);
    }

    public void onHandleReceived(Context context, Intent intent) {
        // To be implemented by children.
    }

}