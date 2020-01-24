package org.linphone.sample.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.linphone.sample.CallActivity;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intentNew = new Intent(context, CallActivity.class);
        intentNew.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Log.w("myLog", "RECEIVER ");
        context.startActivity(intentNew);
    }
}