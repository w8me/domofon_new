package org.linphone.sample.ui;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.sample.service.LinphoneService;
import org.linphone.sample.R;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ImageView mLed;
    private CoreListenerStub mCoreListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mLed = findViewById(R.id.led);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();

        mCoreListener = new CoreListenerStub() {
            @Override
            public void onRegistrationStateChanged(Core core, ProxyConfig cfg, RegistrationState state, String message) {
                android.util.Log.w("myLog", "REG STATE " + state);
                updateLed(state);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAndRequestCallPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinphoneService.getCore().addListener(mCoreListener);
        ProxyConfig proxyConfig = LinphoneService.getCore().getDefaultProxyConfig();
        if (proxyConfig != null) {
            updateLed(proxyConfig.getState());
        } else {
            startActivity(new Intent(this, ConfigureAccountActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LinphoneService.getCore().removeListener(mCoreListener);
    }

    private void updateLed(RegistrationState state) {
        switch (state) {
            case Ok:
                mLed.setImageResource(R.drawable.led_connected);
                break;
            case None:
            case Cleared:
                mLed.setImageResource(R.drawable.led_disconnected);
                break;
            case Failed:
                mLed.setImageResource(R.drawable.led_error);
                break;
            case Progress:
                mLed.setImageResource(R.drawable.led_inprogress);
                break;
        }
    }

    private void checkAndRequestCallPermissions() {
        ArrayList<String> permissionsList = new ArrayList<>();

        int recordAudio = getPackageManager().checkPermission(Manifest.permission.RECORD_AUDIO, getPackageName());
        int camera = getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName());

        if (recordAudio != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.RECORD_AUDIO);
        }

        if (camera != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.CAMERA);
        }

        if (permissionsList.size() > 0) {
            String[] permissions = new String[permissionsList.size()];
            permissions = permissionsList.toArray(permissions);
            ActivityCompat.requestPermissions(this, permissions, 0);
        }
    }
}
