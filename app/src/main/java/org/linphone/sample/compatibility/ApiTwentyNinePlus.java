package org.linphone.sample.compatibility;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.linphone.sample.receivers.NotificationBroadcastReceiver;
import org.linphone.sample.R;

import static org.linphone.sample.compatibility.Compatibility.*;


@TargetApi(29)
public class ApiTwentyNinePlus {


    public static Notification.Action getCallAnswerAction(Context context, int callId) {
        Intent answerIntent = new Intent(context, NotificationBroadcastReceiver.class);
        answerIntent.setAction(INTENT_ANSWER_CALL_NOTIF_ACTION);
        answerIntent.putExtra(INTENT_NOTIF_ID, callId);

        PendingIntent answerPendingIntent =
                PendingIntent.getBroadcast(
                        context, callId, answerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.w("myLog", "getCallAnswerAction ");

        return new Notification.Action.Builder(
                        R.drawable.call_audio_start,
                        context.getString(R.string.notification_call_answer_label),
                        answerPendingIntent)
                .build();
    }

    public static Notification.Action getCallDeclineAction(Context context, int callId) {
        Intent hangupIntent = new Intent(context, NotificationBroadcastReceiver.class);
        hangupIntent.setAction(INTENT_HANGUP_CALL_NOTIF_ACTION);
        hangupIntent.putExtra(INTENT_NOTIF_ID, callId);

        PendingIntent hangupPendingIntent =
                PendingIntent.getBroadcast(
                        context, callId, hangupIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.w("myLog", "getCallDeclineAction ");
        return new Notification.Action.Builder(
                        R.drawable.call_hangup,
                        context.getString(R.string.notification_call_hangup_label),
                        hangupPendingIntent)
                .build();
    }
}
