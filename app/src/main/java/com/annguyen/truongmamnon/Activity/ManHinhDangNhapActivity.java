package com.annguyen.truongmamnon.Activity;

import androidx.appcompat.app.AppCompatActivity;

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
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.annguyen.truongmamnon.Controller.DataProvider;
import com.annguyen.truongmamnon.Controller.DatabaseSQLite;
import com.annguyen.truongmamnon.Controller.SharedPref;
import com.annguyen.truongmamnon.Model.ThongTinGiaoVien;
import com.annguyen.truongmamnon.Model.ThongTinHocSinh;
import com.annguyen.truongmamnon.Model.ThongTinNguoiThan;
import com.annguyen.truongmamnon.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManHinhDangNhapActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String CURRENT_PASS = "current_pass"; //Luu pass word
    public static final String CURRENT_NAME = "current_username"; //Luu user name
    public static final String CURRENT_CHECK = "current_check"; //Luu trang thai co muon luu username va password
    public static final String CURRENT_Class = "current_class"; //Luu ten lop nguoi giao vien
    public static final String CURRENT_TEACHER = "current_teacher"; //Luu ma so giao vien
    public static final String CURRENT_PORT = "current_port"; //Luu PORT ket noi
    public static final String CURRENT_DATE = "current_date"; //Luu ngay de phuc vu clear data theo ngay
    public static final String CURRENT_ACCOUNT = "current_accoung";
    public static final String CURRENT_UID = "current_uid";
    public static final String CURRENT_LOGIN_STATUS = "current_login"; //Luu trang thai dang nhap
    public static final String CURRENT_STUDENTS_COUNT = "current_student_count";

    private EditText userName, passWord; //Edit text luu username va password
    private Button loginSystem; //Button login
    private CheckBox saveDataPref; //Check box cho phep luu thong tin dang nhap va hien thi mat khau dang nhap
    private static SharedPref sharedPref; //Class luu mot vai thong tin nhu voi SharedPreferences
    private Context context; // bien context

    public static ThongTinGiaoVien thongTinGiaoVien; //Class chua data cua giao vien dang nhap sau khi moc data from database
    DataProvider dataProvider; //Class de moc data from database
    private ArrayList<ThongTinHocSinh> arrayDanhSachHocSinh; //Array chua danh sach cac hoc sinh thuoc lop cua giao vien quan ly
    private ArrayList<ThongTinNguoiThan> arrayDanhSachNguoiThan; //Array chua danh sach cac nguoi than cua hoc sinh thuoc lop cua giao vien ly
    public static DatabaseSQLite databaseSQLite; //Class cho phep giao tiep voi sqlite

    boolean kiemTraKetNoiInternet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_dang_nhap);
        context = this;
    }

    private void init() {
        //Khoi tao SQLit
        databaseSQLite = new DatabaseSQLite(this,"ConekKindergaten.sqlite",null,1);
        sharedPref = new SharedPref(context); //Khoi tao SharedPreferences
        /*Kiem tra ket noi voi database
          - Neu khong ket noi duoc voi database thi thong bao loi Internet
          - Neu ket noi thanh cong thi cho phep khoi tao database trong sqlite
        */
/*        if (dataProvider.getInstance().CONN("coneknfc","Conek@123","NFC","125.212.201.52") == null){
            Toast.makeText(ManHinhDangNhapActivity.this,"Lỗi Internet, Xin kiểm tra lại",Toast.LENGTH_SHORT).show();
        }else {
            *//*Khi ket noi thanh cong toi database
            * Neu trang thai dang nhap van duoc duy tri thi vao man hinh dang nhap luon
            * Neu trang thai dang nhap da bi huy thi se khoi tao database trong sqlite
            * *//*
        }*/
        if (SharedPref.get(CURRENT_LOGIN_STATUS,Boolean.class) == false){
            //Khoi ta sqlite
            try{
                //ThongTinNguoiThan chua thong tin nguoi than
                databaseSQLite.QuerryData("CREATE TABLE IF NOT EXISTS ThongTinNguoiThan(Id INTEGER PRIMARY KEY AUTOINCREMENT,Uid VARCHAR(20)," +
                        "HoTen VARCHAR(50),DiaChi VARCHAR(50),quanhe VARCHAR(50),MaHs VARCHAR(20),SoDienThoai VARCHAR(12),HinhAnh BLOB,Lop VARCHAR(10))");
                //ThongTinHocSinh chua thong tin hoc sinh
                databaseSQLite.QuerryData("CREATE TABLE IF NOT EXISTS ThongTinHocSinh(Id INTEGER PRIMARY KEY AUTOINCREMENT,MaHs VARCHAR(20)," +
                        "HoTen VARCHAR(50),NgaySinh VARCHAR(12),Lop VARCHAR(10),GioiTinh VARCHAR(10),DiaChi VARCHAR(50),HinhAnh BLOB,MaGV VARCHAR(10))");
                //ThongTinGiaoVien chua thong tin giao vien
                databaseSQLite.QuerryData("CREATE TABLE IF NOT EXISTS ThongTinGiaoVien(Id INTEGER PRIMARY KEY AUTOINCREMENT,MaGV VARCHAR(10)," +
                        "HoTen VARCHAR(50),DiaChi VARCHAR(50),SoDienThoai VARCHAR(11),Lop VARCHAR(10),LoaiTaiKhoan INT)");
                //UIDTag chua thong tin don con muon
                databaseSQLite.QuerryData("CREATE TABLE IF NOT EXISTS UIDTag(Id INTEGER PRIMARY KEY AUTOINCREMENT,Uid VARCHAR(20),ThoiGian VARCHAR(20),Confirm INT)");
                //ThongTinNopTien
                databaseSQLite.QuerryData("CREATE TABLE IF NOT EXISTS ThongTinNopTien(Id INTEGER PRIMARY KEY AUTOINCREMENT,MaHs VARCHAR(10)," +
                        "MaLop VARCHAR(10),Status INTEGER)");
                //Bang kiem tra don hoc sinh
                databaseSQLite.QuerryData("CREATE TABLE IF NOT EXISTS KiemTraDonCon(Id INTEGER PRIMARY KEY AUTOINCREMENT,Uid VARCHAR(20),MaHs VARCHAR(10))");
            }catch (Exception ex){
                ex.printStackTrace();
            }
            AnhXa();
        }else {
            startActivity(new Intent(ManHinhDangNhapActivity.this,MainActivity.class));
            finish();
        }
    }
    private void AnhXa() {
        /*
        * Anh xa cac bien khoi tao voi cac views*/
        userName = findViewById(R.id.edtUserNameManHinhDangNhap);
        passWord = findViewById(R.id.edtPasswordManHinhDangNhap);
        loginSystem = findViewById(R.id.btnLoginSystemManHinhDangNhap);
        saveDataPref = findViewById(R.id.chbNhoThongTinDangNhapManHinhDangNhap);
        /*Lay username va password va checkbox save data da luu*/
        userName.setText(SharedPref.get(CURRENT_NAME,String.class));
        passWord.setText(SharedPref.get(CURRENT_PASS,String.class));
        saveDataPref.setChecked(SharedPref.get(CURRENT_CHECK,Boolean.class));
        /*Khoi tao bat su kien khi click vao nut dang nhap*/
        findViewById(R.id.btnLoginSystemManHinhDangNhap).setOnClickListener(this);
        /*
        * Bat su kien khi nguoi dung checkbox vao show pass*/
    }
    /*Luu thong tin username va password*/
    void CheckSave(){
        if (saveDataPref.isChecked()){
            Toast.makeText(ManHinhDangNhapActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
            SharedPref.put(CURRENT_PASS,passWord.getText().toString());
            SharedPref.put(CURRENT_NAME,userName.getText().toString());
            SharedPref.put(CURRENT_CHECK,true);
        }else {
            SharedPref.put(CURRENT_CHECK,false);
            SharedPref.clear();
        }
    }
    /*
    * Bat su kien khi click vao button login*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLoginSystemManHinhDangNhap:
                if (kiemTraKetNoiInternet == true){
                    LoadDataFromDataBase loadDataFromDataBase = new LoadDataFromDataBase(context);
                    loadDataFromDataBase.execute("SELECT *FROM ThongTinGiaoVien WHERE username ='"+userName.getText().toString().trim()+"'");
                }else {
                    Toast.makeText(ManHinhDangNhapActivity.this,"Mất kết nối Internet, Xin kiểm tra lại!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    /*
    * Class load du lieu tu database sql server ve*/
    private class LoadDataFromDataBase extends AsyncTask<String,Void,ThongTinGiaoVien>{
        ProgressDialog progressDialog;

        public LoadDataFromDataBase(Context mContext) {
            progressDialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_DARK);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Đang tải dữ liệu");
            progressDialog.show();
        }

        @Override
        protected ThongTinGiaoVien doInBackground(String... strings) {
            return dataProvider.getInstance().LayThongTinGiaoVien(strings[0]);
        }

        @Override
        protected void onPostExecute(ThongTinGiaoVien thongTinGiaoVien) {
            //Tim kiem tren DB user name neu co thi lay ve toan bo thong tin dang nhap ve
            //Neu khong co thi thong bao tai khoan chua duoc dang ky
            thongTinGiaoVien = dataProvider.getInstance().LayThongTinGiaoVien("SELECT *FROM ThongTinGiaoVien WHERE username ='"+userName.getText().toString().trim()+"'");
            if (thongTinGiaoVien.getUserName().trim().equals(userName.getText().toString().trim()) && thongTinGiaoVien.getPassWord().trim().equals(passWord.getText().toString().trim())){
                //Nhap dung user name va pass word
                //Kiem tra xem tai khoan nay da dang nhap lan nao tren thiet bi mobile nay chua
                Cursor dataGiaoVienLogin = databaseSQLite.GetData("SELECT *FROM ThongTinGiaoVien WHERE MaGV = '"+thongTinGiaoVien.getMaGiaoVien().trim()+"'");
                //Toast.makeText(ManHinhDangNhapActivity.this,String.valueOf(thongTinGiaoVien.getLoaiTaiKhoan()),Toast.LENGTH_SHORT).show();
                //Luu mot vai gia tri hay dung nhu Lop, Ma so giao vien, Ten cong ket noi voi thiet bi
                SharedPref.put(CURRENT_Class,thongTinGiaoVien.getLop().trim());
                SharedPref.put(CURRENT_TEACHER,thongTinGiaoVien.getMaGiaoVien().trim());
                SharedPref.put(CURRENT_ACCOUNT,thongTinGiaoVien.getLoaiTaiKhoan());
                SharedPref.put(CURRENT_PORT,thongTinGiaoVien.getPortName().trim());
                if (dataGiaoVienLogin.getCount() < 1){
                    databaseSQLite.QuerryData("DELETE FROM ThongTinHocSinh");
                    databaseSQLite.QuerryData("DELETE FROM ThongTinNguoiThan");
                    databaseSQLite.QuerryData("DELETE FROM ThongTinGiaoVien");
                    databaseSQLite.QuerryData("VACUUM");
                    //Neu tai khoan chua dang nhap lan nao thi Luu toan bo thong tin cua nguoi dang nhap vao table ThongTinGiaoVien
                    databaseSQLite.InsertThongTinGiaoVien(thongTinGiaoVien.getMaGiaoVien().trim(),thongTinGiaoVien.getTenGiaoVien().trim(),thongTinGiaoVien.getDiaChi().trim(),
                            thongTinGiaoVien.getSoDienThoai().trim(),thongTinGiaoVien.getLop().trim(),thongTinGiaoVien.getLoaiTaiKhoan());
                    //databaseSQLite.InsertAccountLogin(thongTinGiaoVien.getMaGiaoVien(),1);
                    arrayDanhSachHocSinh = new ArrayList<>();
                    arrayDanhSachNguoiThan = new ArrayList<>();
                    //Lay du lieu tren DB roi luu vao SQLITE
                    arrayDanhSachHocSinh = dataProvider.getInstance().LayDanhSachThongTinHocSinh("SELECT *FROM ThongTinHocSinh WHERE malop ='"+thongTinGiaoVien.getLop().trim()+"'");
                    arrayDanhSachNguoiThan = dataProvider.getInstance().LayDanhSachThongTinNguoiThan("SELECT *FROM ThongTinNguoiThan WHERE malop ='"+thongTinGiaoVien.getLop().trim()+"'");
                    for (int i = 0; i < arrayDanhSachHocSinh.size(); i++){
                        databaseSQLite.InsertThongTinHocSinh(arrayDanhSachHocSinh.get(i).getMaHocSinh().trim(),arrayDanhSachHocSinh.get(i).getHoTen().trim(),
                                arrayDanhSachHocSinh.get(i).getNgaySinh().trim(), arrayDanhSachHocSinh.get(i).getLopHocSinh().trim(),
                                arrayDanhSachHocSinh.get(i).getGioiTinh().trim(),arrayDanhSachHocSinh.get(i).getDiaChi().trim(),
                                arrayDanhSachHocSinh.get(i).getHinhAnh(),arrayDanhSachHocSinh.get(i).getMaGiaoVien().trim());
                        databaseSQLite.InsertPayment(arrayDanhSachHocSinh.get(i).getMaHocSinh().trim(),arrayDanhSachHocSinh.get(i).getLopHocSinh().trim(),
                                0);
                    }
                    for (int i = 0; i < arrayDanhSachNguoiThan.size(); i++){
                        databaseSQLite.InsertThongTinNguoiThan(arrayDanhSachNguoiThan.get(i).getMaUID().trim(),arrayDanhSachNguoiThan.get(i).getHoTen(),
                                arrayDanhSachNguoiThan.get(i).getDiaChi(),arrayDanhSachNguoiThan.get(i).getQuanHe(),arrayDanhSachNguoiThan.get(i).getMaHocSinh(),
                                arrayDanhSachNguoiThan.get(i).getSoDienThoai(),arrayDanhSachNguoiThan.get(i).getHinhAnh(),arrayDanhSachNguoiThan.get(i).getLop());
                    }
                    CheckSave();
                    startActivity(new Intent(ManHinhDangNhapActivity.this,MainActivity.class));
                    finish();
                }else {
                    CheckSave();
                    startActivity(new Intent(ManHinhDangNhapActivity.this,MainActivity.class));
                    finish();
                }
            }else {
                Toast.makeText(context,"Tài khoản chưa được đăng ký",Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }
    //Broadcast kiem tra internet
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED){
                    kiemTraKetNoiInternet = true;
                    init();
                }else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED){
                    kiemTraKetNoiInternet = false;
                    Toast.makeText(ManHinhDangNhapActivity.this,"Lỗi Intertnet, Vui lòng kiểm tra lại!",Toast.LENGTH_SHORT).show();
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
