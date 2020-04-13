package com.annguyen.truongmamnon.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.annguyen.truongmamnon.Activity.MainActivity;
import com.annguyen.truongmamnon.Activity.ManHinhDangNhapActivity;
import com.annguyen.truongmamnon.Controller.DataProvider;
import com.annguyen.truongmamnon.Controller.SharedPref;
import com.annguyen.truongmamnon.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentSupportPayment extends Fragment implements View.OnClickListener {
    private View view;
    private static String textMaGiaoVien = "";
    private static EditText uidPayment,nameParent, nameStudent, codeStudent, totalMoney, nameTeacher, codeTeacher,
                    reasonPayment;
    private static String uidDataSupportPayment = "123456";
    private static SharedPref sharedPref;
    private static Button confirm, getData;
    private Spinner monthPayment;
    private TextView classStudent;
    private SwipeRefreshLayout refreshLayout;
    String phoneNumber = "";
    DataProvider dataProvider;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_support_payment,container,false);
        sharedPref = new SharedPref(view.getContext());
        textMaGiaoVien = SharedPref.get(ManHinhDangNhapActivity.CURRENT_TEACHER,String.class);
        AnhXa();
        return view;
    }

    private void AnhXa() {
        monthPayment = view.findViewById(R.id.spinnerMonthPaymentSupportPaymentFragment);
        nameParent = view.findViewById(R.id.edtNameParentSupportPayment);
        nameStudent = view.findViewById(R.id.edtNameStudentSupportPayment);
        codeStudent = view.findViewById(R.id.edtCodeStudentSupportPayment);
        reasonPayment = view.findViewById(R.id.edtReasonSupportPaymentFragment);
        classStudent = view.findViewById(R.id.tvClassStudentSupportPaymentFragment);

        totalMoney = view.findViewById(R.id.edtTotalMoneySupportPayment);
        nameTeacher = view.findViewById(R.id.edtNameTeacherSupportPayment);
        codeTeacher = view.findViewById(R.id.edtCodeTeacherSupportPayment);
        confirm = view.findViewById(R.id.btnConfirmSupportFragment);

        uidPayment = view.findViewById(R.id.edtUidParentSupportPayment);
        refreshLayout = view.findViewById(R.id.pullToGetDataSupportPayment);

        view.findViewById(R.id.btnConfirmSupportFragment).setOnClickListener(this);

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
        ArrayAdapter arrayAdapterMonth = new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arraySpinnerMonth);
        monthPayment.setAdapter(arrayAdapterMonth);

        monthPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reasonPayment.setText("Thanh toán tiền học " + arraySpinnerMonth.get(position).trim());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
            uidDataSupportPayment = SharedPref.get(ManHinhDangNhapActivity.CURRENT_UID,String.class);
            uidPayment.setText(uidDataSupportPayment);
            GetMinuteLateFromDB getMinuteLateFromDB = new GetMinuteLateFromDB(view.getContext());
            getMinuteLateFromDB.execute();
        }else {
            Toast.makeText(view.getContext(),"Xin kiểm tra lại kết nối Internet",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfirmSupportFragment:
                if (MainActivity.kiemTraKetNoiInternet == true){
                    if (!uidPayment.getText().toString().trim().equals("") && !uidPayment.getText().toString().trim().equals("123456")){
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM");
                        String thoiGianKiemTra = dateFormat1.format(new Date());
                        ConfirmPayment confirmPayment = new ConfirmPayment(view.getContext());
                        confirmPayment.execute(thoiGianKiemTra);
                    /*Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SENDTO);
                    intent.putExtra("sms_body",reasonPayment.getText().toString().trim());
                    intent.setData(Uri.parse("sms:"+phoneNumber));
                    view.getContext().startActivity(intent);*/
                    }
                    else {
                        Toast.makeText(view.getContext(),"Bạn chưa lấy thông tin",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(view.getContext(),"Xin kiểm tra lại kết nối Internet!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    /*Lay tong cong so phut muon*/
    private class GetMinuteLateFromDB extends AsyncTask<Integer,Void,Integer> {
        //ProgressDialog progressDialog;

        public GetMinuteLateFromDB(Context mContext) {
            //progressDialog = new ProgressDialog(mContext);
        }
        @Override
        protected void onPreExecute() {
            //progressDialog.setMessage("Please wait...");
            //progressDialog.show();
        }
        @SuppressLint("WrongThread")
        @Override
        protected Integer doInBackground(Integer... integers) {
            String dateTime = "2020/";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM");
            String date = dateFormat.format(new Date());

            switch (monthPayment.getSelectedItemPosition()){
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
            if (date.trim().equals(dateTime.trim())){
                return dataProvider.getInstance().GetLateMinute(uidDataSupportPayment,dateTime);
            }else {
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == -1){
                Toast.makeText(view.getContext(),"Kiểm tra lại tháng",Toast.LENGTH_SHORT).show();
            }else {
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
                    }
                }
                totalMoney.setText(String.valueOf(integer*5000));
            }
            //progressDialog.dismiss();
        }
    }

    private class ConfirmPayment extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog;
        public ConfirmPayment(Context mContext){
            progressDialog = new ProgressDialog(mContext);
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            return dataProvider.getInstance().CheckConfirmPayment(codeStudent.getText().toString().trim(),strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("")){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String date = dateFormat.format(new Date());
                //ManHinhDangNhapActivity.databaseSQLite.QuerryData("UPDATE ThongTinNopTien SET Status = 1 WHERE MaHs = '"+codeStudent.getText().toString().trim()+"'");
                if (dataProvider.getInstance().InsertConfirmPayment(codeStudent.getText().toString().trim(),
                        codeTeacher.getText().toString().trim(),
                        date,totalMoney.getText().toString().trim(),"1",classStudent.getText().toString().trim()) == 1){
                    Toast.makeText(view.getContext(),"Xác nhận thành công",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(view.getContext(),"Xác nhận không thành công",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(view.getContext(),"Học sinh đã đóng tiền",Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }
}
