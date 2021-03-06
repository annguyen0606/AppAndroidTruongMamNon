package com.annguyen.truongmamnon.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.annguyen.truongmamnon.Adapter.MainViewPagerAdapter;
import com.annguyen.truongmamnon.Controller.DataProvider;
import com.annguyen.truongmamnon.Controller.SharedPref;
import com.annguyen.truongmamnon.Controller.FirebaseManage;
import com.annguyen.truongmamnon.Fragment.FragmentDanhSachChamThe;
import com.annguyen.truongmamnon.Fragment.FragmentDanhSachChuaChamThe;
import com.annguyen.truongmamnon.Model.ThongTinHocSinh;
import com.annguyen.truongmamnon.Model.ThongTinNguoiThan;
import com.annguyen.truongmamnon.R;
import com.annguyen.truongmamnon.Service.FirebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements FirebaseManage.ListenData, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    public static TabLayout tabLayout; //Bien tablayout
    public static int namePORT = 0;
    private ViewPager viewPager;//Bien viewpager de cho phep nguoi dung vuot de chuyen fragment

    private Intent intent = null; //Bien Intent
    private Context context; //Bien context
    private SharedPref sharedPref; //Bien sharedPreferences de truy mot vai thong tin data luu
    private static String textTenLop = ""; //Lay gia tri ten lop ma giao vien hien tai dang dang nhap
    private DataProvider dataProvider; //Class de moc data from Database sql server

    public static boolean kiemTraKetNoiInternet = false;
    TextView textHello;
    FloatingActionButton floatAddAccount;
    NavigationView navigationMenu;
    String textMaGiaoVien = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        sharedPref = new SharedPref(context);
        textMaGiaoVien = SharedPref.get(ManHinhDangNhapActivity.CURRENT_TEACHER,String.class);
        /*
        * Khoi tao truyen du lieu interface*/
        FirebaseManage.setListener(MainActivity.this);
        /*Ham Anh xa*/
        AnhXa();
        /*Ham Init*/
        init();
        /*
        * Khoi tao context
        * Khoi tao Intent
        * Lay gia tri ten lop*/
        intent = new Intent(this, FirebaseService.class);

        textTenLop = SharedPref.get(ManHinhDangNhapActivity.CURRENT_Class,String.class);
        namePORT = Integer.parseInt(SharedPref.get(ManHinhDangNhapActivity.CURRENT_PORT,String.class));
        /*Bat service*/
        if (SharedPref.get(ManHinhDangNhapActivity.CURRENT_LOGIN_STATUS,Boolean.class) == false){
            SharedPref.put(ManHinhDangNhapActivity.CURRENT_LOGIN_STATUS,true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            }else {
                startService(intent);
            }
        }
    }
    private void init() {
        /*Khoi tao viewpager
        * ADD fragments*/
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mainViewPagerAdapter.addFragment(new FragmentDanhSachChamThe(),"Danh sách đón");
        mainViewPagerAdapter.addFragment(new FragmentDanhSachChuaChamThe(),"Danh sách chờ");

        viewPager.setAdapter(mainViewPagerAdapter);
        /*
        * Set Icon cho cac fragment tren tablayout*/
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.getTabAt(0).getOrCreateBadge().setNumber(100);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_conek2);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_access_time_black_24dp);

        /*Tat ca Ringtone khi khoi tao Activity*/
        FirebaseManage.stopSound();
        /*Lay date dang nhap
        * Neu dang nhap vao mot ngay moi se clear du lieu o table UIDTag trong sqlite*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(new Date());
        String date2 = new SimpleDateFormat("MM").format(new Date()).trim();
        String thang = SharedPref.get(ManHinhDangNhapActivity.CURRENT_DATE,String.class);
        String[] checkThang = thang.split("/");
        if (!date.equals(SharedPref.get(ManHinhDangNhapActivity.CURRENT_DATE,String.class))){
            SharedPref.put(ManHinhDangNhapActivity.CURRENT_DATE,date);
            ManHinhDangNhapActivity.databaseSQLite.QuerryData("DELETE FROM UIDTag");
            ManHinhDangNhapActivity.databaseSQLite.QuerryData("DELETE FROM KiemTraDonCon");
            ManHinhDangNhapActivity.databaseSQLite.QuerryData("VACUUM");
        }
        if(!date2.equals(checkThang[1].trim())){
            ManHinhDangNhapActivity.databaseSQLite.QuerryData("DELETE FROM KiemTraDongTien");
        }
        int countDontTakeStudent = SharedPref.get(ManHinhDangNhapActivity.CURRENT_STUDENTS_COUNT,Integer.class);
        if (countDontTakeStudent > 0){
            tabLayout.getTabAt(0).getOrCreateBadge().setVisible(true);
            tabLayout.getTabAt(0).getOrCreateBadge().setNumber(countDontTakeStudent);
            tabLayout.getTabAt(0).getOrCreateBadge().setMaxCharacterCount(3);
        }else {
            tabLayout.getTabAt(0).getOrCreateBadge().setVisible(false);
        }
    }

    private void AnhXa() {
        tabLayout = findViewById(R.id.myTabLayout);
        viewPager = findViewById(R.id.mainViewpagerMainActivity);
        //floatAddAccount = findViewById(R.id.floatAddAccount);
        navigationMenu = findViewById(R.id.navigationMenu);
        textHello = findViewById(R.id.tvNameMainActivity);

        Cursor DataTeacher = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinGiaoVien WHERE MaGV = '"+textMaGiaoVien+"'");
        while (DataTeacher.moveToNext()){
            textHello.setText("Xin chào: "+DataTeacher.getString(2));
        }


        //findViewById(R.id.floatAddAccount).setOnClickListener(this);

        navigationMenu.setNavigationItemSelectedListener(this);
    }
    /*Bat su kien trang thai truyen du lieu interface*/
    @Override
    public void onUIChange(Boolean status) {
        if (status = true){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    int countDontTakeStudent = FragmentDanhSachChamThe.LoadDuLieu();
                    FragmentDanhSachChuaChamThe.LoadDuLieuChuaChamThe();
                    if (countDontTakeStudent > 0){
                        SharedPref.put(ManHinhDangNhapActivity.CURRENT_STUDENTS_COUNT,countDontTakeStudent);
                        tabLayout.getTabAt(0).getOrCreateBadge().setVisible(true);
                        tabLayout.getTabAt(0).getOrCreateBadge().setNumber(countDontTakeStudent);
                        tabLayout.getTabAt(0).getOrCreateBadge().setMaxCharacterCount(3);
                    }else {
                        tabLayout.getTabAt(0).getOrCreateBadge().setVisible(false);
                    }
                }
            });
        }else {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.floatAddAccount:
//                startActivity(new Intent(MainActivity.this, AddAccountActivity.class));
//                break;
        }
    }

    private class ClearThongTinDonCong extends AsyncTask<Void,Void,Void>{
        AlertDialog alertDialog;
        public ClearThongTinDonCong(Context mContext){
            alertDialog = new SpotsDialog.Builder().setContext(mContext)
                    .setMessage("Clearing...")
                    .setCancelable(false)
                    .build();
        }
        @Override
        protected void onPreExecute() {
            alertDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dataProvider.getInstance().ClearThongTin("DELETE FROM ThongTinNopTien");
            dataProvider.getInstance().ClearThongTin("DELETE FROM ThoiGianDonCon");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            alertDialog.dismiss();
        }
    }

    /*Class Update du lieu tu database sql server*/
    private class UpdateDataFromDB extends AsyncTask<Void,Void,Void>{
        AlertDialog alertDialog;
        public UpdateDataFromDB(Context mContext) {
            alertDialog = new SpotsDialog.Builder().setContext(mContext)
                    .setMessage("Please waiting...")
                    .setCancelable(false)
                    .build();
        }
        @Override
        protected void onPreExecute() {
            alertDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<ThongTinHocSinh> arrayDanhSachHocSinh = new ArrayList<>();
            ArrayList<ThongTinNguoiThan> arrayDanhSachNguoiThan = new ArrayList<>();
            arrayDanhSachHocSinh = dataProvider.getInstance().LayDanhSachThongTinHocSinh("SELECT *FROM ThongTinHocSinh WHERE malop ='"+textTenLop+"'");
            arrayDanhSachNguoiThan = dataProvider.getInstance().LayDanhSachThongTinNguoiThan("SELECT *FROM ThongTinNguoiThan WHERE malop ='"+textTenLop+"'");
            for (int i = 0; i < arrayDanhSachHocSinh.size(); i++){
                ManHinhDangNhapActivity.databaseSQLite.InsertThongTinHocSinh(arrayDanhSachHocSinh.get(i).getMaHocSinh().trim(),arrayDanhSachHocSinh.get(i).getHoTen().trim(),
                        arrayDanhSachHocSinh.get(i).getNgaySinh().trim(), arrayDanhSachHocSinh.get(i).getLopHocSinh().trim(),
                        arrayDanhSachHocSinh.get(i).getGioiTinh().trim(),arrayDanhSachHocSinh.get(i).getDiaChi().trim(),
                        arrayDanhSachHocSinh.get(i).getHinhAnh(),arrayDanhSachHocSinh.get(i).getMaGiaoVien().trim());
            }
            for (int i = 0; i < arrayDanhSachNguoiThan.size(); i++){
                ManHinhDangNhapActivity.databaseSQLite.InsertThongTinNguoiThan(arrayDanhSachNguoiThan.get(i).getMaUID().trim(),arrayDanhSachNguoiThan.get(i).getHoTen(),
                        arrayDanhSachNguoiThan.get(i).getDiaChi(),arrayDanhSachNguoiThan.get(i).getQuanHe(),arrayDanhSachNguoiThan.get(i).getMaHocSinh(),
                        arrayDanhSachNguoiThan.get(i).getSoDienThoai(),arrayDanhSachNguoiThan.get(i).getHinhAnh(),arrayDanhSachNguoiThan.get(i).getLop());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    int countDontTakeStudent = FragmentDanhSachChamThe.LoadDuLieu();
                    FragmentDanhSachChuaChamThe.LoadDuLieuChuaChamThe();
                    if (countDontTakeStudent > 0){
                        SharedPref.put(ManHinhDangNhapActivity.CURRENT_STUDENTS_COUNT,countDontTakeStudent);
                        tabLayout.getTabAt(0).getOrCreateBadge().setVisible(true);
                        tabLayout.getTabAt(0).getOrCreateBadge().setNumber(countDontTakeStudent);
                        tabLayout.getTabAt(0).getOrCreateBadge().setMaxCharacterCount(3);
                    }else {
                        tabLayout.getTabAt(0).getOrCreateBadge().setVisible(false);
                    }
                }
            });
            alertDialog.dismiss();
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
                    Toast.makeText(MainActivity.this,"Lỗi Intertnet, Xin kiểm tra lại!",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.clearMenu:
//                ClearThongTinDonCong clearThongTinDonCong = new ClearThongTinDonCong(MainActivity.this);
//                clearThongTinDonCong.execute();
                ManHinhDangNhapActivity.databaseSQLite.QuerryData("DELETE FROM UIDTag");
                ManHinhDangNhapActivity.databaseSQLite.QuerryData("DELETE FROM KiemTraDonCon");
                ManHinhDangNhapActivity.databaseSQLite.QuerryData("DELETE FROM KiemTraDongTien");
                ManHinhDangNhapActivity.databaseSQLite.QuerryData("VACUUM");
                int countDontTakeStudent = FragmentDanhSachChamThe.LoadDuLieu();
                FragmentDanhSachChuaChamThe.LoadDuLieuChuaChamThe();
                if (countDontTakeStudent > 0){
                    SharedPref.put(ManHinhDangNhapActivity.CURRENT_STUDENTS_COUNT,countDontTakeStudent);
                    tabLayout.getTabAt(0).getOrCreateBadge().setVisible(true);
                    tabLayout.getTabAt(0).getOrCreateBadge().setNumber(countDontTakeStudent);
                    tabLayout.getTabAt(0).getOrCreateBadge().setMaxCharacterCount(3);
                }else {
                    tabLayout.getTabAt(0).getOrCreateBadge().setVisible(false);
                }
                FragmentDanhSachChuaChamThe.LoadDuLieuChuaChamThe();
                break;
//            case R.id.settingMenu:
//                startActivity(new Intent(MainActivity.this, SettingDeviceActivity.class));
//                break;
            case R.id.updateMenu:
                if (kiemTraKetNoiInternet == true){
                    ManHinhDangNhapActivity.databaseSQLite.QuerryData("DELETE FROM ThongTinHocSinh");
                    ManHinhDangNhapActivity.databaseSQLite.QuerryData("DELETE FROM ThongTinNguoiThan");
                    ManHinhDangNhapActivity.databaseSQLite.QuerryData("VACUUM");
                    UpdateDataFromDB updateDataFromDB = new UpdateDataFromDB(MainActivity.this);
                    updateDataFromDB.execute();
                }else {
                    Toast.makeText(MainActivity.this,"Lỗi Intertnet, Không thể cập nhật",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.logOutMenu:
                SharedPref.put(ManHinhDangNhapActivity.CURRENT_LOGIN_STATUS,false);
                stopService(intent);
                /*android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);*/
                finish();
                moveTaskToBack(true);
                break;
            case R.id.statisticMenu:
                startActivity(new Intent(MainActivity.this, StatisticActivity.class));
                break;
            case R.id.paymentMenu:
                startActivity(new Intent(MainActivity.this, SupportPaymentActivity.class));
                break;
        }
        return true;
    }
}
