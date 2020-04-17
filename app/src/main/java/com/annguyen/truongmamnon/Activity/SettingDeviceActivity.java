package com.annguyen.truongmamnon.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.annguyen.truongmamnon.Controller.SharedPref;
import com.annguyen.truongmamnon.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class SettingDeviceActivity extends AppCompatActivity implements View.OnClickListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listDevices;
    private ArrayList<String> listNameDevices;
    private ArrayAdapter arrayAdapter;
    private static BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice[] bluetoothDevices;
    private Context context;
    private BluetoothSocket bluetoothSocket = null;
    private ProgressDialog dialog;
    private static final String APP_NAME = "Kindergarten";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int STATE_LISTENING = 1;
    private static final int STATE_CONNECTING = 2;
    private static final int STATE_CONNECTED = 3;
    private static final int STATE_CONNECTION_FAILED = 4;
    private static final int STATE_MESSAGE_RECEVIED = 5;
    private String portConnect,passWifi,ssidWifi;
    private int index = 0;
    private SendReceive sendReceive;
    private ImageView pullDown, backToMainActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device);
        KhoiTao();
        AnhXa();
        XinPhepDinhVi();
    }

    private void KhoiTao() {
        dialog = new ProgressDialog(SettingDeviceActivity.this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevices = new BluetoothDevice[100];
        listNameDevices = new ArrayList<>();
    }

    private void AnhXa() {
        listDevices = findViewById(R.id.lvListDevicesSettingActivity);
        swipeRefreshLayout = findViewById(R.id.pullToRefreshScanDevices);
        pullDown = findViewById(R.id.imagePullDown);
        backToMainActivity = findViewById(R.id.backMainActivityAtSettingActivity);

        findViewById(R.id.backMainActivityAtSettingActivity).setOnClickListener(this);

        arrayAdapter = new ArrayAdapter(SettingDeviceActivity.this,android.R.layout.simple_list_item_1,listNameDevices);
        listDevices.setAdapter(arrayAdapter);
        listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.setMessage("Connecting");
                dialog.show();

                BluetoothDevice deviceBluetooth = null;
                deviceBluetooth = bluetoothDevices[position];

                listNameDevices.clear();
                arrayAdapter.setNotifyOnChange(true);

                ClientClass clientClass = new ClientClass(deviceBluetooth);
                clientClass.start();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KiemTraBluetooth();
                ScanDevices();
                pullDown.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },5000);
            }
        });
    }


    void ScanDevices(){
        listDevices.setVisibility(View.VISIBLE);
        listNameDevices.clear();
        arrayAdapter.setNotifyOnChange(true);

        bluetoothAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
    }

    public void KiemTraBluetooth() {
        if (bluetoothAdapter == null){
            Toast.makeText(SettingDeviceActivity.this,"Điện thoại của bạn không hỗ trợ Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if (!bluetoothAdapter.isEnabled()){
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingDeviceActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            builder.setTitle("Yêu cầu tính năng Bluetooth");
            builder.setMessage("Bluetooth chưa bật. Vui lòng bật tính năng");
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                    /*Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth,1);*/
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case STATE_LISTENING:
                    break;
                case STATE_CONNECTING:
                    break;
                case STATE_CONNECTED:
                    dialog.dismiss();
                    pullDown.setVisibility(View.VISIBLE);
                    Toast.makeText(SettingDeviceActivity.this,"Đã kết nối thành công",Toast.LENGTH_SHORT).show();
                    Show_Dialog_Setup_Device();
                    break;
                case STATE_CONNECTION_FAILED:
                    dialog.dismiss();
                    pullDown.setVisibility(View.VISIBLE);
                    if (bluetoothSocket == null){
                    }else {
                        try {
                            bluetoothSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(SettingDeviceActivity.this,"Kết nối thất bại vui lòng kiểm tra lại",Toast.LENGTH_SHORT).show();
                    break;
                case STATE_MESSAGE_RECEVIED:
                    break;
            }
            return true;
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backMainActivityAtSettingActivity:
                onBackPressed();
                break;
        }
    }

    private class ClientClass extends Thread{
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass (BluetoothDevice device1){
            device = device1;
            try {
                socket=device1.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket = socket;
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void run(){
            bluetoothAdapter.cancelDiscovery();
            try {
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            }catch (IOException e){
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }
    private class SendReceive extends Thread{
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive(BluetoothSocket socket){
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut= bluetoothSocket.getOutputStream();
            }catch (IOException e){
                e.printStackTrace();
            }
            inputStream = tempIn;
            outputStream = tempOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while(true){
                try {
                    bytes = inputStream.read(buffer);
                    String readData = new String(buffer,0,bytes);
                    handler.obtainMessage(STATE_MESSAGE_RECEVIED,bytes,-1,readData).sendToTarget();
                }catch (IOException e){

                }
            }
        }

        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            }catch (IOException e){ }
        }
    }
    void XinPhepDinhVi(){
        if (ContextCompat.checkSelfPermission(SettingDeviceActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SettingDeviceActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(SettingDeviceActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            } else {
                ActivityCompat.requestPermissions(SettingDeviceActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
    }
    void Show_Dialog_Setup_Device() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingDeviceActivity.this);
        //View mView = View.inflate(getContext(),R.layout.setup_wifi_layout,null);
        View mView = getLayoutInflater().inflate(R.layout.setup_wifi_layout, null);
        final EditText mSSID = mView.findViewById(R.id.edtSSIDSetupWifiLayout);
        final EditText mPass = mView.findViewById(R.id.edtPassSetupWifiLayout);

        final String mPort = SharedPref.get(ManHinhDangNhapActivity.CURRENT_PORT,String.class);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssidwf = wifiInfo.getSSID().trim();
        ssidwf = ssidwf.substring(1,ssidwf.length() - 1);
        mSSID.setText(ssidwf);
        Button mSendToDevice = mView.findViewById(R.id.btnSendSetupWifiLayout);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        mSendToDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPort.equals("") && !mSSID.getText().toString().isEmpty() && !mPass.getText().toString().isEmpty()) {

                    portConnect = mPort;
                    passWifi = mPass.getText().toString().trim();
                    ssidWifi = mSSID.getText().toString().trim();
                    String dataSend = passWifi + "," + ssidWifi + "," + portConnect + ")";
                    Log.e("Log", dataSend);
                    sendReceive.write(dataSend.getBytes());
                    dialog.dismiss();
                    if (bluetoothSocket == null) {
                    } else {
                        try {
                            bluetoothSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(SettingDeviceActivity.this, "Nhập lại các thông số", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                bluetoothDevices[index] = device;
                index++;
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceName == null){
                    listNameDevices.add(deviceHardwareAddress);
                }
                else {
                    listNameDevices.add(deviceName);
                }
                arrayAdapter.notifyDataSetChanged();
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                index = 1;
            }
        }
    };
}
