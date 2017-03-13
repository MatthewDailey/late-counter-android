package com.reactiverobot.latecounter.notifications;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import roboguice.receiver.RoboBroadcastReceiver;

import static com.reactiverobot.latecounter.notifications.NotificationUtils.sendNotification;

public class ReminderReceiver extends RoboBroadcastReceiver {
    protected void handleReceive(Context context, Intent intent) {
        Log.d("ReminderReceiver", "Received broadcast to send notification.");

        sendNotification(context, "Counterz", "Time to check your habit counters.");
    }
}
