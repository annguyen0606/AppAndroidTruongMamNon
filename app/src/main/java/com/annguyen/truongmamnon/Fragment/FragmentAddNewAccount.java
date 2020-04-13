package com.annguyen.truongmamnon.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import java.util.Calendar;

public class FragmentAddNewAccount extends Fragment implements View.OnClickListener{
    private static String dataUIDAddAcountFragment = "123456";
    private View view;
    private ImageButton chooseImageParent, chooseImageStudent, chooseImageParentFolder, chooseImageStudentFolder;
    private ImageView imageParent, imageStudent;
    private Button confirmInsert;
    private EditText nameParent, nameStudent, addressParent, phoneParent, relativeParent, maStudent,
                     addressStudent;
    private static EditText uidParent;
    private ImageView imagePicDate, imageCheckCodeStudent, imageCheckCodeParent;
    private Spinner spinnerSex;
    private TextView txtDateBirth, txtClass;
    private byte[] imageByteParent;
    private byte[] imageByteStudent;
    private final int RESQUEST_CODE_CAMERA = 123;
    private final int RESQUEST_CODE_CAMERA_2 = 111;
    private final int RESQUEST_CODE_FOLDER_PARENT = 456;
    private final int RESQUEST_CODE_FOLDER_STUDENT = 789;
    private DataProvider dataProvider;
    private SharedPref sharedPref;
    private String textMaGiaoVien = "";
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private SwipeRefreshLayout getDataUID;
    int year = 0;
    int month = 0;
    int day = 0;

    private boolean checkCodeStudent = false;
    private boolean checkCodeParent = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_new_acount,container,false);
        init();
        AnhXa();
        return view;
    }

    private void init() {
        sharedPref = new SharedPref(view.getContext());
        textMaGiaoVien = SharedPref.get(ManHinhDangNhapActivity.CURRENT_TEACHER,String.class);

        imageByteParent = new byte[0];
        imageByteStudent = new byte[0];
    }
    private void AnhXa() {
        uidParent = view.findViewById(R.id.edtUidParentAddAccountFragment);
        chooseImageParent = view.findViewById(R.id.chooseCameraAddAccountFragment);
        chooseImageStudent = view.findViewById(R.id.chooseCameraStusendAddAccountFragment);
        txtClass = view.findViewById(R.id.tvClassStudentAddAccountFragment);
        spinnerSex = view.findViewById(R.id.spinnerSexStudentAddAccountFragment);
        txtDateBirth = view.findViewById(R.id.tvDateOfBirthStudentAddAccountFragment);
        imagePicDate = view.findViewById(R.id.imgPicDataAddAccountFragment);
        imageCheckCodeStudent = view.findViewById(R.id.imageCheckCodeStudentAddAccountFragment);
        imageCheckCodeParent = view.findViewById(R.id.imageCheckCodeParentAddAccountFragment);

        imageParent = view.findViewById(R.id.imgHinhAnhCamreFramentAddAccount);
        imageStudent = view.findViewById(R.id.imgHinhAnhStudentCamreFramentAddAccount);

        confirmInsert = view.findViewById(R.id.btnInsertImageAddAccountFragment);

        nameParent = view.findViewById(R.id.edtNameParentAddAccountFragment);
        nameStudent = view.findViewById(R.id.edtNameStudentAddAccountFragment);
        addressParent = view.findViewById(R.id.edtAddressParentAddAccountFragment);
        phoneParent = view.findViewById(R.id.edtPhoneParentAddAccountFragment);
        relativeParent = view.findViewById(R.id.edtRelavtiveParentAddAccountFragment);
        maStudent = view.findViewById(R.id.edtCodeStudentAddAccountFragment);

        addressStudent = view.findViewById(R.id.edtAddressStudentAddAccountFragment);
        getDataUID = view.findViewById(R.id.pullToGetDataAddNewAccount);

        chooseImageParentFolder = view.findViewById(R.id.chooseImageFromFolderAddAccountFragment);
        chooseImageStudentFolder = view.findViewById(R.id.chooseImageStudentFromFolderAddAccountFragment);

        view.findViewById(R.id.chooseImageStudentFromFolderAddAccountFragment).setOnClickListener(this);
        view.findViewById(R.id.chooseImageFromFolderAddAccountFragment).setOnClickListener(this);
        view.findViewById(R.id.chooseCameraStusendAddAccountFragment).setOnClickListener(this);
        view.findViewById(R.id.chooseCameraAddAccountFragment).setOnClickListener(this);
        view.findViewById(R.id.imgPicDataAddAccountFragment).setOnClickListener(this);
        ArrayList<String> arraySpinnerSex = new ArrayList<>();
        arraySpinnerSex.add("Nam");
        arraySpinnerSex.add("Nữ");
        ArrayAdapter arrayAdapterSex = new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arraySpinnerSex);
        spinnerSex.setAdapter(arrayAdapterSex);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtDateBirth.setText(dayOfMonth+"/"+(month +1)+"/"+year);
            }
        };

        imageCheckCodeStudent.setImageResource(R.drawable.ic_autorenew);
        imageCheckCodeParent.setImageResource(R.drawable.ic_autorenew);
        view.findViewById(R.id.imageCheckCodeStudentAddAccountFragment).setOnClickListener(this);
        view.findViewById(R.id.imageCheckCodeParentAddAccountFragment).setOnClickListener(this);
        view.findViewById(R.id.btnInsertImageAddAccountFragment).setOnClickListener(this);
        uidParent.setText(dataUIDAddAcountFragment);
        txtClass.setText(SharedPref.get(ManHinhDangNhapActivity.CURRENT_Class,String.class));

        getDataUID.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataUIDAddAcountFragment = SharedPref.get(ManHinhDangNhapActivity.CURRENT_UID,String.class);
                uidParent.setText(dataUIDAddAcountFragment);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataUID.setRefreshing(false);
                    }
                },1000);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESQUEST_CODE_CAMERA && resultCode == getActivity().RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageParent.setImageBitmap(bitmap);
        }else if (requestCode == RESQUEST_CODE_CAMERA_2 && resultCode == getActivity().RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageStudent.setImageBitmap(bitmap);
        }else if (requestCode == RESQUEST_CODE_FOLDER_PARENT && resultCode == getActivity().RESULT_OK && data != null) {
            Uri uri = data.getData();
            System.out.println(uri);
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageParent.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if (requestCode == RESQUEST_CODE_FOLDER_STUDENT && resultCode == getActivity().RESULT_OK && data != null) {
            Uri uri = data.getData();
            System.out.println(uri);
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageStudent.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chooseImageFromFolderAddAccountFragment:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,RESQUEST_CODE_FOLDER_PARENT);
                break;
            case R.id.chooseImageStudentFromFolderAddAccountFragment:
                Intent intent3 = new Intent(Intent.ACTION_PICK);
                intent3.setType("image/*");
                startActivityForResult(intent3,RESQUEST_CODE_FOLDER_STUDENT);
                break;
            case R.id.btnInsertImageAddAccountFragment:
                if (MainActivity.kiemTraKetNoiInternet == true){
                    if (imageParent.getDrawable() != null && imageStudent.getDrawable() != null
                            && !maStudent.getText().toString().trim().equals("")
                            && !uidParent.getText().toString().trim().equals("")
                            && !nameParent.getText().toString().trim().equals("")
                            && !addressParent.getText().toString().trim().equals("")
                            && !phoneParent.getText().toString().trim().equals("")
                            && !relativeParent.getText().toString().trim().equals("")
                            && !nameStudent.getText().toString().trim().equals("")
                            && !txtDateBirth.getText().toString().trim().equals("")
                            && !txtClass.getText().toString().trim().equals("")
                            && !spinnerSex.getSelectedItem().toString().trim().equals("")
                            && !addressStudent.getText().toString().trim().equals("")){

                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageParent.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        System.out.println(bitmap.getWidth());
                        System.out.println(bitmap.getHeight());
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        imageByteParent = byteArrayOutputStream.toByteArray();

                        bitmapDrawable = (BitmapDrawable) imageStudent.getDrawable();
                        bitmap = bitmapDrawable.getBitmap();
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        imageByteStudent = byteArrayOutputStream.toByteArray();

                        InsertIntoDataBase insertIntoDataBase = new InsertIntoDataBase(view.getContext());
                        int checkInsert = 0;
                        if (checkCodeStudent == true && checkCodeParent == true){
                            insertIntoDataBase.execute(checkInsert);
                        }else {
                            Toast.makeText(view.getContext(),"Mã học sinh đã tồn tại",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(view.getContext(),"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(view.getContext(),"Xin kiểm tra lại kết nối Internet!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chooseCameraAddAccountFragment:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESQUEST_CODE_CAMERA);
                break;
            case R.id.chooseCameraStusendAddAccountFragment:
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent2,RESQUEST_CODE_CAMERA_2);
                break;
            case R.id.imageCheckCodeStudentAddAccountFragment:
                if (MainActivity.kiemTraKetNoiInternet == true){
                    if (!maStudent.getText().toString().trim().equals("")){
                        CheckCodeOfStudent checkCodeOfStudent = new CheckCodeOfStudent(view.getContext());
                        checkCodeOfStudent.execute(maStudent.getText().toString().trim());
                    }else {
                        Toast.makeText(view.getContext(),"Bạn chưa nhập mã học sinh",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(view.getContext(),"Xin kiểm tra lại kết nối Internet!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageCheckCodeParentAddAccountFragment:
                if (MainActivity.kiemTraKetNoiInternet == true){
                    if (!uidParent.getText().toString().trim().equals("123456")){
                        CheckUidOfParent checkUidOfParent = new CheckUidOfParent(view.getContext());
                        checkUidOfParent.execute(uidParent.getText().toString().trim());
                    }else {
                        Toast.makeText(view.getContext(),"Bạn chưa chạm thẻ",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(view.getContext(),"Xin kiểm tra lại kết nối Internet!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgPicDataAddAccountFragment:
                final Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,
                        month,
                        day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
                break;
        }
    }

    private class CheckCodeOfStudent extends AsyncTask<String,Void,ThongTinHocSinh>{
        ProgressDialog progressDialog;
        public CheckCodeOfStudent(Context mContext){
            progressDialog = new ProgressDialog(mContext);
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected ThongTinHocSinh doInBackground(String... strings) {
            ThongTinHocSinh thongTinHocSinh = dataProvider.getInstance().LayThongTinHocSinh("SELECT *FROM ThongTinHocSinh WHERE mahs = '"+strings[0]+"'");
            return thongTinHocSinh;
        }

        @Override
        protected void onPostExecute(ThongTinHocSinh thongTinHocSinh) {
            if (thongTinHocSinh.getMaHocSinh().trim().equals("ABC")){
                imageCheckCodeStudent.setImageResource(R.drawable.ic_check);
                checkCodeStudent = true;
            }else {
                Toast.makeText(view.getContext(),"Mã học sinh đã tồn tại",Toast.LENGTH_SHORT).show();
                imageCheckCodeStudent.setImageResource(R.drawable.ic_priority_high);
            }
            progressDialog.dismiss();
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
                imageCheckCodeParent.setImageResource(R.drawable.ic_check);
                checkCodeParent = true;
            }else {
                Toast.makeText(view.getContext(),"Mã người thân đã tồn tại",Toast.LENGTH_SHORT).show();
                imageCheckCodeParent.setImageResource(R.drawable.ic_priority_high);
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
            return dataProvider.getInstance().InsertThongTinHocSinh(maStudent.getText().toString().trim(),nameStudent.getText().toString().trim(),
                    txtDateBirth.getText().toString().trim(),txtClass.getText().toString().trim(),spinnerSex.getSelectedItem().toString().trim(),
                    addressStudent.getText().toString().trim(),imageByteStudent,textMaGiaoVien);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1){
                int insertSuccess = dataProvider.getInstance().InsertThongTinPhuHuynh(uidParent.getText().toString().trim(),nameParent.getText().toString().trim(),
                        addressParent.getText().toString().trim(),relativeParent.getText().toString().trim(),maStudent.getText().toString().trim(),
                        phoneParent.getText().toString().trim(),imageByteParent,txtClass.getText().toString().trim());
                if (insertSuccess == 1){
                    checkCodeStudent = false;
                    checkCodeParent = false;
                    Toast.makeText(view.getContext(),"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                    ClearData();
                }
            }
            progressDialog.dismiss();
        }
    }

    void ClearData(){
        uidParent.setText("");
        nameParent.setText("");
        nameStudent.setText("");
        addressParent.setText("");
        addressStudent.setText("");
        maStudent.setText("");
        phoneParent.setText("");
        relativeParent.setText("");
        txtDateBirth.setText("");
        imageByteParent = new byte[0];
        imageByteStudent = new byte[0];
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(imageByteParent,
                0, imageByteParent.length);
        imageParent.setImageBitmap(decodebitmap);
        imageStudent.setImageBitmap(decodebitmap);
        imageCheckCodeParent.setImageResource(R.drawable.ic_autorenew);
        imageCheckCodeStudent.setImageResource(R.drawable.ic_autorenew);
    }
}
