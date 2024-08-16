package com.guarino.milectura;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.guarino.milectura.data.db.MiLecturaDatabase;
import com.facebook.drawee.backends.pipeline.Fresco;

public class MiLecturaApplication extends Application {

    public static final String CHANNEL_ID = "0";
    private static final String APP_ID = "ca-app-pub-3238435315161366~5348645978";
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        createNotificationChannel();
        MiLecturaDatabase.create(this);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
