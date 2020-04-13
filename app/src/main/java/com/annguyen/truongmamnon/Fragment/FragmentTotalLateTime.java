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
import com.annguyen.truongmamnon.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_total_late_time,container,false);
        sharedPref = new SharedPref(view.getContext());
        AnhXa();
        return view;
    }

    private void AnhXa() {
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
                    //Toast.makeText(view.getContext(),txtLop,Toast.LENGTH_SHORT).show();
                    GetMinuteLateFromDB getMinuteLateFromDB = new GetMinuteLateFromDB(view.getContext());
                    getMinuteLateFromDB.execute(txtLop);
                    //Toast.makeText(view.getContext(),arrayThongTinThongKe.get(1).getUid().toString().trim(),Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    arrayDanhSachHocSinh = new ArrayList<>();
                    arrayDanhSachHocSinh = dataProvider.getInstance().LayDanhSachThongTinHocSinh("SELECT *FROM ThongTinHocSinh WHERE lop ='"+txtLop+"'");
                    GetMinuteLateFromDB2 getMinuteLateFromDB2 = new GetMinuteLateFromDB2(view.getContext());
                    getMinuteLateFromDB2.execute(txtLop);
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
                    data.append("Mã học sinh,Tên học sinh,Số phút,Số tiền,Trạng thái nộp tiền");

                    if (arrayThongTinThongKe.size() > 0){
                        for (int i = 0; i < arrayThongTinThongKe.size(); i++){
                            if (arrayThongTinThongKe.get(i).getStatusPayment() == 0){
                                data.append("\n"+String.valueOf(arrayThongTinThongKe.get(i).getMaHS())+","+String.valueOf(arrayThongTinThongKe.get(i).getHoTen())+","
                                        +String.valueOf(arrayThongTinThongKe.get(i).getThoiGian())+","+String.valueOf(arrayThongTinThongKe.get(i).getMoney())+",Chưa nộp tiền");
                            }else if (arrayThongTinThongKe.get(i).getStatusPayment() == 1){
                                data.append("\n"+String.valueOf(arrayThongTinThongKe.get(i).getMaHS())+","+String.valueOf(arrayThongTinThongKe.get(i).getHoTen())+","
                                        +String.valueOf(arrayThongTinThongKe.get(i).getThoiGian())+","+String.valueOf(arrayThongTinThongKe.get(i).getMoney())+",Đã nộp tiền");
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

    private class GetMinuteLateFromDB extends AsyncTask<String,Void,ArrayList<ThongTinThongKe>> {
        //ProgressDialog progressDialog;

        public GetMinuteLateFromDB(Context mContext) {
//            progressDialog = new ProgressDialog(mContext);
        }

        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<ThongTinThongKe> doInBackground(String... strings) {
            String dateTime = "2020/";
            ArrayList<String> arrayMaHs = new ArrayList<>();
            ArrayList<String> arrayName = new ArrayList<>();
            ArrayList<Integer> arrayPhut = new ArrayList<>();
            ArrayList<String> arrayMoney = new ArrayList<>();
            //Dau tien la lay ma hoc sinh va ten hoc sinh trong table ThongTinHocSinh SQLite
            Cursor dataHs = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinHocSinh WHERE Lop = '"+strings[0] +"'");
            while (dataHs.moveToNext()){
                arrayMaHs.add(dataHs.getString(1));
                arrayName.add(dataHs.getString(2));
            }
            //Lay thoi gian de thuc hien cau lenh truy van len SQL Server
            switch (monthList.getSelectedItemPosition()){
                case 0:
                    dateTime += "01";
                    break;
                case 1:
                    dateTime += "02";
                    break;
                case 2:
                    dateTime += "03";
                    break;
                case 3:
                    dateTime += "04";
                    break;
                case 4:
                    dateTime += "05";
                    break;
                case 5:
                    dateTime += "06";
                    break;
                case 6:
                    dateTime += "07";
                    break;
                case 7:
                    dateTime += "08";
                    break;
                case 8:
                    dateTime += "09";
                    break;
                case 9:
                    dateTime += "10";
                    break;
                case 10:
                    dateTime += "11";
                    break;
                case 11:
                    dateTime += "12";
                    break;
            }
            //Thong ke so phut theo ma hoc sinh
            for (String str : arrayMaHs){
                arrayPhut.add(dataProvider.getInstance().GetLateMinuteFromMaHs(str,dateTime));
            }
            //Tinh tien
            for (int i = 0; i < arrayPhut.size();i++){
                String abc = String.valueOf(arrayPhut.get(i)*+5000);
                arrayMoney.add(abc);
            }
            /*Lay danh sach hoc sinh nop tien*/
            ArrayList<HocSinhNopTien> danhHocSinhNopTien = dataProvider.getInstance().LayDanhSachHocSinhNopTien(txtLop,dateTime);
            /*
            * Hien tai toi dang co 2 truong du lieu
            * Can thong tin trang thai nop tien va ma hoc sinh*/
            for (int i = 0; i < arrayMoney.size(); i++){
                ThongTinThongKe thongTinThongKe = new ThongTinThongKe("ABC","ABC",0,"ABC",0);
                thongTinThongKe.setMaHS(arrayMaHs.get(i));
                thongTinThongKe.setHoTen(arrayName.get(i));
                thongTinThongKe.setThoiGian(arrayPhut.get(i));
                thongTinThongKe.setMoney(arrayMoney.get(i));
                int checkPayment = 0;
                for(int j = 0; j < danhHocSinhNopTien.size(); j++){
                    if (arrayMaHs.get(i).toString().trim().equals(danhHocSinhNopTien.get(j).getMaHocSinh().trim())){
                        checkPayment = 1;
                    }
                }
                thongTinThongKe.setStatusPayment(checkPayment);
                arrayThongTinThongKe.add(thongTinThongKe);
            }
            return arrayThongTinThongKe;
        }

        @Override
        protected void onPreExecute() {
            /*progressDialog.setMessage("Please wait...");
            progressDialog.show();*/
        }

        @Override
        protected void onPostExecute(ArrayList<ThongTinThongKe> thongTinThongKes) {
            totalLateTimeAdapter.notifyDataSetChanged();
            //progressDialog.dismiss();
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
            String dateTime = "2020/";
            arrayPhut = new ArrayList<>();
            arrayMoney = new ArrayList<>();

            switch (monthList.getSelectedItemPosition()){
                case 0:
                    dateTime += "01";
                    break;
                case 1:
                    dateTime += "02";
                    break;
                case 2:
                    dateTime += "03";
                    break;
                case 3:
                    dateTime += "04";
                    break;
                case 4:
                    dateTime += "05";
                    break;
                case 5:
                    dateTime += "06";
                    break;
                case 6:
                    dateTime += "07";
                    break;
                case 7:
                    dateTime += "08";
                    break;
                case 8:
                    dateTime += "09";
                    break;
                case 9:
                    dateTime += "10";
                    break;
                case 10:
                    dateTime += "11";
                    break;
                case 11:
                    dateTime += "12";
                    break;
            }

            for (int i = 0; i < arrayDanhSachHocSinh.size(); i++){
                arrayPhut.add(dataProvider.getInstance().GetLateMinuteFromMaHs(arrayDanhSachHocSinh.get(i).getMaHocSinh(),dateTime));
            }
            for (int i = 0; i < arrayPhut.size();i++){
                String abc = String.valueOf(arrayPhut.get(i)*+5000);
                arrayMoney.add(abc);
            }
            ArrayList<HocSinhNopTien> danhHocSinhNopTien = dataProvider.getInstance().LayDanhSachHocSinhNopTien(txtLop,dateTime);
            for (int i = 0; i < arrayMoney.size(); i++){
                ThongTinThongKe thongTinThongKe = new ThongTinThongKe("ABC","ABC",0,"ABC",0);
                thongTinThongKe.setMaHS(arrayDanhSachHocSinh.get(i).getMaHocSinh());
                thongTinThongKe.setHoTen(arrayDanhSachHocSinh.get(i).getHoTen());
                thongTinThongKe.setThoiGian(arrayPhut.get(i));
                thongTinThongKe.setMoney(arrayMoney.get(i));
                int an = 0;
                for(int j = 0; j < danhHocSinhNopTien.size(); j++){
                    if (arrayDanhSachHocSinh.get(i).getMaHocSinh().toString().trim().equals(danhHocSinhNopTien.get(j).getMaHocSinh().trim())){
                        an = 1;
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
