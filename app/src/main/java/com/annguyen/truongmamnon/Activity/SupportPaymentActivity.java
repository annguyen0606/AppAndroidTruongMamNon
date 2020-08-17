package com.annguyen.truongmamnon.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.annguyen.truongmamnon.Adapter.GetMoneyAdapter;
import com.annguyen.truongmamnon.Controller.DataProvider;
import com.annguyen.truongmamnon.Controller.SharedPref;
import com.annguyen.truongmamnon.Model.ThongTinHocSinhThuTien;
import com.annguyen.truongmamnon.Model.TrangThaiHocSinhNopTien;
import com.annguyen.truongmamnon.R;
import com.google.android.gms.measurement.AppMeasurementInstallReferrerReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SupportPaymentActivity extends AppCompatActivity implements View.OnClickListener {
    private static SharedPref sharedPref;
    private static ListView danhSachHocSinh;
    private GetMoneyAdapter getMoneyAdapter;
    ImageView backMainActivity;
    private ArrayList<ThongTinHocSinhThuTien> arrayThongTinHocSinhThuTien;
    private ArrayList<ThongTinHocSinhThuTien> arrayThongTinHocSinhThuTienTemp1;
    private ArrayList<TrangThaiHocSinhNopTien> arrayThongTinHocSinhThuTienTemp2;
    DataProvider dataProvider;
    String maLop;
    byte[] byteImageStudent;
    boolean kiemTraKetNoiInternet = false;
    private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_payment);
        sharedPref = new SharedPref(SupportPaymentActivity.this);
        byteImageStudent = new byte[0];
        AnhXa();
    }

    private void AnhXa() {
        backMainActivity = findViewById(R.id.backMainActivityAtSupportPaymentActivity);
        refreshLayout = findViewById(R.id.pullToGetDataSupportPayment);

        findViewById(R.id.backMainActivityAtSupportPaymentActivity).setOnClickListener(this);
        danhSachHocSinh = findViewById(R.id.lvDanhSachHocSinhThuTien);
        arrayThongTinHocSinhThuTien = new ArrayList<>();
        arrayThongTinHocSinhThuTienTemp1 = new ArrayList<>();
        arrayThongTinHocSinhThuTienTemp2 = new ArrayList<>();
        getMoneyAdapter = new GetMoneyAdapter(SupportPaymentActivity.this,R.layout.dong_du_lieu_thanh_toan_tien,arrayThongTinHocSinhThuTien);
        danhSachHocSinh.setAdapter(getMoneyAdapter);
        maLop = SharedPref.get(ManHinhDangNhapActivity.CURRENT_Class,String.class);
        LoadDataNopTienTrongThang();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadDataNopTienTrongThang();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
    }
    void LoadDataNopTienTrongThang(){
        arrayThongTinHocSinhThuTien.clear();
        arrayThongTinHocSinhThuTienTemp1.clear();
        arrayThongTinHocSinhThuTienTemp2.clear();
        Cursor duLieuHocSinh = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinHocSinh where TRIM(Lop) = '" + maLop.trim() +"'");
        while(duLieuHocSinh.moveToNext()){
            arrayThongTinHocSinhThuTienTemp1.add(new ThongTinHocSinhThuTien(duLieuHocSinh.getString(2),
                    duLieuHocSinh.getString(3),duLieuHocSinh.getString(1),
                    "500000",0,duLieuHocSinh.getBlob(7)));;
        }
        LayThongTinHocSinhNopTienFromServer layThongTinHocSinhNopTienFromServer = new LayThongTinHocSinhNopTienFromServer();
        layThongTinHocSinhNopTienFromServer.execute();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backMainActivityAtSupportPaymentActivity:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED){
                    kiemTraKetNoiInternet = true;
                }else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED){
                    kiemTraKetNoiInternet = false;
                    Toast.makeText(SupportPaymentActivity.this,"Lỗi Intertnet, Vui lòng kiểm tra lại!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private class LayThongTinHocSinhNopTienFromServer extends AsyncTask<Void,Void,ArrayList<TrangThaiHocSinhNopTien>>{
        public LayThongTinHocSinhNopTienFromServer() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<TrangThaiHocSinhNopTien> doInBackground(Void... voids) {
            String ngayKiemTra = new SimpleDateFormat("yyyy-MM").format(new Date());
            return dataProvider.getInstance().LayDanhSachHocSinhNopTien(maLop,ngayKiemTra);
        }

        @Override
        protected void onPostExecute(ArrayList<TrangThaiHocSinhNopTien> trangThaiHocSinhNopTiens) {
            System.out.println("ANC: " + String.valueOf(trangThaiHocSinhNopTiens.size()));
            for(ThongTinHocSinhThuTien hocSinhThuTien : arrayThongTinHocSinhThuTienTemp1){
                int trangThaiThuTien = 0;
                String soTienNeed = "0";
                if(trangThaiHocSinhNopTiens.size() > 0){
                    for (TrangThaiHocSinhNopTien hocSinhDaNop : trangThaiHocSinhNopTiens){
                        if(hocSinhThuTien.getMaHs().trim().equals(hocSinhDaNop.getMaHocSinh().trim())){
                            if(Integer.parseInt(hocSinhDaNop.getTrangThaiThu().trim()) == 1){
                                trangThaiThuTien = 1;
                            }
                            soTienNeed = hocSinhDaNop.getSoTien();
                        }
                    }
                }else{
                    trangThaiThuTien = 0;
                    soTienNeed = "0";
                }
                arrayThongTinHocSinhThuTien.add(new ThongTinHocSinhThuTien(hocSinhThuTien.getTen(),
                        hocSinhThuTien.getNgaySinh(),hocSinhThuTien.getMaHs(),soTienNeed,
                        trangThaiThuTien,hocSinhThuTien.getHinhAnh()));
                getMoneyAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(trangThaiHocSinhNopTiens);
        }
    }
}
