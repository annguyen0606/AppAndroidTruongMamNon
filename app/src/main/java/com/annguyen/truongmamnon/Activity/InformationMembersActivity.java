package com.annguyen.truongmamnon.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.annguyen.truongmamnon.Controller.DataProvider;
import com.annguyen.truongmamnon.Controller.SharedPref;
import com.annguyen.truongmamnon.Fragment.FragmentDanhSachChamThe;
import com.annguyen.truongmamnon.Fragment.FragmentDanhSachChuaChamThe;
import com.annguyen.truongmamnon.Model.ThongTinNguoiThan;
import com.annguyen.truongmamnon.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class InformationMembersActivity extends AppCompatActivity implements View.OnClickListener {
    /*Class se chua toan bo du lieu cua hoc sinh, giao vien.
    * */
    private CircleImageView circleImageViewParent, circleImageViewStudent, circleImageViewParent2,
            circleImageViewParent3, circleImageViewCheckParent, circleImageViewCheckParent2, circleImageViewCheckParent3;
    //Cac text view chua cac thong tin chi tiet
    private TextView tenHocSinh, tenNguoiThan, soDienThoai, diaChiHocSinh, diaChiNguoiThan, quanHeHocSinh,
            lopHocSinh, ngaySinhHocSinh, gioiTinhHocSinh, maHocSinh, thoiGianMuon;
    private Button confirmTakeStudent; //Button de xac nhan len he thong
    Intent intent; //Bien Intent
    DataProvider dataProvider; //Class de moc data tu database sql server
    String dateStr = ""; //Bien luu tru ngay
    String txtUid = "";
    ImageView backToMainActivity;

    boolean kiemTraKetNoiInternet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_members);
        getSupportActionBar();
        AnhXa();
        intent = this.getIntent();
        /*
        * Lay ma hoc sinh*/
        maHocSinh.setText(intent.getStringExtra("data").trim());
        txtUid = intent.getStringExtra("data2").trim();
        /*2 mang byte chua bytes hinh anh cua nguoi than va cua hoc sinh*/
        byte[] byteImageParent = new byte[0];
        byte[] byteImageParent2 = new byte[0];
        byte[] byteImageParent3 = new byte[0];
        byte[] byteImageStudent = new byte[0];

        final Animation animation = AnimationUtils.loadAnimation(this,R.anim.scale);
        /*Tim kiem thong tin hoc sinh tuong uong trong table ThongTinHocSinh SQLite theo ma so hoc sinh
        * Do nhung du lieu can thiet ra cac text view*/
        Cursor dataCongViec = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinHocSinh WHERE MaHs ='"+maHocSinh.getText().toString()+"'");
        while (dataCongViec.moveToNext()){
            tenHocSinh.setText(dataCongViec.getString(2));
            ngaySinhHocSinh.setText(dataCongViec.getString(3));
            lopHocSinh.setText(dataCongViec.getString(4));
            gioiTinhHocSinh.setText(dataCongViec.getString(5));
            diaChiHocSinh.setText(dataCongViec.getString(6));
            byteImageStudent = dataCongViec.getBlob(7);
        }
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(byteImageStudent,
                0, byteImageStudent.length);
        circleImageViewStudent.setImageBitmap(decodebitmap);
        /*Tim kiem thong tin nguoi than tuong uong trong table ThongTinNguoiThan SQLite theo UID Tag
         * Do nhung du lieu can thiet ra cac text view*/
        //Cursor dataNT = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinNguoiThan WHERE Uid ='"+intent.getStringExtra("data2").trim()+"'");
        Cursor dataNT = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinNguoiThan");
        System.out.println(dataNT.getCount());
        ArrayList<ThongTinNguoiThan> danhSachNguoiThan = new ArrayList<>();
        while (dataNT.moveToNext()){
            ThongTinNguoiThan thongTinNguoiThan = new ThongTinNguoiThan("ABC","ABC","ABC","ABC","ABC","ABC",new byte[0],"ABC");
            thongTinNguoiThan.setMaUID(dataNT.getString(1));
            thongTinNguoiThan.setHoTen(dataNT.getString(2));
            thongTinNguoiThan.setDiaChi(dataNT.getString(3));
            thongTinNguoiThan.setQuanHe(dataNT.getString(4));
            thongTinNguoiThan.setMaHocSinh(dataNT.getString(5));
            thongTinNguoiThan.setSoDienThoai(dataNT.getString(6));
            thongTinNguoiThan.setHinhAnh(dataNT.getBlob(7));
            thongTinNguoiThan.setLop(dataNT.getString(8));
            danhSachNguoiThan.add(thongTinNguoiThan);
            /*
            tenNguoiThan.setText(dataNT.getString(2));
            diaChiNguoiThan.setText(dataNT.getString(3));
            quanHeHocSinh.setText(dataNT.getString(4));
            soDienThoai.setText(dataNT.getString(6));
            byteImageParent = dataNT.getBlob(7);*/
        }
        ArrayList<ThongTinNguoiThan> danhSachNguoiThanLienQuan = new ArrayList<>();
        for (int i = 0; i < danhSachNguoiThan.size(); i++){
            if (maHocSinh.getText().toString().trim().equals(danhSachNguoiThan.get(i).getMaHocSinh().trim())){
                ThongTinNguoiThan thongTinNguoiThan = new ThongTinNguoiThan("ABC","ABC","ABC","ABC","ABC","ABC",new byte[0],"ABC");
                thongTinNguoiThan.setMaUID(danhSachNguoiThan.get(i).getMaUID());
                thongTinNguoiThan.setHoTen(danhSachNguoiThan.get(i).getHoTen());
                thongTinNguoiThan.setDiaChi(danhSachNguoiThan.get(i).getDiaChi());
                thongTinNguoiThan.setQuanHe(danhSachNguoiThan.get(i).getQuanHe());
                thongTinNguoiThan.setSoDienThoai(danhSachNguoiThan.get(i).getSoDienThoai());
                thongTinNguoiThan.setHinhAnh(danhSachNguoiThan.get(i).getHinhAnh());
                danhSachNguoiThanLienQuan.add(thongTinNguoiThan);
            }
        }

        for (int i = 0; i < danhSachNguoiThanLienQuan.size(); i++){
            switch (i){
                case 0:
                    byteImageParent = danhSachNguoiThanLienQuan.get(0).getHinhAnh();
                    if (txtUid.trim().equals(danhSachNguoiThanLienQuan.get(0).getMaUID())){
                        tenNguoiThan.setText(danhSachNguoiThanLienQuan.get(0).getHoTen());
                        diaChiNguoiThan.setText(danhSachNguoiThanLienQuan.get(0).getDiaChi());
                        quanHeHocSinh.setText(danhSachNguoiThanLienQuan.get(0).getQuanHe());
                        soDienThoai.setText(danhSachNguoiThanLienQuan.get(0).getSoDienThoai());

                        circleImageViewCheckParent2.setVisibility(View.INVISIBLE);
                        circleImageViewCheckParent3.setVisibility(View.INVISIBLE);
                        //circleImageViewParent.startAnimation(animation);
                    }
                    Bitmap decodebitmapnt = BitmapFactory.decodeByteArray(byteImageParent,
                            0, byteImageParent.length);
                    circleImageViewParent.setImageBitmap(decodebitmapnt);
                    break;
                case 1:
                    byteImageParent2 = danhSachNguoiThanLienQuan.get(1).getHinhAnh();
                    if (txtUid.trim().equals(danhSachNguoiThanLienQuan.get(1).getMaUID())){
                        tenNguoiThan.setText(danhSachNguoiThanLienQuan.get(1).getHoTen());
                        diaChiNguoiThan.setText(danhSachNguoiThanLienQuan.get(1).getDiaChi());
                        quanHeHocSinh.setText(danhSachNguoiThanLienQuan.get(1).getQuanHe());
                        soDienThoai.setText(danhSachNguoiThanLienQuan.get(1).getSoDienThoai());

                        circleImageViewCheckParent.setVisibility(View.INVISIBLE);
                        circleImageViewCheckParent3.setVisibility(View.INVISIBLE);
                        //circleImageViewParent2.startAnimation(animation);
                    }
                    decodebitmapnt = BitmapFactory.decodeByteArray(byteImageParent2,
                            0, byteImageParent2.length);
                    circleImageViewParent2.setImageBitmap(decodebitmapnt);
                    break;
                case 2:
                    byteImageParent3 = danhSachNguoiThanLienQuan.get(2).getHinhAnh();
                    if (txtUid.trim().equals(danhSachNguoiThanLienQuan.get(2).getMaUID())){
                        tenNguoiThan.setText(danhSachNguoiThanLienQuan.get(2).getHoTen());
                        diaChiNguoiThan.setText(danhSachNguoiThanLienQuan.get(2).getDiaChi());
                        quanHeHocSinh.setText(danhSachNguoiThanLienQuan.get(2).getQuanHe());
                        soDienThoai.setText(danhSachNguoiThanLienQuan.get(2).getSoDienThoai());

                        circleImageViewCheckParent2.setVisibility(View.INVISIBLE);
                        circleImageViewCheckParent.setVisibility(View.INVISIBLE);
                        //circleImageViewParent3.startAnimation(animation);
                    }
                    decodebitmapnt = BitmapFactory.decodeByteArray(byteImageParent3,
                            0, byteImageParent3.length);
                    circleImageViewParent3.setImageBitmap(decodebitmapnt);
                    break;
            }

        }
        //Lay thoi so phut don con muon
        Cursor thoigian = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM UIDTag WHERE Uid = '"+intent.getStringExtra("data2").trim()+"'");
        while (thoigian.moveToNext()){
            thoiGianMuon.setText(thoigian.getString(2));
        }
    }

    private void AnhXa() {
        /*Anh xa cac bien toi cac view tuong ung*/
        tenHocSinh = findViewById(R.id.txtTenHocSinhMembersActivity);
        tenNguoiThan = findViewById(R.id.txtTenNguoiThanMembersActivity);
        gioiTinhHocSinh = findViewById(R.id.txtGioiTinhHocSinhMembersActivity);
        diaChiHocSinh = findViewById(R.id.txtDiaChiHocSinhMembersActivity);
        diaChiNguoiThan = findViewById(R.id.txtDiaChiNguoiThanMembersActivity);
        quanHeHocSinh = findViewById(R.id.txtQuanHeHocSinhMembersActivity);
        soDienThoai = findViewById(R.id.txtDienThoaiNguoiThanMembersActivity);
        lopHocSinh = findViewById(R.id.txtLopHocSinhMembersActivity);
        ngaySinhHocSinh = findViewById(R.id.txtNgaySinhHocSinhMembersActivity);

        maHocSinh = findViewById(R.id.txtMaHocSinhMembersActivity);
        thoiGianMuon = findViewById(R.id.txtThoiGianDonConMuonMembersActivity);
        confirmTakeStudent = findViewById(R.id.btnConfirmImformationActivity);

        backToMainActivity = findViewById(R.id.backMainActivityAtInforActivity);

        circleImageViewParent = findViewById(R.id.circleViewParentFragmentInformation);
        circleImageViewStudent = findViewById(R.id.circleViewStudentFragmentInformation);
        circleImageViewParent2 = findViewById(R.id.circleViewParent2FragmentInformation);
        circleImageViewParent3 = findViewById(R.id.circleViewParent3FragmentInformation);
        circleImageViewCheckParent = findViewById(R.id.circleViewCheckParentFragmentInformation);
        circleImageViewCheckParent2 = findViewById(R.id.circleViewCheck2ParentFragmentInformation);
        circleImageViewCheckParent3 = findViewById(R.id.circleViewCheck3ParentFragmentInformation);
        //Khoi tao su kien cho button
        findViewById(R.id.btnConfirmImformationActivity).setOnClickListener(this);
        findViewById(R.id.backMainActivityAtInforActivity).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*Bat su kien khi click vao button*/
            case R.id.btnConfirmImformationActivity:
                if (kiemTraKetNoiInternet == true){
                    int checkInsert = 0;
                    InsertIntoDataBase insertIntoDataBase = new InsertIntoDataBase(InformationMembersActivity.this);
                    insertIntoDataBase.execute(checkInsert);
                }else {
                    Toast.makeText(InformationMembersActivity.this,"Xin kiểm tra lại Internet!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.backMainActivityAtInforActivity:
                //startActivity(new Intent(InformationMembersActivity.this,MainActivity.class));
                onBackPressed();
                break;
        }
    }
    /*Class de insert du lieu vao Database sql server khi nguoi dung bam xac nhan*/
    private class InsertIntoDataBase extends AsyncTask<Integer,Void,Integer> {
        ProgressDialog progressDialog;
        public InsertIntoDataBase(Context mContext) {
            progressDialog = new ProgressDialog(mContext, AlertDialog.THEME_TRADITIONAL);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Integer doInBackground(Integer... integers) {
            //Lay ngay gio
            SimpleDateFormat Date = new SimpleDateFormat("yyyy/MM/dd");
            dateStr = Date.format(new Date());
            /*
            * Kiem tra xem dia chi UID nay da don con chua
            * Neu chua con con thi insert va tra ve gia tri 1
            * Neu da don con thi khong lam gi ca va tra ve gia tri 0
            * => Tuy can thay doi sang truy xuat theo ma hoc sinh chu khong phai theo ma phu huynh*/
            String uidCheckConfirm = dataProvider.getInstance().CheckConfirmTakeStudent(intent.getStringExtra("data").trim(),dateStr);
            if (uidCheckConfirm.trim().equals("")){
                dataProvider.getInstance().InsertThoiGianDonCon(intent.getStringExtra("data2").trim(),
                        maHocSinh.getText().toString().trim(),lopHocSinh.getText().toString().trim(),dateStr,
                        Integer.parseInt(thoiGianMuon.getText().toString()));
                return 1;
            }else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            ManHinhDangNhapActivity.databaseSQLite.QuerryData("UPDATE UIDTag SET Confirm = 1 WHERE Uid = '"+intent.getStringExtra("data2").trim()+"'");
            if (integer == 1){
                Toast.makeText(InformationMembersActivity.this,"Xác nhận thành công",Toast.LENGTH_SHORT).show();
                Drawable d = getResources().getDrawable(R.drawable.background_button_choose);
                confirmTakeStudent.setBackground(d);
            }else {
                Drawable d = getResources().getDrawable(R.drawable.background_button_choose);
                confirmTakeStudent.setBackground(d);
                Toast.makeText(InformationMembersActivity.this,"Bạn đã xác nhận rồi",Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    int countDontTakeStudent = FragmentDanhSachChamThe.LoadDuLieu();
                    FragmentDanhSachChuaChamThe.LoadDuLieuChuaChamThe();
                    if (countDontTakeStudent > 0){
                        SharedPref.put(ManHinhDangNhapActivity.CURRENT_STUDENTS_COUNT,countDontTakeStudent);
                        MainActivity.tabLayout.getTabAt(0).getOrCreateBadge().setVisible(true);
                        MainActivity.tabLayout.getTabAt(0).getOrCreateBadge().setNumber(countDontTakeStudent);
                        MainActivity.tabLayout.getTabAt(0).getOrCreateBadge().setMaxCharacterCount(3);
                    }else {
                        MainActivity.tabLayout.getTabAt(0).getOrCreateBadge().setVisible(false);
                    }
                }
            });
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
                    Toast.makeText(InformationMembersActivity.this,"Lỗi Intertnet, Xin kiểm tra lại!",Toast.LENGTH_SHORT).show();
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
