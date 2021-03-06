package com.annguyen.truongmamnon.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private static View view;//Bien view chinh la fragment
    private static SwipeRefreshLayout refreshLayout;//Refresh list view
    private static ListView listViewHocSinh;//list view hoc sinh
    private static ArrayList<ThongTinHocSinhRutGon> arrayHocSinh; //Chua nhung thong tin hien thi co ban
    private static ArrayList<ThongTinHocSinhRutGon> arrayHocSinhTemp; //Chua nhung thong tin hien thi co ban
    private static ArrayList<ThongTinHocSinhRutGon> arrayHocSinhTemp2; //Chua nhung thong tin hien thi co ban
    private SharedPref sharedPref; //Class de moc du lieu tu SharedPreferences
    private static String textLop = "";//Chua ten lop de tac dong
    private static DiaryTapAdapter nhatKyChamTheAdapter;//Adapter de do du lieu tu bang arrayHocSinh vao List view

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
        arrayHocSinhTemp = new ArrayList<>();
        arrayHocSinhTemp2 = new ArrayList<>();
        arrayHocSinh.clear();
        /*Moc tat ca du lieu tu UIDTag table trong sqlite o 2 truong la uid va trang thai xac nhan*/
        Cursor dataNguoiThan = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM UIDTag ut INNER JOIN ThongTinNguoiThan nt on TRIM(ut.Uid) = TRIM(nt.Uid) " +
                "where TRIM(nt.Lop) = '"+textLop.trim()+"'");
        while (dataNguoiThan.moveToNext()){
            String mahs = dataNguoiThan.getString(9);
            String name = dataNguoiThan.getString(6);
            String quanHe = dataNguoiThan.getString(8);
            byte[] abc = dataNguoiThan.getBlob(11);
            String maUid = dataNguoiThan.getString(1);
            arrayHocSinhTemp.add(new ThongTinHocSinhRutGon(name,mahs,quanHe,maUid,0,abc));
        }
        if (arrayHocSinhTemp.size() > 0){
            for (int i = arrayHocSinhTemp.size() - 1; i >= 0; i--){
                arrayHocSinhTemp2.add(new ThongTinHocSinhRutGon(arrayHocSinhTemp.get(i).getTen(),arrayHocSinhTemp.get(i).getMaHs(),arrayHocSinhTemp.get(i).getDiaChi(),
                        arrayHocSinhTemp.get(i).getMaUid(),0,arrayHocSinhTemp.get(i).getHinhAnh()));
            }
            for (ThongTinHocSinhRutGon rutGon : arrayHocSinhTemp2){
                Cursor kiemTraTrangThaiDonCon = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM KiemTraDonCon WHERE TRIM(Uid) = '"+rutGon.getMaUid().trim()+"' and TRIM(MaHs) = '"+rutGon.getMaHs().trim()+"'");
                if (kiemTraTrangThaiDonCon.getCount() >= 1){
                    arrayHocSinh.add(new ThongTinHocSinhRutGon(rutGon.getTen(),rutGon.getMaHs(),rutGon.getDiaChi(),rutGon.getMaUid(),1,rutGon.getHinhAnh()));
                }else {
                    countConfirmTakeStudent++;
                    arrayHocSinh.add(new ThongTinHocSinhRutGon(rutGon.getTen(),rutGon.getMaHs(),rutGon.getDiaChi(),rutGon.getMaUid(),0,rutGon.getHinhAnh()));
                }
            }
        }
        nhatKyChamTheAdapter.notifyDataSetChanged();
        return countConfirmTakeStudent;
    }


}
