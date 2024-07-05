/*****************************************************
 * Autor: Alejandro Guarino Muñoz                    *
 *                                                   *
 * Descripcion: broadcast receiver encargado de      *
 * reiniciar las alarmas al reiniciar el dispositivo *
 *****************************************************/
package com.example.milectura.data.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /* Setting the alarm here */
            Intent alarmIntent = new Intent(context, ReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            int interval = 8000;
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

            //Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
        }
    }
}
