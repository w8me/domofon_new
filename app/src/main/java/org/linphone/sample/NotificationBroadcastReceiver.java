package org.linphone.sample;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.linphone.sample.compatibility.Compatibility;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.w("myLog", " ACTION 0");

        if (intent.getAction().equals(Compatibility.INTENT_ANSWER_CALL_NOTIF_ACTION) || intent.getAction().equals(Compatibility.INTENT_HANGUP_CALL_NOTIF_ACTION)) {
            Log.w("myLog", " ACTION 1");

        }
    }

}
