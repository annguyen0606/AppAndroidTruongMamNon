package com.annguyen.truongmamnon.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.annguyen.truongmamnon.Activity.InformationMembersActivity;
import com.annguyen.truongmamnon.Activity.MainActivity;
import com.annguyen.truongmamnon.Activity.ManHinhDangNhapActivity;
import com.annguyen.truongmamnon.Adapter.DiaryTapAdapter;
import com.annguyen.truongmamnon.Controller.SharedPref;
import com.annguyen.truongmamnon.Model.ThongTinHocSinhRutGon;
import com.annguyen.truongmamnon.R;

import java.util.ArrayList;

public class FragmentDanhSachChamThe extends Fragment{
    private View view;//Bien view chinh la fragment
    private static SwipeRefreshLayout refreshLayout;//Refresh list view
    private static ListView listViewHocSinh;//list view hoc sinh
    private static ArrayList<ThongTinHocSinhRutGon> arrayHocSinh; //Chua nhung thong tin hien thi co ban
    private static ArrayList<String> arrayData; //Array se chua cac UID trong table UIDTag sqlite
    private static ArrayList<String> arrayDataTemp;//Array se chua cac UID tam thoi trong table UIDTag sqlite
    private SharedPref sharedPref; //Class de moc du lieu tu SharedPreferences
    private static String textLop = "";//Chua ten lop de tac dong
    private static DiaryTapAdapter nhatKyChamTheAdapter;//Adapter de do du lieu tu bang arrayHocSinh vao List view
    private static ImageView imageViewBackground;//Logo conek
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Khoi tao bien view
        view = inflater.inflate(R.layout.fragment_danh_sach_cham_the,container,false);
        //Khai tao ham khoi tao
        init();
        //Khai bao ham anh xa
        AnhXa();
        return view;
    }

    private void init() {
        /*Khoi tao bien sharedPref
        * Lay gia tri lop
        * khoi tao mang arrayHocSinh*/
        sharedPref = new SharedPref(view.getContext());
        textLop = SharedPref.get(ManHinhDangNhapActivity.CURRENT_Class,String.class);
        arrayHocSinh = new ArrayList<>();
    }

    private void AnhXa() {
        refreshLayout = view.findViewById(R.id.pullToRefreshNhatKyChamThe);
        listViewHocSinh = view.findViewById(R.id.lvListNhatKyChamTheActivity);
        imageViewBackground = view.findViewById(R.id.backgroundFragmentDanhSachChamThe);

        nhatKyChamTheAdapter = new DiaryTapAdapter(view.getContext(),R.layout.dong_du_lieu_fragment_danh_sach_cham_the,arrayHocSinh);
        listViewHocSinh.setAdapter(nhatKyChamTheAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadDuLieu();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
        /*Bat su kien khi click item */
        listViewHocSinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), InformationMembersActivity.class);
                intent.putExtra("data",arrayHocSinh.get(position).getMaHs());
                intent.putExtra("data2",arrayHocSinh.get(position).getMaUid());
                startActivity(intent);
            }
        });
        int countDontTakeStudent = LoadDuLieu();
        if (countDontTakeStudent > 0){
            SharedPref.put(ManHinhDangNhapActivity.CURRENT_STUDENTS_COUNT,countDontTakeStudent);
            MainActivity.tabLayout.getTabAt(0).getOrCreateBadge().setVisible(true);
            MainActivity.tabLayout.getTabAt(0).getOrCreateBadge().setNumber(countDontTakeStudent);
            MainActivity.tabLayout.getTabAt(0).getOrCreateBadge().setMaxCharacterCount(3);
        }else {
            MainActivity.tabLayout.getTabAt(0).getOrCreateBadge().setVisible(false);
        }
    }

    public static int LoadDuLieu() {
        int countConfirmTakeStudent = 0;
        /*Khoi tao 2 mang chua UID va 2 mang chua status*/
        arrayData = new ArrayList<String>();
        arrayDataTemp = new ArrayList<String>();
        ArrayList<Integer> arrayStatus = new ArrayList<>();
        ArrayList<Integer> arrayStatusTemp = new ArrayList<>();
        arrayHocSinh.clear();
        /*Moc tat ca du lieu tu UIDTag table trong sqlite o 2 truong la uid va trang thai xac nhan*/
        Cursor dataNguoiThan = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM UIDTag");
        while (dataNguoiThan.moveToNext()){
            String dataUid = dataNguoiThan.getString(1).trim();
            arrayDataTemp.add(dataUid);
            arrayStatusTemp.add(dataNguoiThan.getInt(3));
        }
        /*Dao nguoc lai mang arrayDataTemp va luu vao arrayData, mang Status like that*/
        for (int i = 0; i < arrayDataTemp.size(); i++){
            String abc = String.valueOf(arrayDataTemp.get(arrayDataTemp.size() - i - 1));
            int cde = arrayStatusTemp.get(arrayDataTemp.size() - i - 1);
            arrayData.add(abc);
            arrayStatus.add(cde);
        }
        /*Neu co du lieu thi an logo conek di
        * Moc tat ca cac data can thiet tu table ThongTinNguoiThan roi do bao mang arrayHocSinh*/
        if (arrayData.size() > 0){
            imageViewBackground.setVisibility(View.INVISIBLE);
            int index = 0;
            for (String str : arrayData){
                Cursor dataNT = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinNguoiThan WHERE Uid = '"+str+"'");
                while (dataNT.moveToNext()){
                    String lop = dataNT.getString(8);
                    if (lop.trim().equals(textLop)){
                        String mahs = dataNT.getString(5);
                        String name = dataNT.getString(2);
                        String quanHe = dataNT.getString(4);
                        byte[] abc = dataNT.getBlob(7);
                        String maUid = dataNT.getString(1);
                        arrayHocSinh.add(new ThongTinHocSinhRutGon(name,mahs,quanHe,maUid,arrayStatus.get(index),abc));
                        if (arrayStatus.get(index) == 0){
                            countConfirmTakeStudent++;
                        }
                    }else {
                    }
                }
                index++;
            }
        }else {
            imageViewBackground.setVisibility(View.VISIBLE);
        }
        nhatKyChamTheAdapter.notifyDataSetChanged();
        return countConfirmTakeStudent;
    }


}
