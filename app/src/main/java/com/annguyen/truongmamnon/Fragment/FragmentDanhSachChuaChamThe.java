package com.annguyen.truongmamnon.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.annguyen.truongmamnon.Activity.ManHinhDangNhapActivity;
import com.annguyen.truongmamnon.Adapter.DiaryNotTapAdapter;
import com.annguyen.truongmamnon.Controller.SharedPref;
import com.annguyen.truongmamnon.Model.ThongTinHocSinhRutGon;
import com.annguyen.truongmamnon.R;

import org.w3c.dom.CDATASection;

import java.util.ArrayList;

public class FragmentDanhSachChuaChamThe extends Fragment {
    private View view;
    private static SwipeRefreshLayout refreshLayout;
    private static ListView listViewHocSinh;
    private static ArrayList<ThongTinHocSinhRutGon> arrayHocSinh; //Chua nhung thong tin hien thi co ban
    private static ArrayList<String> arrayMaHsTemp;
    private SharedPref sharedPref;
    private static DiaryNotTapAdapter nhatKyChamTheAdapter;
    private static String tenLop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_danh_sach_chua_cham_the, container, false);
        init();
        AnhXa();
        return view;
    }

    private void init() {
        sharedPref = new SharedPref(view.getContext());
        tenLop = SharedPref.get(ManHinhDangNhapActivity.CURRENT_Class, String.class);
        arrayHocSinh = new ArrayList<>();
        arrayMaHsTemp = new ArrayList<>();
    }

    private void AnhXa() {
        refreshLayout = view.findViewById(R.id.pullToRefreshDanhSachChuaChamThe);
        listViewHocSinh = view.findViewById(R.id.lvListNhatKyChuaChamTheActivity);

        nhatKyChamTheAdapter = new DiaryNotTapAdapter(view.getContext(), R.layout.dong_du_lieu_danh_sach_chua_cham_the, arrayHocSinh);
        listViewHocSinh.setAdapter(nhatKyChamTheAdapter);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LoadDuLieuChuaChamThe();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadDuLieuChuaChamThe();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        listViewHocSinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Show_Dialog_Call_Phone();
                Cursor DataNTPhone = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinNguoiThan");
                ArrayList<String> Phone = new ArrayList<>();
                ArrayList<String> MaHs = new ArrayList<>();
                ArrayList<String> quanHeParent = new ArrayList<>();


                ArrayList<String> phoneNeed = new ArrayList<>();
                ArrayList<String> quanHeNeed = new ArrayList<>();
                while (DataNTPhone.moveToNext()) {
                    Phone.add(DataNTPhone.getString(6));
                    MaHs.add(DataNTPhone.getString(5));
                    quanHeParent.add(DataNTPhone.getString(4));
                }
                for (int i = 0; i < Phone.size(); i++) {
                    if (arrayHocSinh.get(position).getMaHs().trim().equals(MaHs.get(i).trim())) {
                        phoneNeed.add(Phone.get(i).trim());
                        quanHeNeed.add(quanHeParent.get(i).trim());
                        /*Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + Phone.get(i).trim()));
                        view.getContext().startActivity(intent);*/
                    }
                }

                Show_Dialog_Call_Phone(phoneNeed,quanHeNeed);
            }
        });

        LoadDuLieuChuaChamThe();
    }

    public static void LoadDuLieuChuaChamThe() {
        arrayMaHsTemp = new ArrayList<>();
        arrayHocSinh.clear();
        ArrayList<String> fullStudents = new ArrayList<>();
        //Moc tat ca cac UID data tu table UIDTag
        Cursor fullST = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT * FROM ThongTinHocSinh where TRIM(Lop) = '"+tenLop.trim()+"'");
        while (fullST.moveToNext()){
            fullStudents.add(fullST.getString(1).trim());
        }
        for (String strMaHS : fullStudents){
            Cursor dataNguoiThan = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM UIDTag ut INNER JOIN ThongTinNguoiThan nt on TRIM(ut.Uid) = TRIM(nt.Uid) " +
                    "where TRIM(nt.Lop) = '"+tenLop.trim()+"' and TRIM(nt.MaHs) = '"+strMaHS+"'");
            if(dataNguoiThan.getCount() >= 1){

            }else{
                arrayMaHsTemp.add(strMaHS);
            }
        }
        if (arrayMaHsTemp.size() > 0){
            for (String strUID : arrayMaHsTemp){
                Cursor dataNT = ManHinhDangNhapActivity.databaseSQLite.GetData("SELECT *FROM ThongTinHocSinh WHERE TRIM(MaHs) = '"+strUID+"'");
                while (dataNT.moveToNext()){
                    String mahs = dataNT.getString(1);
                    String name = dataNT.getString(2);
                    String diaChi = dataNT.getString(6);
                    byte[] abc = dataNT.getBlob(7);
                    String maUid = dataNT.getString(1);
                    arrayHocSinh.add(new ThongTinHocSinhRutGon(name,mahs,diaChi,maUid,1,abc));
                }
            }
        }
        nhatKyChamTheAdapter.notifyDataSetChanged();
    }

    void Show_Dialog_Call_Phone(final ArrayList<String> phoneToCall, ArrayList<String> relativeToCall) {
        ArrayList<String> arrayParent = new ArrayList<>();
        for (int i = 0; i < phoneToCall.size(); i++){
            arrayParent.add("SĐT " + relativeToCall.get(i).toString().trim() + " : " + phoneToCall.get(i).toString().trim());
        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
        View mView = View.inflate(getContext(),R.layout.call_phone_layout,null);
        final ListView listViewData = mView.findViewById(R.id.listViewDataParentLayout);
        final ArrayAdapter<String> arrayAdapterParent = new ArrayAdapter<String>(mView.getContext(),android.R.layout.simple_list_item_1,arrayParent);
        mBuilder.setView(mView);
        listViewData.setAdapter(arrayAdapterParent);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phoneToCall.get(position).trim()));
                        view.getContext().startActivity(intent);
            }
        });
    }
}
