package com.annguyen.truongmamnon.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.annguyen.truongmamnon.Activity.MainActivity;
import com.annguyen.truongmamnon.Controller.NotifyManage;
import com.annguyen.truongmamnon.Controller.FirebaseManage;

public class FirebaseService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(new FirebaseManage(),new IntentFilter(FirebaseManage.BOARD_CAST_NAME));
        FirebaseManage.startMe(this);
    }

    private void initFore() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotifyManage.creatChannel(this,"MainService","main_service", NotificationManager.IMPORTANCE_HIGH);
        startForeground(1,NotifyManage.callNotify4MainService(this,pendingIntent,"MainService","Trường mầm non","Đang chạy"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initFore();
        return START_NOT_STICKY;
    }
}
