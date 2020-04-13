package com.annguyen.truongmamnon.Controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.annguyen.truongmamnon.R;

public class NotifyManage {
    public static void creatChannel(Context context, String chID, String chName, int impt){
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = new NotificationChannel(chID,chName,impt);
            nc.enableLights(true);
            nc.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            nc.setLightColor(Color.GREEN);
            nm.createNotificationChannel(nc);
        }
    }
    public static void callNotify(Context context, PendingIntent pendingIntent, int id, String chId, String title, String text){
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder nb = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setColor(Color.BLUE)
                .setSmallIcon(R.drawable.ic_conek);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            nb.setChannelId(chId);
        }
        nm.notify(id,nb.build());
    }

    public static Notification callNotify4MainService(Context context, PendingIntent pendingIntent,String chId, String title, String text){
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
        //nb.setPriority(Notification.PRIORITY_MIN);
               // .setPriority(Notification.PRIORITY_MIN)
                .setContentTitle(title)
                .setContentText(text)
                .setShowWhen(true)
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_conek);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            nb.setChannelId(chId);
        }
        return nb.build();
    }
}
