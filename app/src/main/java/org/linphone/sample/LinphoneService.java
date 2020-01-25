package org.linphone.sample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.linphone.core.Call;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Factory;
import org.linphone.core.LogCollectionState;
import org.linphone.core.tools.Log;
import org.linphone.mediastream.Version;
import org.linphone.sample.compatibility.Compatibility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class LinphoneService extends Service {

    private static LinphoneService sInstance;

    private Handler mHandler;
    private Timer mTimer;

    private Core mCore;
    private CoreListenerStub mCoreListener;

    public static boolean isReady() {
        return sInstance != null;
    }

    public static Core getCore() {
        return sInstance.mCore;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        String basePath = getFilesDir().getAbsolutePath();
        Factory.instance().setLogCollectionPath(basePath);
        Factory.instance().enableLogCollection(LogCollectionState.Enabled);
        Factory.instance().setDebugMode(true, getString(R.string.app_name));

        mHandler = new Handler();
        mCoreListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
                Toast.makeText(LinphoneService.this, message, Toast.LENGTH_SHORT).show();
                android.util.Log.w("myLog", "STATE CHANGE " + state);
                if (state == Call.State.IncomingReceived) {
                    android.util.Log.w("myLog", "INCOME " + call);
                    displayCallNotification();
                    //  displayCallNotification(getApplicationContext());
                } else if (state == Call.State.Connected) {
                    // This stats means the call has been established, let's start the call activity
                    Intent intent = new Intent(LinphoneService.this, CallActivity.class);
                    // As it is the Service that is starting the activity, we have to give this flag
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();

                }
            }
        };

        try {
            copyIfNotExist(R.raw.linphonerc_default, basePath + "/.linphonerc");
            copyFromPackage(R.raw.linphonerc_factory, "linphonerc");
        } catch (IOException ioe) {
            Log.e(ioe);
        }

        mCore = Factory.instance().createCore(basePath + "/.linphonerc", basePath + "/linphonerc", this);
        mCore.addListener(mCoreListener);
        configureCore();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);


        String CHANNEL_ID = "Background Service";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = builder
                    .setColor(getResources().getColor(R.color.primary_color))
                    .setContentTitle("Активно приложение Видео дом")
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(CHANNEL_ID);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            notificationManager.createNotificationChannel(mChannel);
            startForeground(1231, notification);

        }

        if (sInstance != null) {
            return START_STICKY;
        }

        sInstance = this;

        mCore.start();
        TimerTask lTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                if (mCore != null) {
                                    mCore.iterate();
                                }
                            }
                        });
            }
        };
        mTimer = new Timer("Linphone scheduler");
        mTimer.schedule(lTask, 0, 20);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mCore.removeListener(mCoreListener);
        mTimer.cancel();
        mCore.stop();
        mCore = null;
        sInstance = null;
        super.onDestroy();
    }

    private void configureCore() {
        String basePath = getFilesDir().getAbsolutePath();
        String userCerts = basePath + "/user-certs";
        File f = new File(userCerts);
        if (!f.exists()) {
            if (!f.mkdir()) {
                Log.e(userCerts + " can't be created.");
            }
        }
        mCore.setUserCertificatesPath(userCerts);
    }


    private void copyIfNotExist(int ressourceId, String target) throws IOException {
        File lFileToCopy = new File(target);
        if (!lFileToCopy.exists()) {
            copyFromPackage(ressourceId, lFileToCopy.getName());
        }
    }

    private void copyFromPackage(int ressourceId, String target) throws IOException {
        FileOutputStream lOutputStream = openFileOutput(target, 0);
        InputStream lInputStream = getResources().openRawResource(ressourceId);
        int readByte;
        byte[] buff = new byte[8048];
        while ((readByte = lInputStream.read(buff)) != -1) {
            lOutputStream.write(buff, 0, readByte);
        }
        lOutputStream.flush();
        lOutputStream.close();
        lInputStream.close();
    }


    public void displayCallNotification() {

        Intent i = new Intent(App.getInstance(), CallActivity.class);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = Compatibility.createIncomingCallNotification(App.getInstance(), 12312, null, "32342", "123123", pendingIntent);

        String CHANNEL_ID_NOTIFICATION = "My Notifications2";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID_NOTIFICATION, CHANNEL_ID_NOTIFICATION, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(CHANNEL_ID_NOTIFICATION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            mChannel.enableVibration(true);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }
}
