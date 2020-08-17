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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.annguyen.truongmamnon.Activity.ManHinhDangNhapActivity;
import com.annguyen.truongmamnon.Activity.SupportPaymentActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    String textLop = "";

    void init(){
        sharedPref = new SharedPref(mcontext);
        textLop = SharedPref.get(ManHinhDangNhapActivity.CURRENT_Class,String.class);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String ngayKiemTra = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String ngayKiemTra2 = new SimpleDateFormat("yyyy-MM").format(new Date());
        databaseReference.child("MamNon").child("DuLieuNopTien").child(textLop).child(ngayKiemTra2).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Intent i = new Intent(mcontext, SupportPaymentActivity.class);
                String tenPhuHuynh = "";
                String tenHocSinh = "";
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(mcontext,
                        0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                Cursor dataNguoiThan2 = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM KiemTraDongTien WHERE Trim(Uid) = '"+snapshot.getKey().trim()+"'");
                if (dataNguoiThan2.getCount() == 0){
                    Cursor dataNguoiThan = ManHinhDangNhapActivity.databaseSQLite.GetData("Select *from ThongTinNguoiThan nt, ThongTinHocSinh hs where TRIM(nt.MaHs) = TRIM(hs.MaHs) and TRIM(nt.Uid) = '" + snapshot.getKey().trim() +"'");
                    while (dataNguoiThan.moveToNext()){
                        tenPhuHuynh = dataNguoiThan.getString(2);
                        tenHocSinh = dataNguoiThan.getString(11);
                        ManHinhDangNhapActivity.databaseSQLite.InsertPayment(snapshot.getKey(),dataNguoiThan.getString(10));
                        NotifyManage.callNotify(mcontext,pendingIntent,idNotify++,NOTIFY_ID,"Có tài khoản tương tác", dataNguoiThan.getString(4) + " " + tenPhuHuynh + " đóng tiền cho " + tenHocSinh);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Intent i = new Intent(mcontext, SupportPaymentActivity.class);
                String tenPhuHuynh = "";
                String tenHocSinh = "";
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(mcontext,
                        0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                Cursor dataNguoiThan2 = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM KiemTraDongTien WHERE Trim(Uid) = '"+snapshot.getKey().trim()+"'");
                if (dataNguoiThan2.getCount() == 0){
                    Cursor dataNguoiThan = ManHinhDangNhapActivity.databaseSQLite.GetData("Select *from ThongTinNguoiThan nt, ThongTinHocSinh hs where TRIM(nt.MaHs) = TRIM(hs.MaHs) and TRIM(nt.Uid) = '" + snapshot.getKey().trim() +"'");
                    while (dataNguoiThan.moveToNext()){
                        tenPhuHuynh = dataNguoiThan.getString(2);
                        tenHocSinh = dataNguoiThan.getString(11);
                        ManHinhDangNhapActivity.databaseSQLite.InsertPayment(snapshot.getKey(),dataNguoiThan.getString(10));
                        NotifyManage.callNotify(mcontext,pendingIntent,idNotify++,NOTIFY_ID,"Có tài khoản tương tác", dataNguoiThan.getString(4) + " " + tenPhuHuynh + " đóng tiền cho " + tenHocSinh);
                    }
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
        databaseReference.child("MamNon").child("DuLieuDonHocSinh").child(textLop).child(ngayKiemTra).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Intent i = new Intent(mcontext, ManHinhDangNhapActivity.class);
                String tenPhuHuynh = "";
                String tenHocSinh = "";
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(mcontext,
                        0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                Cursor dataNguoiThan2 = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM UIDTag WHERE Trim(Uid) = '"+snapshot.getKey().trim()+"'");
                if (dataNguoiThan2.getCount() == 0){
                    String[] minuteLate = snapshot.getValue().toString().split(",");
                    if(Integer.parseInt(minuteLate[1]) > 0){
                        ManHinhDangNhapActivity.databaseSQLite.QuerryData("INSERT INTO UIDTag VALUES(null,'"+snapshot.getKey()+"','"+minuteLate[1]+"',"+0+")");
                    }else {
                        ManHinhDangNhapActivity.databaseSQLite.QuerryData("INSERT INTO UIDTag VALUES(null,'"+snapshot.getKey()+"','"+0+"',"+0+")");
                    }
                    if (listenData!=null){
                        listenData.onUIChange(true);
                    }
                    Cursor dataNguoiThan = ManHinhDangNhapActivity.databaseSQLite.GetData("Select *from ThongTinNguoiThan nt, ThongTinHocSinh hs where TRIM(nt.MaHs) = TRIM(hs.MaHs) and TRIM(nt.Uid) = '" + snapshot.getKey().trim() +"'");
                    while (dataNguoiThan.moveToNext()){
                        tenPhuHuynh = dataNguoiThan.getString(2);
                        tenHocSinh = dataNguoiThan.getString(11);
                        NotifyManage.callNotify(mcontext,pendingIntent,idNotify++,NOTIFY_ID,"Có tài khoản tương tác", dataNguoiThan.getString(4) + " " + tenPhuHuynh + " đến đón " + tenHocSinh);
//                    if (ringtone == null){
//                        ringtone = RingtoneManager.getRingtone(mcontext,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
//                    }
//                    playSound();
                    }
                }else{ }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Intent i = new Intent(mcontext, ManHinhDangNhapActivity.class);
                String tenPhuHuynh = "";
                String tenHocSinh = "";
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(mcontext,
                        0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                Cursor dataNguoiThan2 = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM UIDTag WHERE Trim(Uid) = '"+snapshot.getKey().trim()+"'");
                if (dataNguoiThan2.getCount() == 0){
                    String[] minuteLate = snapshot.getValue().toString().split(",");
                    if(Integer.parseInt(minuteLate[1]) > 0){
                        ManHinhDangNhapActivity.databaseSQLite.QuerryData("INSERT INTO UIDTag VALUES(null,'"+snapshot.getKey()+"','"+minuteLate[1]+"',"+0+")");
                    }else {
                        ManHinhDangNhapActivity.databaseSQLite.QuerryData("INSERT INTO UIDTag VALUES(null,'"+snapshot.getKey()+"','"+0+"',"+0+")");
                    }
                    if (listenData!=null){
                        listenData.onUIChange(true);
                    }
                    Cursor dataNguoiThan = ManHinhDangNhapActivity.databaseSQLite.GetData("Select *from ThongTinNguoiThan nt, ThongTinHocSinh hs where TRIM(nt.MaHs) = TRIM(hs.MaHs) and TRIM(nt.Uid) = '" + snapshot.getKey().trim() +"'");
                    while (dataNguoiThan.moveToNext()){
                        tenPhuHuynh = dataNguoiThan.getString(2);
                        tenHocSinh = dataNguoiThan.getString(11);
                        NotifyManage.callNotify(mcontext,pendingIntent,idNotify++,NOTIFY_ID,"Có tài khoản tương tác", dataNguoiThan.getString(4) + " " + tenPhuHuynh + " đến đón " + tenHocSinh);
                    }
                }else{ }
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
