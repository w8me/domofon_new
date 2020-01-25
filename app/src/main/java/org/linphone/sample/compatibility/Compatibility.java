package org.linphone.sample.compatibility;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import org.linphone.mediastream.Version;

public class Compatibility {
    public static final String INTENT_NOTIF_ID = "NOTIFICATION_ID";
    public static final String INTENT_HANGUP_CALL_NOTIF_ACTION = "org.linphone.HANGUP_CALL_ACTION";
    public static final String INTENT_ANSWER_CALL_NOTIF_ACTION = "org.linphone.ANSWER_CALL_ACTION";


    public static Notification createIncomingCallNotification(
            Context context,
            int callId,
            String contactName,
            String sipUri,
            PendingIntent intent) {
        if (Version.sdkAboveOrEqual(Version.API26_O_80)) {
            Log.w("myLog", "createIncomingCallNotification ");
            return ApiTwentySixPlus.createIncomingCallNotification(
                    context, callId,contactName, sipUri, intent);
        } else if (Version.sdkAboveOrEqual(Version.API24_NOUGAT_70)) {
            return ApiTwentyFourPlus.createIncomingCallNotification(
                    context, callId,contactName, sipUri, intent);
        }
        return ApiTwentyOnePlus.createIncomingCallNotification(
                context, contactName, sipUri, intent);
    }


    public static Notification.Action getCallAnswerAction(Context context, int callId) {
        if (Version.sdkAboveOrEqual(29)) {
            return ApiTwentyNinePlus.getCallAnswerAction(context, callId);
        } else if (Version.sdkAboveOrEqual(Version.API24_NOUGAT_70)) {
            return ApiTwentyFourPlus.getCallAnswerAction(context, callId);
        }
        return null;
    }

    public static Notification.Action getCallDeclineAction(Context context, int callId) {
        if (Version.sdkAboveOrEqual(29)) {
            return ApiTwentyNinePlus.getCallDeclineAction(context, callId);
        } else if (Version.sdkAboveOrEqual(Version.API24_NOUGAT_70)) {
            return ApiTwentyFourPlus.getCallDeclineAction(context, callId);
        }
        return null;
    }
}
