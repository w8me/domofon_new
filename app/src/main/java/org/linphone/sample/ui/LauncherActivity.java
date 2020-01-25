package org.linphone.sample.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.linphone.sample.service.LinphoneService;
import org.linphone.sample.R;

public class LauncherActivity extends Activity {
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.launcher);

        mHandler = new Handler();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (LinphoneService.isReady()) {
            onServiceReady();
        } else {
            startService(new Intent().setClass(this, LinphoneService.class));
            new ServiceWaitThread().start();
        }
    }

    private void onServiceReady() {
        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
    }

    private class ServiceWaitThread extends Thread {
        public void run() {
            while (!LinphoneService.isReady()) {
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException("waiting thread sleep() has been interrupted");
                }
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onServiceReady();
                }
            });
        }
    }
}
