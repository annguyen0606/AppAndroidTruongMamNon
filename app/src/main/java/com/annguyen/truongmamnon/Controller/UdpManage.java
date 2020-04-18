package com.annguyen.truongmamnon.Controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;

import com.annguyen.truongmamnon.Activity.ManHinhDangNhapActivity;
import com.annguyen.truongmamnon.Activity.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UdpManage extends BroadcastReceiver {
    static public String BOARD_CAST_NAME = "UdpManage";
    static public String NOTIFY_ID = "BOARD_CAST_NAME";
    public static String message = "";
    int idNotify = 2;
    boolean checkHasData = false;
    private UdpServerThread udpServerThread;
    private Context mcontext;
    private List<String> dataList;
    private static ListenData listenData;
    SharedPref sharedPref;
    static Ringtone ringtone;
    static DatagramSocket socket;
    void init(){
        sharedPref = new SharedPref(mcontext);
        dataList = new ArrayList<>();
        startUDP();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        init();
        NotifyManage.creatChannel(context,NOTIFY_ID,"Co Data", NotificationManager.IMPORTANCE_DEFAULT);
    }
    static public void stopMe(Context context){
        if (socket != null){
            socket.close();
        }
    }
    static public void startMe(Context context){
        Intent intent = new Intent();
        intent.setAction(BOARD_CAST_NAME);
        context.sendBroadcast(intent);
    }

    private class UdpServerThread extends Thread {
        int serverPort;

        public UdpServerThread(int serverPort) {
            super();
            this.serverPort = serverPort;
        }
        @Override
        public void run() {
            try {
                socket = new DatagramSocket(serverPort);
                String dataCheck = "";
                while(true){
                    byte[] buf = new byte[256];
                    // receive request
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);     //this code block the program flow
                    message = new String(packet.getData()).trim();
                    // send the response to the client at "address" and "port"
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    //System.out.println(port);
                    SharedPref.put(ManHinhDangNhapActivity.CURRENT_UID,message);
                    if (dataList.size() > 0){
                        for (int i = 0; i < dataList.size(); i++){
                            if (message.equals(dataList.get(i))){
                                checkHasData = true;
                                break;
                            }else {
                                checkHasData = false;
                            }
                        }
                    }else {
                        checkHasData = false;
                    }
                    if (message.length() == 16 && checkHasData == false && !dataCheck.equals(message)){
                        dataCheck = message;
                        dataList.add(message);
                        String thoigianStr = GetLateMinute();
                        Cursor dataNguoiThan = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM UIDTag WHERE Uid = '"+message+"'");
                        if (dataNguoiThan.getCount() == 0){
                            ManHinhDangNhapActivity.databaseSQLite.QuerryData("INSERT INTO UIDTag VALUES(null,'"+message+"',"+"'"+thoigianStr +"',"+0+")");
                            if (ringtone == null){
                                ringtone = RingtoneManager.getRingtone(mcontext,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                            }
                            playSound();
                            if (listenData!=null){
                                listenData.onUIChange(true);
                            }
                        }else{ }
                        ClearArray clearArray = new ClearArray();
                        clearArray.start();

                        Intent i = new Intent(mcontext, ManHinhDangNhapActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(mcontext,
                                0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotifyManage.callNotify(mcontext,pendingIntent,idNotify++,NOTIFY_ID,"Có tài khoản tương tác", message);
                        //mcontext.startActivity(i);
                        /*String dString = "1";
                        byte[] buff = dString.getBytes();
                        packet = new DatagramPacket(buff, buff.length, address, 6688);
                        socket.send(packet);*/
                    }else {
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(socket != null){
                    socket.close();
                }
            }
        }
    }

    private class ClearArray extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dataList.clear();

            System.out.println("Clear success");
        }
    }

    private void startUDP() {
        int UdpServerPORT = MainActivity.namePORT;
        udpServerThread = new UdpServerThread(UdpServerPORT);
        udpServerThread.start();
    }
    public interface  ListenData{
        public void onUIChange(Boolean status);
    }

    public static void setListener(ListenData listener){
       UdpManage.listenData = listener;
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

   private String GetLateMinute(){
        SimpleDateFormat gio = new SimpleDateFormat("HH");
        SimpleDateFormat phut = new SimpleDateFormat("mm");
        String gioString = gio.format(new Date());
        String phutString = phut.format(new Date());
       int tongThoiGianDonMuon = 0;
       if (Integer.parseInt(gioString) >= 9){
           int gioInt = Integer.parseInt(gioString) - 9;
           int phutInt = Integer.parseInt(phutString) - 0;

           tongThoiGianDonMuon = gioInt*60 + phutInt;
       }
       return String.valueOf(tongThoiGianDonMuon);
   }
}
