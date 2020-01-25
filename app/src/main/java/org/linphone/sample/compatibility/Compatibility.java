/*
 * Copyright (c) 2010-2019 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.linphone.sample.compatibility;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import org.linphone.mediastream.Version;

public class Compatibility {
    public static final String CHAT_NOTIFICATIONS_GROUP = "CHAT_NOTIF_GROUP";
    public static final String KEY_TEXT_REPLY = "key_text_reply";
    public static final String INTENT_NOTIF_ID = "NOTIFICATION_ID";
    public static final String INTENT_REPLY_NOTIF_ACTION = "org.linphone.REPLY_ACTION";
    public static final String INTENT_HANGUP_CALL_NOTIF_ACTION = "org.linphone.HANGUP_CALL_ACTION";
    public static final String INTENT_ANSWER_CALL_NOTIF_ACTION = "org.linphone.ANSWER_CALL_ACTION";
    public static final String INTENT_LOCAL_IDENTITY = "LOCAL_IDENTITY";
    public static final String INTENT_MARK_AS_READ_ACTION = "org.linphone.MARK_AS_READ_ACTION";


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
