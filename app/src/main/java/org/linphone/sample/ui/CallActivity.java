package org.linphone.sample.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.mediastream.Version;
import org.linphone.sample.views.CallIncomingAnswerButton;
import org.linphone.sample.views.CallIncomingButtonListener;
import org.linphone.sample.views.CallIncomingDeclineButton;
import org.linphone.sample.service.LinphoneService;
import org.linphone.sample.R;

public class CallActivity extends Activity {

    private CoreListenerStub mCoreListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.w("myLog", "CALL  ACTIVITY");

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.call);

        LinphoneService.getCore().setNativeVideoWindowId(findViewById(R.id.videoSurface));

        mCoreListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
                android.util.Log.w("myLog", "FINISH ");
                if (state == Call.State.End || state == Call.State.Released) {
                    finish();
                }
            }
        };

        ((CallIncomingAnswerButton)findViewById(R.id.answer_button)).setListener(
                new CallIncomingButtonListener() {
                    @Override
                    public void onAction() {
                        CallParams params = LinphoneService.getCore().createCallParams(LinphoneService.getCore().getCurrentCall());
                        params.enableVideo(true);
                        LinphoneService.getCore().getCurrentCall().acceptWithParams(params);
                    }
                });
        ((CallIncomingDeclineButton)findViewById(R.id.decline_button)).setListener(
                new CallIncomingButtonListener() {
                    @Override
                    public void onAction() {
                        terminateCall();
                    }
                });
        LinphoneService.getCore().addListener(mCoreListener);
    }

    private void terminateCall() {
        Core core = LinphoneService.getCore();
        if (core.getCallsNb() > 0) {
            Call call = core.getCurrentCall();
            if (call == null) {
                call = core.getCalls()[0];
            }
            call.terminate();
        }
    }

    @Override
    protected void onDestroy() {
        LinphoneService.getCore().removeListener(mCoreListener);
        terminateCall();
        super.onDestroy();
    }

    @TargetApi(24)
    @Override
    public void onUserLeaveHint() {
        boolean supportsPip = getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
        if (supportsPip && Version.sdkAboveOrEqual(24)) {
            enterPictureInPictureMode();
        }
    }
}
