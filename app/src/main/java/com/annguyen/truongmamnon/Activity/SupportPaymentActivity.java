package com.annguyen.truongmamnon.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.annguyen.truongmamnon.Controller.DataProvider;
import com.annguyen.truongmamnon.Controller.SharedPref;
import com.annguyen.truongmamnon.Model.TrangThaiHocSinhNopTien;
import com.annguyen.truongmamnon.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SupportPaymentActivity extends AppCompatActivity implements View.OnClickListener {
    private static String textMaGiaoVien = "";
    private static EditText uidPayment,nameParent, nameStudent, codeStudent, totalMoney, nameTeacher, codeTeacher,
                    reasonPayment, statusPayment;
    private static String uidDataSupportPayment = "123456";
    private static SharedPref sharedPref;
    private static Button confirm, getData;
    private Spinner monthPayment;
    private TextView classStudent;
    private SwipeRefreshLayout refreshLayout;
    String phoneNumber = "";
    DataProvider dataProvider;
    ImageView backMainActivity;
    CircleImageView circleImageStudent;
    boolean kiemTraNopTien = false;
    byte[] byteImageStudent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_payment);
        sharedPref = new SharedPref(SupportPaymentActivity.this);
        byteImageStudent = new byte[0];
        textMaGiaoVien = SharedPref.get(ManHinhDangNhapActivity.CURRENT_TEACHER,String.class);
        AnhXa();
    }

    private void AnhXa() {
        monthPayment = findViewById(R.id.spinnerMonthPaymentSupportPaymentFragment);
        nameParent = findViewById(R.id.edtNameParentSupportPayment);
        nameStudent = findViewById(R.id.edtNameStudentSupportPayment);
        codeStudent = findViewById(R.id.edtCodeStudentSupportPayment);
        reasonPayment = findViewById(R.id.edtReasonSupportPaymentFragment);
        classStudent = findViewById(R.id.tvClassStudentSupportPaymentFragment);

        totalMoney = findViewById(R.id.edtTotalMoneySupportPayment);
        nameTeacher = findViewById(R.id.edtNameTeacherSupportPayment);
        codeTeacher = findViewById(R.id.edtCodeTeacherSupportPayment);
        statusPayment = findViewById(R.id.edtTrangThaiNopTienSupportPaymentFragment);
        backMainActivity = findViewById(R.id.backMainActivityAtSupportPaymentActivity);
        circleImageStudent = findViewById(R.id.circleHinhAnhHocSinhSupportPaymentActivity);
        confirm = findViewById(R.id.btnConfirmSupportFragment);

        uidPayment = findViewById(R.id.edtUidParentSupportPayment);
        refreshLayout = findViewById(R.id.pullToGetDataSupportPayment);

        findViewById(R.id.btnConfirmSupportFragment).setOnClickListener(this);
        findViewById(R.id.backMainActivityAtSupportPaymentActivity).setOnClickListener(this);

        final ArrayList<String> arraySpinnerMonth = new ArrayList<>();
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
        ArrayAdapter arrayAdapterMonth = new ArrayAdapter(SupportPaymentActivity.this,android.R.layout.simple_list_item_1,arraySpinnerMonth);
        monthPayment.setAdapter(arrayAdapterMonth);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshGetData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },100);
            }
        });
    }
    void RefreshGetData(){
        if (MainActivity.kiemTraKetNoiInternet == true){
            ClearDuLieu();
            uidDataSupportPayment = SharedPref.get(ManHinhDangNhapActivity.CURRENT_UID,String.class);
            uidPayment.setText(uidDataSupportPayment);
            LayThongTinTaiKhoan layThongTinTaiKhoan = new LayThongTinTaiKhoan();
            layThongTinTaiKhoan.execute();
        }else {
            Toast.makeText(SupportPaymentActivity.this,"Xin kiểm tra lại kết nối Internet",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backMainActivityAtSupportPaymentActivity:
                onBackPressed();
                break;
            case R.id.btnConfirmSupportFragment:
                if (MainActivity.kiemTraKetNoiInternet == true){
                    if (!uidPayment.getText().toString().trim().equals("") && !uidPayment.getText().toString().trim().equals("123456")){
                        if (kiemTraNopTien == true){
                            ConfirmPayment confirmPayment = new ConfirmPayment(SupportPaymentActivity.this);
                            confirmPayment.execute();
                        }else {
                            Toast.makeText(SupportPaymentActivity.this,"Không được thu tiền tài khoản này",Toast.LENGTH_SHORT).show();
                        }
                        /*ConfirmPayment confirmPayment = new ConfirmPayment(view.getContext());
                        confirmPayment.execute(thoiGianKiemTra);*/
                    /*Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SENDTO);
                    intent.putExtra("sms_body",reasonPayment.getText().toString().trim());
                    intent.setData(Uri.parse("sms:"+phoneNumber));
                    view.getContext().startActivity(intent);*/
                    }
                    else {
                        Toast.makeText(SupportPaymentActivity.this,"Bạn chưa lấy thông tin",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SupportPaymentActivity.this,"Xin kiểm tra lại kết nối Internet!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    /*Lay tong cong so phut muon*/
    private class LayThongTinTaiKhoan extends AsyncTask<Integer,Void,ArrayList<TrangThaiHocSinhNopTien>> {
        public LayThongTinTaiKhoan() {
        }
        @Override
        protected void onPreExecute() {
        }
        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<TrangThaiHocSinhNopTien> doInBackground(Integer... integers) {
            String dateTime = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            String date = dateFormat.format(new Date());

            switch (monthPayment.getSelectedItemPosition()){
                case 0:
                    dateTime = "01/" + date;
                    break;
                case 1:
                    dateTime = "02/" + date;
                    break;
                case 2:
                    dateTime = "03/" + date;
                    break;
                case 3:
                    dateTime = "04/" + date;
                    break;
                case 4:
                    dateTime = "05/" + date;
                    break;
                case 5:
                    dateTime = "06/" + date;
                    break;
                case 6:
                    dateTime = "07/" + date;
                    break;
                case 7:
                    dateTime = "08/" + date;
                    break;
                case 8:
                    dateTime = "09/" + date;
                    break;
                case 9:
                    dateTime = "10/" + date;
                    break;
                case 10:
                    dateTime = "11/" + date;
                    break;
                case 11:
                    dateTime = "12/" + date;
                    break;
            }
            return dataProvider.getInstance().CheckConfirmPayment(codeStudent.getText().toString().trim(),dateTime);
        }

        @Override
        protected void onPostExecute(ArrayList<TrangThaiHocSinhNopTien> trangThaiHocSinhNopTiens) {
            reasonPayment.setText("Thanh toán tiền học " + monthPayment.getSelectedItem().toString().trim());
            if (!uidDataSupportPayment.equals("")){
                Cursor DataTeacher = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinGiaoVien WHERE MaGV = '"+textMaGiaoVien+"'");
                while (DataTeacher.moveToNext()){
                    nameTeacher.setText(DataTeacher.getString(2));
                    codeTeacher.setText(DataTeacher.getString(1));
                }
                Cursor DataNT = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinNguoiThan WHERE Uid = '"+uidPayment.getText().toString().trim()+"'");
                while (DataNT.moveToNext()){
                    nameParent.setText(DataNT.getString(2));
                    codeStudent.setText(DataNT.getString(5));
                    phoneNumber = DataNT.getString(6);
                }

                Cursor DataHS = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinHocSinh WHERE MaHs = '"+codeStudent.getText().toString().trim()+"'");
                while (DataHS.moveToNext()){
                    nameStudent.setText(DataHS.getString(2));
                    classStudent.setText(DataHS.getString(4));
                    byteImageStudent = DataHS.getBlob(7);
                }
            }
            Bitmap decodebitmap = BitmapFactory.decodeByteArray(byteImageStudent,
                    0, byteImageStudent.length);
            circleImageStudent.setImageBitmap(decodebitmap);
            if (trangThaiHocSinhNopTiens.size() > 0){
                totalMoney.setText(trangThaiHocSinhNopTiens.get(0).getSoTien());
                if (trangThaiHocSinhNopTiens.get(0).getTrangThaiThu().toString().trim().equals("0")){
                    kiemTraNopTien = true;
                    statusPayment.setText("Chưa nộp tiền");
                }else if (trangThaiHocSinhNopTiens.get(0).getTrangThaiThu().toString().trim().equals("1")){
                    statusPayment.setText("Đã nộp tiền");
                    kiemTraNopTien = false;
                }

            }else {
                Toast.makeText(SupportPaymentActivity.this,"Không có dữ liệu thanh toán",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ConfirmPayment extends AsyncTask<Void,Void,Integer>{
        ProgressDialog progressDialog;
        public ConfirmPayment(Context mContext){
            progressDialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_DARK);
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Confirming...");
            progressDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Integer doInBackground(Void... voids) {
            String dateTime = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            String date = dateFormat.format(new Date());

            switch (monthPayment.getSelectedItemPosition()){
                case 0:
                    dateTime = "01/" + date;
                    break;
                case 1:
                    dateTime = "02/" + date;
                    break;
                case 2:
                    dateTime = "03/" + date;
                    break;
                case 3:
                    dateTime = "04/" + date;
                    break;
                case 4:
                    dateTime = "05/" + date;
                    break;
                case 5:
                    dateTime = "06/" + date;
                    break;
                case 6:
                    dateTime = "07/" + date;
                    break;
                case 7:
                    dateTime = "08/" + date;
                    break;
                case 8:
                    dateTime = "09/" + date;
                    break;
                case 9:
                    dateTime = "10/" + date;
                    break;
                case 10:
                    dateTime = "11/" + date;
                    break;
                case 11:
                    dateTime = "12/" + date;
                    break;
            }
            return dataProvider.getInstance().InsertConfirmPayment(codeStudent.getText().toString().trim(),dateTime);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1){
                Toast.makeText(SupportPaymentActivity.this,"Xác nhận thành công",Toast.LENGTH_SHORT).show();
                kiemTraNopTien = false;
            }else {
                Toast.makeText(SupportPaymentActivity.this,"Xác nhận không thành công",Toast.LENGTH_SHORT).show();
            }
            ClearDuLieu();
            progressDialog.dismiss();
        }
    }

    void ClearDuLieu(){
        uidPayment.setText("");
        nameParent.setText("");
        nameTeacher.setText("");
        nameStudent.setText("");
        codeStudent.setText("");
        codeTeacher.setText("");
        classStudent.setText("");
        totalMoney.setText("");
        reasonPayment.setText("");
        statusPayment.setText("");
    }
}
