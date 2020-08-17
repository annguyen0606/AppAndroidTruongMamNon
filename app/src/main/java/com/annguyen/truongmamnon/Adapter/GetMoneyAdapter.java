package com.annguyen.truongmamnon.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.annguyen.truongmamnon.Model.ThongTinHocSinhThuTien;
import com.annguyen.truongmamnon.Model.ThongTinThongKe;
import com.annguyen.truongmamnon.R;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetMoneyAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<ThongTinHocSinhThuTien> thongTinHocSinhThuTiens;

    public GetMoneyAdapter(Context context, int layout, List<ThongTinHocSinhThuTien> thongTinHocSinhThuTiens) {
        this.context = context;
        this.layout = layout;
        this.thongTinHocSinhThuTiens = thongTinHocSinhThuTiens;
    }

    @Override
    public int getCount() {
        return thongTinHocSinhThuTiens.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        TextView txtTen;
        TextView ngaySinh;
        TextView maSoHS;
        TextView numberMoney;
        TextView status;
        CircleImageView imageSqlite;
        ImageView imgStatusPayment;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GetMoneyAdapter.ViewHolder holder;
        if (convertView == null){
            holder = new GetMoneyAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtTen = convertView.findViewById(R.id.txtTenDongDuLieuThuTien);
            holder.ngaySinh = convertView.findViewById(R.id.txtNgaySinhDongDuLieuThuTien);
            holder.maSoHS = convertView.findViewById(R.id.txtMaHocSinhDongDuLieuThuTien);
            holder.numberMoney = convertView.findViewById(R.id.tvMoneyNeedGetDongDuLieuThuTien);
            holder.status = convertView.findViewById(R.id.tvStatusConfirmDongDuLieuThuTien);
            holder.imgStatusPayment = convertView.findViewById(R.id.imageStatusThuTien);
            holder.imageSqlite = convertView.findViewById(R.id.circleHinhDanhHocSinhNopTienDongDuLieuThanhToan);
            convertView.setTag(holder);
        }else {
            holder = (GetMoneyAdapter.ViewHolder) convertView.getTag();
        }

        ThongTinHocSinhThuTien hocSinhThuTien = thongTinHocSinhThuTiens.get(position);
        holder.txtTen.setText(hocSinhThuTien.getTen().toString());
        holder.ngaySinh.setText(hocSinhThuTien.getNgaySinh().toString());
        holder.maSoHS.setText(hocSinhThuTien.getMaHs().toString());
        holder.numberMoney.setText(hocSinhThuTien.getNumberMoney().toString());
        holder.status.setText(String.valueOf(hocSinhThuTien.getStatus()));
        byte[] byteAnhHocSinh = hocSinhThuTien.getHinhAnh();
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(byteAnhHocSinh,
                0, byteAnhHocSinh.length);
        holder.imageSqlite.setImageBitmap(decodebitmap);
        if (Integer.parseInt(holder.status.getText().toString().trim()) == 1){
            holder.imgStatusPayment.setImageResource(R.drawable.ic_check);
        }else {
            holder.imgStatusPayment.setImageResource(R.drawable.ic_priority_high);
        }
        return convertView;
    }
}
