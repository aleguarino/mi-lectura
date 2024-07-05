/*****************************************************
 * Autor: Alejandro Guarino Muñoz                    *
 *                                                   *
 * Descripcion: broadcast receiver encargado de      *
 * lanzar las notificaciones de lectura              *
 *****************************************************/
package com.example.milectura.data.broadcast;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.milectura.MiLecturaApplication;
import com.example.milectura.R;
import com.example.milectura.data.model.Book;
import com.example.milectura.ui.MainActivity;

public class ReminderBroadcast extends BroadcastReceiver {
    private int id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = "";
        String text = "";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mainActivity = new Intent(context, MainActivity.class);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (intent.getExtras() != null) {
            title = intent.getStringExtra(Book.TAG);
            text = context.getString(R.string.read_notification_message);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivity, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "GOAL_NOTIFICATION";
            String descriptionText = "notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(MiLecturaApplication.CHANNEL_ID, name, importance);
            channel.setDescription(descriptionText);

            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MiLecturaApplication.CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_transparent)
                .setContentTitle(text)
                .setContentText(title)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(id++, builder.build());
    }
}
