package org.linphone.sample.compatibility;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;

import androidx.core.content.ContextCompat;

import org.linphone.sample.R;


@TargetApi(21)
class ApiTwentyOnePlus {

    public static Notification createIncomingCallNotification(
            Context context,
            String contactName,
            String sipUri,
            PendingIntent intent) {

        return new Notification.Builder(context)
                .setContentTitle(contactName)
                .setContentText(context.getString(R.string.incall_notif_incoming))
                .setSmallIcon(R.drawable.topbar_call_notification)
                .setAutoCancel(false)
                .setContentIntent(intent)
                .setCategory(Notification.CATEGORY_CALL)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH)
                .setOngoing(true)
                .setLights(
                        ContextCompat.getColor(context, R.color.notification_led_color),
                        context.getResources().getInteger(R.integer.notification_ms_on),
                        context.getResources().getInteger(R.integer.notification_ms_off))
                .setShowWhen(true)
                .setFullScreenIntent(intent, true)
                .build();
    }
}
