package com.reactiverobot.latecounter.notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.reactiverobot.latecounter.notifications.NotificationUtils.sendNotification;

public class FirebaseNotificationReceiver extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        sendNotification(
                this,
                getNotificationTitle(remoteMessage.getNotification()),
                remoteMessage.getNotification().getBody());
    }

    private String getNotificationTitle(RemoteMessage.Notification notification) {
        if (notification.getTitle() != null) {
            return notification.getTitle();
        } else {
            return "Counterz";
        }
    }


}
