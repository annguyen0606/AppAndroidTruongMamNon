package com.annguyen.truongmamnon.Controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.annguyen.truongmamnon.Activity.ManHinhDangNhapActivity;
import com.annguyen.truongmamnon.Activity.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirebaseManage extends BroadcastReceiver {
    static public String BOARD_CAST_NAME = "FirebaseManage";
    static public String NOTIFY_ID = "BOARD_CAST_NAME";
    int idNotify = 2;
    int id = 0;
    private Context mcontext;
    private DatabaseReference databaseReference;
    private static ListenData listenData;
    SharedPref sharedPref;
    static Ringtone ringtone;

    void init(){
        sharedPref = new SharedPref(mcontext);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("DuLieuMamNonDemo").child("1A").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Intent i = new Intent(mcontext, ManHinhDangNhapActivity.class);
                String tenPhuHuynh = "";
                String maHS = "";
                String tenHocSinh = "";
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(mcontext,
                        0, i, PendingIntent.FLAG_UPDATE_CURRENT);

                Cursor dataNguoiThan = ManHinhDangNhapActivity.databaseSQLite.GetData("Select *from ThongTinNguoiThan nt, ThongTinHocSinh hs where TRIM(nt.MaHs) = TRIM(hs.MaHs) and TRIM(nt.Uid) = '" + snapshot.getKey().trim() +"'");
                while (dataNguoiThan.moveToNext()){
                    tenPhuHuynh = dataNguoiThan.getString(2);
                    maHS = dataNguoiThan.getString(11);
                    NotifyManage.callNotify(mcontext,pendingIntent,idNotify++,NOTIFY_ID,"Có tài khoản tương tác", tenPhuHuynh + " take " + maHS);

                    Cursor dataNguoiThan2 = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM UIDTag WHERE Trim(Uid) = '"+snapshot.getKey().trim()+"'");
                    if (dataNguoiThan2.getCount() == 0){
                        ManHinhDangNhapActivity.databaseSQLite.QuerryData("INSERT INTO UIDTag VALUES(null,'"+snapshot.getKey()+"',"+"'17:00:00',"+0+")");
                        if (listenData!=null){
                            listenData.onUIChange(true);
                        }
                    }else{ }
//                    if (ringtone == null){
//                        ringtone = RingtoneManager.getRingtone(mcontext,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
//                    }
//                    playSound();
                }



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        init();
        NotifyManage.creatChannel(context,NOTIFY_ID,"Co Data", NotificationManager.IMPORTANCE_DEFAULT);
        if (listenData!=null){
            listenData.onUIChange(true);
        }
    }
    static public void startMe(Context context){
        Intent intent = new Intent();
        intent.setAction(BOARD_CAST_NAME);
        context.sendBroadcast(intent);
    }
    public interface  ListenData{
        public void onUIChange(Boolean status);
    }

    public static void setListener(ListenData listener){
       FirebaseManage.listenData = listener;
   }

    void playSound(){
        if (ringtone != null){
            ringtone.play();
        }
    }
   public static void stopSound(){
        if (ringtone != null){
            ringtone.stop();
        }
   }
}
