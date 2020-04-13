package com.annguyen.truongmamnon.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class InternetConnectionBroadCast extends BroadcastReceiver {
    public static boolean kiemTraKetNoiInternet = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED){
                kiemTraKetNoiInternet = true;
            }else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED){
                kiemTraKetNoiInternet = false;
                Toast.makeText(context,"Lỗi Intertnet, Xin kiểm tra lại!",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
