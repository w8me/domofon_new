package org.linphone.sample;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        String CHANNEL_ID = "Background Service";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = builder
                    .setColor(getResources().getColor(R.color.primary_color))
                    .setContentTitle("Активно приложение Binman")
                    .build();
        }


        /*String CHANNEL_ID_NOTIFICATION = "Notification";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_NOTIFICATION)
                    .setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = builder
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle("Активно приложение Binman")
                    .build();

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID_NOTIFICATION, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(CHANNEL_ID_NOTIFICATION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            notificationManager.createNotificationChannel(mChannel);
            startForeground(1231, notification);

        }*/

    }

    public static App getInstance() {
        return instance;
    }
}
