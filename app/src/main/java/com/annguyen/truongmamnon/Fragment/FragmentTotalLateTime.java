package com.annguyen.truongmamnon.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.annguyen.truongmamnon.Activity.MainActivity;
import com.annguyen.truongmamnon.Activity.ManHinhDangNhapActivity;
import com.annguyen.truongmamnon.Adapter.TotalLateTimeAdapter;
import com.annguyen.truongmamnon.Controller.DataProvider;
import com.annguyen.truongmamnon.Controller.SharedPref;
import com.annguyen.truongmamnon.Model.HocSinhNopTien;
import com.annguyen.truongmamnon.Model.ThongTinHocSinh;
import com.annguyen.truongmamnon.Model.ThongTinThongKe;
import com.annguyen.truongmamnon.Model.TrangThaiHocSinhNopTien;
import com.annguyen.truongmamnon.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentTotalLateTime extends Fragment implements View.OnClickListener {
    private View view;
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
    ArrayList<String> arrayMoney;
    ArrayAdapter arrayAdapterClass;

    String maGiaoVien = "";
    String ngayThangThongKe = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_total_late_time,container,false);
        sharedPref = new SharedPref(view.getContext());
        AnhXa();
        return view;
    }

    private void AnhXa() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ngayThangThongKe = dateFormat.format(new Date());
        maGiaoVien = SharedPref.get(ManHinhDangNhapActivity.CURRENT_TEACHER,String.class);

        exportCSV = view.findViewById(R.id.imageExportFragmentTotalLateTime);
        listViewTotalLateMinute = view.findViewById(R.id.lvListTotalLateMinute);
        arrayThongTinThongKe = new ArrayList<>();
        totalLateTimeAdapter = new TotalLateTimeAdapter(view.getContext(),R.layout.dong_du_lieu_fragment_total_late_minute,arrayThongTinThongKe);
        listViewTotalLateMinute.setAdapter(totalLateTimeAdapter);

        classList = view.findViewById(R.id.spinnerClassTotalLateMinute);
        monthList = view.findViewById(R.id.spinnerMonthTotalLateMinute);

        refreshLayout = view.findViewById(R.id.pullToGetDataTotalLateTime);

        view.findViewById(R.id.imageExportFragmentTotalLateTime).setOnClickListener(this);
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
        ArrayAdapter arrayAdapterMonth = new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arraySpinnerMonth);
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
        if (MainActivity.kiemTraKetNoiInternet == true){
            if (classList.getCount() == 0){
                GetClassList getClassList = new GetClassList(view.getContext());
                getClassList.execute();
            }else {
                txtLop = classList.getSelectedItem().toString().trim();
            }
            arrayThongTinThongKe.clear();
            totalLateTimeAdapter.notifyDataSetChanged();
            switch (loaitaikhoan){
                case 3:
                    if (classList.getCount() > 0){
                        if (classList.getSelectedItem().toString().trim().equals(txtMaLop)){
                            //Toast.makeText(view.getContext(),txtLop,Toast.LENGTH_SHORT).show();
                            arrayDanhSachHocSinh = new ArrayList<>();
                            arrayDanhSachHocSinh = dataProvider.getInstance().LayDanhSachThongTinHocSinh("SELECT *FROM ThongTinHocSinh WHERE lop ='"+txtMaLop+"'");
                            GetMinuteLateFromDB2 getMinuteLateFromDB2 = new GetMinuteLateFromDB2(view.getContext());
                            getMinuteLateFromDB2.execute(txtMaLop);
                            //Toast.makeText(view.getContext(),arrayThongTinThongKe.get(1).getUid().toString().trim(),Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(view.getContext(),"Bạn không thể xem dữ liệu lớp khác",Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 2:
                    arrayDanhSachHocSinh = new ArrayList<>();
                    arrayDanhSachHocSinh = dataProvider.getInstance().LayDanhSachThongTinHocSinh("SELECT *FROM ThongTinHocSinh WHERE lop ='"+txtLop+"'");
                    GetMinuteLateFromDB2 getMinuteLateFromDB3 = new GetMinuteLateFromDB2(view.getContext());
                    getMinuteLateFromDB3.execute(txtLop);
                    break;
            }
        }else {
            Toast.makeText(view.getContext(),"Xin kiểm tra lại kết nối Internet",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageExportFragmentTotalLateTime:
                if (MainActivity.kiemTraKetNoiInternet == true){
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
                            FileOutputStream out = view.getContext().openFileOutput("DuLieuThongKeThuTien.csv",Context.MODE_PRIVATE);
                            out.write((data.toString()).getBytes());
                            out.close();

                            File fileLocation = new File(view.getContext().getApplicationContext().getFilesDir(),"DuLieuThongKeThuTien.csv");
                            Uri path = FileProvider.getUriForFile(view.getContext().getApplicationContext(),"com.annguyen.truongmamnon.Fragment",fileLocation);
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
                        Toast.makeText(view.getContext(),"Bạn chưa lấy dữ liệu",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(view.getContext(),"Xin kiểm tra lại kết nối Internet!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class GetMinuteLateFromDB2 extends AsyncTask<String,Void,ArrayList<ThongTinThongKe>> {
        //ProgressDialog progressDialog;

        public GetMinuteLateFromDB2(Context mContext) {
            //progressDialog = new ProgressDialog(mContext);
        }

        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<ThongTinThongKe> doInBackground(String... strings) {
            String dateTime = "";
            String dateTime2 = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            String date = dateFormat.format(new Date());
            arrayPhut = new ArrayList<>();

            switch (monthList.getSelectedItemPosition()){
                case 0:
                    dateTime = date + "/01";
                    dateTime2 = "01/" + date;
                    break;
                case 1:
                    dateTime = date + "/02";
                    dateTime2 = "02/" + date;
                    break;
                case 2:
                    dateTime = date + "/03";
                    dateTime2 = "03/" + date;
                    break;
                case 3:
                    dateTime = date + "/04";
                    dateTime2 = "04/" + date;
                    break;
                case 4:
                    dateTime = date + "/05";
                    dateTime2 = "05/" + date;
                    break;
                case 5:
                    dateTime = date + "/06";
                    dateTime2 = "06/" + date;
                    break;
                case 6:
                    dateTime = date + "/07";
                    dateTime2 = "07/" + date;
                    break;
                case 7:
                    dateTime = date + "/08";
                    dateTime2 = "08/" + date;
                    break;
                case 8:
                    dateTime = date + "/09";
                    dateTime2 = "09/" + date;
                    break;
                case 9:
                    dateTime = date + "/10";
                    dateTime2 = "10/" + date;
                    break;
                case 10:
                    dateTime = date + "/11";
                    dateTime2 = "11/" + date;
                    break;
                case 11:
                    dateTime = date + "/12";
                    dateTime2 = "12/" + date;
                    break;
            }

            for (int i = 0; i < arrayDanhSachHocSinh.size(); i++){
                arrayPhut.add(dataProvider.getInstance().GetLateMinuteFromMaHs(arrayDanhSachHocSinh.get(i).getMaHocSinh(),dateTime));
            }
            ArrayList<TrangThaiHocSinhNopTien> danhHocSinhNopTien = dataProvider.getInstance().LayDanhSachHocSinhNopTien(txtLop,dateTime2);
            for (int i = 0; i < arrayDanhSachHocSinh.size(); i++){
                ThongTinThongKe thongTinThongKe = new ThongTinThongKe("ABC","ABC",0,"0",0);
                thongTinThongKe.setMaHS(arrayDanhSachHocSinh.get(i).getMaHocSinh());
                thongTinThongKe.setHoTen(arrayDanhSachHocSinh.get(i).getHoTen());
                thongTinThongKe.setThoiGian(arrayPhut.get(i));
                int an = 0;
                for(int j = 0; j < danhHocSinhNopTien.size(); j++){
                    if (arrayDanhSachHocSinh.get(i).getMaHocSinh().toString().trim().equals(danhHocSinhNopTien.get(j).getMaHocSinh().trim()) && danhHocSinhNopTien.get(j).getTrangThaiThu().trim().equals("1")){
                        an = 1;
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
            //progressDialog.setMessage("Please wait...");
            //progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<ThongTinThongKe> thongTinThongKes) {
            totalLateTimeAdapter.notifyDataSetChanged();
            //progressDialog.dismiss();
        }
    }

    private class GetClassList extends AsyncTask<Void,Void,ArrayList<String>> {
        //ProgressDialog progressDialog;

        public GetClassList(Context mContext) {
            //progressDialog = new ProgressDialog(mContext);
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
            /*progressDialog.setMessage("Please wait...");
            progressDialog.show();*/
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            arrayAdapterClass = new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,strings);
            classList.setAdapter(arrayAdapterClass);
            txtLop = classList.getSelectedItem().toString().trim();
            //progressDialog.dismiss();
        }
    }
}
