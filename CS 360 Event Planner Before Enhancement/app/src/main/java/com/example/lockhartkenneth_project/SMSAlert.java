package com.example.lockhartkenneth_project;

/**
 * @author Kenneth Lockhart
 * Date: 12/14/2024
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import kotlinx.coroutines.channels.BroadcastChannel;

/**
 * SMS Alert for notification when enabled
 */
public class SMSAlert extends BroadcastReceiver {
        /**
         * Runs when the broadcast is called
         * @param context The Context in which the receiver is running.
         * @param intent The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get data
            Bundle bundle = intent.getExtras();
            String eventName = bundle.getString("event");
            String eventDesc = bundle.getString("description");

            // Build Notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyText")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(eventName)
                    .setContentText(eventDesc)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            // Get Notification Manager
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            // Check POST permission
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            // Send notification
            notificationManager.notify(200, builder.build());

        }

}
