package org.linphone.sample.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.sample.App;
import org.linphone.sample.ui.CallActivity;
import org.linphone.sample.service.LinphoneService;
import org.linphone.sample.compatibility.Compatibility;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equals(Compatibility.INTENT_HANGUP_CALL_NOTIF_ACTION)) {
            Core core = LinphoneService.getCore();
            if (core.getCallsNb() > 0) {
                Call call = core.getCurrentCall();
                if (call == null) {
                    call = core.getCalls()[0];
                }
                call.terminate();
            }
        } else if(intent.getAction().equals(Compatibility.INTENT_ANSWER_CALL_NOTIF_ACTION)){
            CallParams params = LinphoneService.getCore().createCallParams(LinphoneService.getCore().getCurrentCall());
            params.enableVideo(true);
            LinphoneService.getCore().getCurrentCall().acceptWithParams(params);
            Intent i = new Intent(App.getInstance(), CallActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }
    }

}
