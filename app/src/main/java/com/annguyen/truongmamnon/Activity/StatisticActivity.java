package com.annguyen.truongmamnon.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.annguyen.truongmamnon.Adapter.TotalLateTimeAdapter;
import com.annguyen.truongmamnon.Controller.DataProvider;
import com.annguyen.truongmamnon.Controller.SharedPref;
import com.annguyen.truongmamnon.Model.ThongTinHocSinh;
import com.annguyen.truongmamnon.Model.ThongTinThongKe;
import com.annguyen.truongmamnon.Model.TrangThaiHocSinhNopTien;
import com.annguyen.truongmamnon.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StatisticActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner classList, monthList;
    private ImageView exportCSV;
    private static ListView listViewTotalLateMinute;
    private TotalLateTimeAdapter totalLateTimeAdapter;
    private ArrayList<ThongTinThongKe> arrayThongTinThongKe;
    private SwipeRefreshLayout refreshLayout;
    DataProvider dataProvider;
    String txtLop = "";
    String txtMaLop = ""; //Lay ma lop de tim ma truong => Sau khi tim duoc ma truong se loat danh sach cac lop trong truong do
    private SharedPref sharedPref;
    int loaitaikhoan = 5;
    ArrayList<ThongTinHocSinh> arrayDanhSachHocSinh;
    ArrayList<Integer> arrayPhut;
    ArrayAdapter arrayAdapterClass;

    String maGiaoVien = "";
    String ngayThangThongKe = "";
    ImageView backMainActivity;
    boolean kiemTraKetNoiInternet = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        sharedPref = new SharedPref(StatisticActivity.this);
        AnhXa();
    }

    private void AnhXa() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ngayThangThongKe = dateFormat.format(new Date());
        maGiaoVien = SharedPref.get(ManHinhDangNhapActivity.CURRENT_TEACHER,String.class);

        exportCSV = findViewById(R.id.imageExportFragmentTotalLateTime);
        listViewTotalLateMinute = findViewById(R.id.lvListTotalLateMinute);
        arrayThongTinThongKe = new ArrayList<>();
        totalLateTimeAdapter = new TotalLateTimeAdapter(StatisticActivity.this,R.layout.dong_du_lieu_fragment_total_late_minute,arrayThongTinThongKe);
        listViewTotalLateMinute.setAdapter(totalLateTimeAdapter);

        classList = findViewById(R.id.spinnerClassTotalLateMinute);
        monthList = findViewById(R.id.spinnerMonthTotalLateMinute);
        backMainActivity = findViewById(R.id.backMainActivityAtStatisticActivity);

        refreshLayout = findViewById(R.id.pullToGetDataTotalLateTime);

        findViewById(R.id.imageExportFragmentTotalLateTime).setOnClickListener(this);
        findViewById(R.id.backMainActivityAtStatisticActivity).setOnClickListener(this);
        ArrayList<String> arraySpinnerMonth = new ArrayList<>();
        arraySpinnerMonth.add("Tháng 1");
        arraySpinnerMonth.add("Tháng 2");
        arraySpinnerMonth.add("Tháng 3");
        arraySpinnerMonth.add("Tháng 4");
        arraySpinnerMonth.add("Tháng 5");
        arraySpinnerMonth.add("Tháng 6");
        arraySpinnerMonth.add("Tháng 7");
        arraySpinnerMonth.add("Tháng 8");
        arraySpinnerMonth.add("Tháng 9");
        arraySpinnerMonth.add("Tháng 10");
        arraySpinnerMonth.add("Tháng 11");
        arraySpinnerMonth.add("Tháng 12");
        ArrayAdapter arrayAdapterMonth = new ArrayAdapter(StatisticActivity.this,android.R.layout.simple_list_item_1,arraySpinnerMonth);
        monthList.setAdapter(arrayAdapterMonth);

        loaitaikhoan = SharedPref.get(ManHinhDangNhapActivity.CURRENT_ACCOUNT,Integer.class);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshDataClass();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
    }

    void RefreshDataClass(){
        if (kiemTraKetNoiInternet == true){
            if (classList.getCount() > 0){
                txtLop = classList.getSelectedItem().toString().trim();
                arrayThongTinThongKe.clear();
                totalLateTimeAdapter.notifyDataSetChanged();
                switch (loaitaikhoan){
                    case 3:
                        if (classList.getSelectedItem().toString().trim().equals(txtMaLop)){
                            arrayDanhSachHocSinh = new ArrayList<>();
                            arrayDanhSachHocSinh = dataProvider.getInstance().LayDanhSachThongTinHocSinh("SELECT *FROM ThongTinHocSinh WHERE lop ='"+txtMaLop+"'");
                            LayThongTinTuDB layThongTinTuDB = new LayThongTinTuDB();
                            layThongTinTuDB.execute(txtMaLop);
                        }else {
                            Toast.makeText(StatisticActivity.this,"Bạn không thể xem dữ liệu lớp khác",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        arrayDanhSachHocSinh = new ArrayList<>();
                        arrayDanhSachHocSinh = dataProvider.getInstance().LayDanhSachThongTinHocSinh("SELECT *FROM ThongTinHocSinh WHERE lop ='"+txtLop+"'");
                        LayThongTinTuDB layThongTinTuDB = new LayThongTinTuDB();
                        layThongTinTuDB.execute(txtLop);
                        break;
                }
            }else {
                GetClassList getClassList = new GetClassList();
                getClassList.execute();
            }
        }else {
            Toast.makeText(StatisticActivity.this,"Xin kiểm tra lại kết nối Internet",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backMainActivityAtStatisticActivity:
                onBackPressed();
                break;
            case R.id.imageExportFragmentTotalLateTime:
                if (kiemTraKetNoiInternet == true){
                    StringBuilder data = new StringBuilder();
                    data.append("Mã học sinh,Mã giáo viên,Tên học sinh,Tháng,Số phút,Tổng số tiền,Trạng thái nộp,Mã lớp");

                    if (arrayThongTinThongKe.size() > 0){
                        for (int i = 0; i < arrayThongTinThongKe.size(); i++){
                            if (arrayThongTinThongKe.get(i).getStatusPayment() == 0){
                                data.append("\n"+arrayThongTinThongKe.get(i).getMaHS()+","+maGiaoVien+","+arrayThongTinThongKe.get(i).getHoTen()+","+ngayThangThongKe+","
                                        +arrayThongTinThongKe.get(i).getThoiGian()+","+arrayThongTinThongKe.get(i).getMoney()+",Chưa nộp tiền"+","+txtLop);
                            }else if (arrayThongTinThongKe.get(i).getStatusPayment() == 1){
                                data.append("\n"+arrayThongTinThongKe.get(i).getMaHS()+","+maGiaoVien+","+arrayThongTinThongKe.get(i).getHoTen()+","+ngayThangThongKe+","
                                        +arrayThongTinThongKe.get(i).getThoiGian()+","+arrayThongTinThongKe.get(i).getMoney()+",Đã nộp tiền"+","+txtLop);
                            }
                        }
                        try {
                            FileOutputStream out = openFileOutput("DuLieuThongKeThuTien.csv",Context.MODE_PRIVATE);
                            out.write((data.toString()).getBytes());
                            out.close();

                            File fileLocation = new File(getApplicationContext().getFilesDir(),"DuLieuThongKeThuTien.csv");
                            Uri path = FileProvider.getUriForFile(getApplicationContext(),"com.annguyen.truongmamnon.Fragment",fileLocation);
                            Intent fileIntent = new Intent(Intent.ACTION_SEND);
                            fileIntent.setType("text/csv;charset=UTF-8");
                            fileIntent.putExtra(Intent.EXTRA_SUBJECT,"DuLieuThongKe");
                            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            fileIntent.putExtra(Intent.EXTRA_STREAM,path);
                            startActivity(Intent.createChooser(fileIntent,"Send Mail"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(StatisticActivity.this,"Bạn chưa lấy dữ liệu",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(StatisticActivity.this,"Xin kiểm tra lại kết nối Internet!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class LayThongTinTuDB extends AsyncTask<String,Void,ArrayList<ThongTinThongKe>> {
        public LayThongTinTuDB() {
        }

        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<ThongTinThongKe> doInBackground(String... strings) {
            String dateTime2 = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            String date = dateFormat.format(new Date());
            arrayPhut = new ArrayList<>();
            switch (monthList.getSelectedItemPosition()){
                case 0:
                    dateTime2 = "01/" + date;
                    break;
                case 1:
                    dateTime2 = "02/" + date;
                    break;
                case 2:
                    dateTime2 = "03/" + date;
                    break;
                case 3:
                    dateTime2 = "04/" + date;
                    break;
                case 4:
                    dateTime2 = "05/" + date;
                    break;
                case 5:
                    dateTime2 = "06/" + date;
                    break;
                case 6:
                    dateTime2 = "07/" + date;
                    break;
                case 7:
                    dateTime2 = "08/" + date;
                    break;
                case 8:
                    dateTime2 = "09/" + date;
                    break;
                case 9:
                    dateTime2 = "10/" + date;
                    break;
                case 10:
                    dateTime2 = "11/" + date;
                    break;
                case 11:
                    dateTime2 = "12/" + date;
                    break;
            }

            for (int i = 0; i < arrayDanhSachHocSinh.size(); i++){
                arrayPhut.add(dataProvider.getInstance().GetLateMinuteFromMaHs(arrayDanhSachHocSinh.get(i).getMaHocSinh(),dateTime2));
            }
            ArrayList<TrangThaiHocSinhNopTien> danhHocSinhNopTien = dataProvider.getInstance().LayDanhSachHocSinhNopTien(txtLop,dateTime2);
            for (int i = 0; i < arrayDanhSachHocSinh.size(); i++){
                ThongTinThongKe thongTinThongKe = new ThongTinThongKe("ABC","ABC",0,"0",0);
                thongTinThongKe.setMaHS(arrayDanhSachHocSinh.get(i).getMaHocSinh());
                thongTinThongKe.setHoTen(arrayDanhSachHocSinh.get(i).getHoTen());
                thongTinThongKe.setThoiGian(arrayPhut.get(i));
                int an = 0;
                for(int j = 0; j < danhHocSinhNopTien.size(); j++){
                    if (arrayDanhSachHocSinh.get(i).getMaHocSinh().trim().equals(danhHocSinhNopTien.get(j).getMaHocSinh().trim()) && danhHocSinhNopTien.get(j).getTrangThaiThu().trim().equals("1")){
                        an = 1;
                        thongTinThongKe.setMoney(danhHocSinhNopTien.get(j).getSoTien().trim());
                    }else if (arrayDanhSachHocSinh.get(i).getMaHocSinh().trim().equals(danhHocSinhNopTien.get(j).getMaHocSinh().trim()) && danhHocSinhNopTien.get(j).getTrangThaiThu().trim().equals("0")){
                        thongTinThongKe.setMoney(danhHocSinhNopTien.get(j).getSoTien().trim());
                    }
                }
                thongTinThongKe.setStatusPayment(an);
                arrayThongTinThongKe.add(thongTinThongKe);
            }
            return arrayThongTinThongKe;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(ArrayList<ThongTinThongKe> thongTinThongKes) {
            totalLateTimeAdapter.notifyDataSetChanged();
        }
    }

    private class GetClassList extends AsyncTask<Void,Void,ArrayList<String>> {

        public GetClassList() {
        }

        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> arrayClass = new ArrayList<>();

            txtMaLop = SharedPref.get(ManHinhDangNhapActivity.CURRENT_Class,String.class);

            String maTruong = dataProvider.getInstance().LayMaTruongTruong(txtMaLop);
            arrayClass = dataProvider.getInstance().LayDanhSachLop(maTruong);

            return arrayClass;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            arrayAdapterClass = new ArrayAdapter(StatisticActivity.this,android.R.layout.simple_list_item_1,strings);
            classList.setAdapter(arrayAdapterClass);
            txtLop = classList.getSelectedItem().toString().trim();
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
                    Toast.makeText(StatisticActivity.this,"Lỗi Intertnet, Vui lòng kiểm tra lại!",Toast.LENGTH_SHORT).show();
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
}
