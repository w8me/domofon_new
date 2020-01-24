package org.linphone.sample.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AutoStartUp extends BroadcastReceiver {

    private static final String SETTINGS = "settings";
    public static final String LIST_ALARMS = "alarm_list";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w("myLog", "START UP ");
    }
}
