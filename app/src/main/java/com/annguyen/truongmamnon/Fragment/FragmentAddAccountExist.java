package com.annguyen.truongmamnon.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.service.voice.VoiceInteractionService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.annguyen.truongmamnon.Model.ThongTinHocSinh;
import com.annguyen.truongmamnon.Model.ThongTinNguoiThan;
import com.annguyen.truongmamnon.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentAddAccountExist extends Fragment implements View.OnClickListener {
    private final int RESQUEST_CODE_CAMERA = 123;
    private final int RESQUEST_CODE_FOLDER = 456;
    private View view;
    private EditText codeParent, nameParent, addressParent, phoneParent, relativeParent;
    private ImageView imageParent,checkCodeParent;
    private CircleImageView imageStudent;
    private TextView codeStudent, dateStudent, addressStudent, sexStudent, classStudent;
    private Spinner nameStudent;
    private Button getDataInsert;
    private ImageButton chooseCameraParent,chooseImageFolder;

    private DataProvider dataProvider;
    private SharedPref sharedPref;

    private SwipeRefreshLayout getDataUID;
    String txtMaLop = "";
    ArrayList<ThongTinHocSinh> danhSachHocSinh;
    byte[] imageByteParent;

    boolean checkUIDParent = false;

    boolean taiKhoanDangKy = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_account_exist,container,false);
        init();
        AnhXa();
        return view;
    }

    private void init() {
        sharedPref = new SharedPref(view.getContext());
        txtMaLop = SharedPref.get(ManHinhDangNhapActivity.CURRENT_Class,String.class);
        danhSachHocSinh = new ArrayList<>();
    }

    private void AnhXa() {
        codeParent = view.findViewById(R.id.edtUidParentAddAccountFragmentExist);
        nameParent = view.findViewById(R.id.edtNameParentAddAccountFragmentExist);
        addressParent = view.findViewById(R.id.edtAddressParentAddAccountFragmentExist);
        phoneParent = view.findViewById(R.id.edtPhoneParentAddAccountFragmentExist);
        relativeParent = view.findViewById(R.id.edtRelavtiveParentAddAccountFragmentExist);

        imageParent = view.findViewById(R.id.imgHinhAnhCamreFramentAddAccountExist);
        imageStudent = view.findViewById(R.id.circleHinhAnhStudentAddAccountExist);

        codeStudent = view.findViewById(R.id.tvCodeStudentAddAccountExist);
        dateStudent = view.findViewById(R.id.tvDateOfBirthStudentAddAccountFragmentExist);
        addressStudent = view.findViewById(R.id.tvAddressStudentAddAccountExist);
        sexStudent = view.findViewById(R.id.tvSexStudentAddAccountExist);
        classStudent = view.findViewById(R.id.tvClassStudentAddAccountExist);

        nameStudent = view.findViewById(R.id.spinnerNameStudentAddAccountExistFragment);

        getDataInsert = view.findViewById(R.id.btnInsertImageAddAccountFragmentExist);
        chooseCameraParent = view.findViewById(R.id.chooseCameraAddAccountFragmentExist);
        chooseImageFolder = view.findViewById(R.id.chooseImageFromFolderAddAccountFragmentExist);
        checkCodeParent = view.findViewById(R.id.imageCheckCodeParentAddAccountFragmentExist);
        getDataUID = view.findViewById(R.id.pullToGetDataAddAccountExist);

        view.findViewById(R.id.imageCheckCodeParentAddAccountFragmentExist).setOnClickListener(this);
        view.findViewById(R.id.chooseCameraAddAccountFragmentExist).setOnClickListener(this);
        view.findViewById(R.id.btnInsertImageAddAccountFragmentExist).setOnClickListener(this);
        view.findViewById(R.id.chooseImageFromFolderAddAccountFragmentExist).setOnClickListener(this);
        classStudent.setText(txtMaLop);

        getDataUID.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshGetData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataUID.setRefreshing(false);
                    }
                },1000);
            }
        });

        nameStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (MainActivity.kiemTraKetNoiInternet == true){
                    codeStudent.setText(danhSachHocSinh.get(position).getMaHocSinh());
                    dateStudent.setText(danhSachHocSinh.get(position).getNgaySinh());
                    sexStudent.setText(danhSachHocSinh.get(position).getGioiTinh());
                    addressStudent.setText(danhSachHocSinh.get(position).getDiaChi());

                    Bitmap decodebitmapnt = BitmapFactory.decodeByteArray(danhSachHocSinh.get(position).getHinhAnh(),
                            0, danhSachHocSinh.get(position).getHinhAnh().length);
                    imageStudent.setImageBitmap(decodebitmapnt);

                    KiemTraSoTaiKhoanDaDangKy kiemTraSoTaiKhoanDaDangKy = new KiemTraSoTaiKhoanDaDangKy(view.getContext());
                    kiemTraSoTaiKhoanDaDangKy.execute(codeStudent.getText().toString().trim());
                    //Toast.makeText(view.getContext(),nameStudent.getSelectedItem().toString().trim(),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(view.getContext(),"Xin kiểm tra lại kết nối Internet!",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void RefreshGetData() {
        if (MainActivity.kiemTraKetNoiInternet == true){
            codeParent.setText(SharedPref.get(ManHinhDangNhapActivity.CURRENT_UID,String.class));
            danhSachHocSinh = new ArrayList<>();
            Cursor dataHs = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinHocSinh");
            while (dataHs.moveToNext()){
                ThongTinHocSinh thongTinHocSinh = new ThongTinHocSinh("ABC","ABC","ABC","ABC","ABC","ABC",new byte[0],"ABC");
                thongTinHocSinh.setHoTen(dataHs.getString(2));
                thongTinHocSinh.setDiaChi(dataHs.getString(6));
                thongTinHocSinh.setGioiTinh(dataHs.getString(5));
                thongTinHocSinh.setLopHocSinh(dataHs.getString(4));
                thongTinHocSinh.setMaHocSinh(dataHs.getString(1));
                thongTinHocSinh.setNgaySinh(dataHs.getString(3));
                thongTinHocSinh.setHinhAnh(dataHs.getBlob(7));
                danhSachHocSinh.add(thongTinHocSinh);
            }
            ArrayList<String> arraySpinnerNameStudent = new ArrayList<>();
            for (int i = 0; i < danhSachHocSinh.size(); i++){
                arraySpinnerNameStudent.add(danhSachHocSinh.get(i).getHoTen());
            }
            ArrayAdapter arrayAdapterSex = new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arraySpinnerNameStudent);
            nameStudent.setAdapter(arrayAdapterSex);
        }else {
            Toast.makeText(view.getContext(),"Xin kiểm tra lại kết nối Internet!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInsertImageAddAccountFragmentExist:
                if (MainActivity.kiemTraKetNoiInternet == true){
                    if (taiKhoanDangKy == true){
                        if (checkUIDParent == true){
                            if (imageParent.getDrawable() != null
                                    && !codeParent.getText().toString().trim().equals("")
                                    && !codeParent.getText().toString().trim().equals("")
                                    && !nameParent.getText().toString().trim().equals("")
                                    && !addressParent.getText().toString().trim().equals("")
                                    && !phoneParent.getText().toString().trim().equals("")
                                    && !relativeParent.getText().toString().trim().equals("")){

                                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageParent.getDrawable();
                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                System.out.println(bitmap.getWidth());
                                System.out.println(bitmap.getHeight());
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                                imageByteParent = byteArrayOutputStream.toByteArray();

                                InsertIntoDataBase insertIntoDataBase = new InsertIntoDataBase(view.getContext());
                                int checkInsert = 0;
                                insertIntoDataBase.execute(checkInsert);
                            }else {
                                Toast.makeText(view.getContext(),"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(view.getContext(),"Mã số thẻ đã tồn tài!",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(view.getContext(),"Tài khoản không thể đăng ký thêm",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(view.getContext(),"Xin kiểm tra lại kết nối Internet!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chooseCameraAddAccountFragmentExist:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,RESQUEST_CODE_CAMERA);
                break;
            case R.id.imageCheckCodeParentAddAccountFragmentExist:
                if (MainActivity.kiemTraKetNoiInternet == true){
                    if (!codeParent.getText().toString().trim().equals("123456")){
                        CheckUidOfParent checkUidOfParent = new CheckUidOfParent(view.getContext());
                        checkUidOfParent.execute(codeParent.getText().toString().trim());
                    }else {
                        Toast.makeText(view.getContext(),"Bạn chưa chạm thẻ",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(view.getContext(),"Xin kiểm tra lại kết nối Internet!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chooseImageFromFolderAddAccountFragmentExist:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,RESQUEST_CODE_FOLDER);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESQUEST_CODE_CAMERA && resultCode == getActivity().RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageParent.setImageBitmap(bitmap);
        }
        if (requestCode == RESQUEST_CODE_FOLDER && resultCode == getActivity().RESULT_OK && data != null){
            Uri uri = data.getData();
            System.out.println(uri);
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageParent.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private class CheckUidOfParent extends AsyncTask<String,Void,ThongTinNguoiThan>{
        ProgressDialog progressDialog;
        public CheckUidOfParent(Context mContext){
            progressDialog = new ProgressDialog(mContext);
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected ThongTinNguoiThan doInBackground(String... strings) {
            ThongTinNguoiThan thongTinNguoiThan = dataProvider.getInstance().LayThongTinNguoiThan("SELECT *FROM ThongTinNguoiThan WHERE uid = '"+strings[0]+"'");
            return thongTinNguoiThan;
        }

        @Override
        protected void onPostExecute(ThongTinNguoiThan thongTinNguoiThan) {
            if (thongTinNguoiThan.getMaUID().trim().equals("ABC")){
                checkCodeParent.setImageResource(R.drawable.ic_check);
                checkUIDParent = true;
            }else {
                Toast.makeText(view.getContext(),"Mã người thân đã tồn tại",Toast.LENGTH_SHORT).show();
                checkCodeParent.setImageResource(R.drawable.ic_priority_high);
            }
            progressDialog.dismiss();
        }
    }

    private class KiemTraSoTaiKhoanDaDangKy extends AsyncTask<String,Void,ArrayList<ThongTinNguoiThan>>{
        ProgressDialog progressDialog;
        public KiemTraSoTaiKhoanDaDangKy(Context mContext){
            progressDialog = new ProgressDialog(mContext);
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<ThongTinNguoiThan> doInBackground(String... strings) {
            ArrayList<ThongTinNguoiThan> danhSachNguoiThanDangKy = new ArrayList<>();
            danhSachNguoiThanDangKy = dataProvider.getInstance().LayDanhSachThongTinNguoiThan("SELECT *FROM ThongTinNguoiThan WHERE mahs = '"+strings[0]+"'");
            return danhSachNguoiThanDangKy;
        }

        @Override
        protected void onPostExecute(ArrayList<ThongTinNguoiThan> thongTinNguoiThans) {
            if (thongTinNguoiThans.size() > 3){
                Toast.makeText(view.getContext(),"Đã đăng ký đủ số tài khoản",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(view.getContext(),"Được phép thêm thẻ cho học sinh này",Toast.LENGTH_SHORT).show();
                taiKhoanDangKy = true;
            }
            progressDialog.dismiss();
        }
    }
    private class InsertIntoDataBase extends AsyncTask<Integer,Void,Integer>{
        ProgressDialog progressDialog;

        public InsertIntoDataBase(Context mContext) {
            progressDialog = new ProgressDialog(mContext);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Integer doInBackground(Integer... integers) {
            return dataProvider.getInstance().InsertThongTinPhuHuynh(codeParent.getText().toString().trim(),nameParent.getText().toString().trim(),
                    addressParent.getText().toString().trim(),relativeParent.getText().toString().trim(),codeStudent.getText().toString().trim(),
                    phoneParent.getText().toString().trim(),imageByteParent,classStudent.getText().toString().trim());
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1){
                Toast.makeText(view.getContext(),"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                checkUIDParent = false;
                taiKhoanDangKy = false;
                ClearData();
            } else {
                Toast.makeText(view.getContext(),"Đăng ký thất bại",Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    void ClearData(){
        codeStudent.setText("");
        nameParent.setText("");
        addressParent.setText("");
        addressStudent.setText("");
        codeStudent.setText("");
        phoneParent.setText("");
        relativeParent.setText("");
        dateStudent.setText("");
        imageByteParent = new byte[0];
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(imageByteParent,
                0, imageByteParent.length);
        imageParent.setImageBitmap(decodebitmap);
        imageStudent.setImageBitmap(decodebitmap);
    }
}
