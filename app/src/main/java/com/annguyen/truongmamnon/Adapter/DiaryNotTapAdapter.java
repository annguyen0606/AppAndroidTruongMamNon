package com.annguyen.truongmamnon.Adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.annguyen.truongmamnon.Model.ThongTinHocSinh;
import com.annguyen.truongmamnon.Model.ThongTinHocSinhRutGon;
import com.annguyen.truongmamnon.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiaryNotTapAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<ThongTinHocSinhRutGon> thongTinHocSinhs;

    public DiaryNotTapAdapter(Context context, int layout, List<ThongTinHocSinhRutGon> thongTinHocSinhs) {
        this.context = context;
        this.layout = layout;
        this.thongTinHocSinhs = thongTinHocSinhs;
    }

    @Override
    public int getCount() {
        return thongTinHocSinhs.size();
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
        TextView txtMaHS;
        TextView txtDiaChi;
        CircleImageView imageSqlite;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtMaHS = convertView.findViewById(R.id.txtMaHocSinhDongDuLieuLayout);
            holder.txtTen = convertView.findViewById(R.id.txtTenDongDuLieuLayout);
            holder.txtDiaChi = convertView.findViewById(R.id.txtDiaChiDongDuLieuLayout);
            holder.imageSqlite = convertView.findViewById(R.id.circleHinhAnhDongDuLieuLayout);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        ThongTinHocSinhRutGon thongTinHocSinh = thongTinHocSinhs.get(position);
        byte[] byteAnhHocSinh = thongTinHocSinh.getHinhAnh();
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(byteAnhHocSinh,
                0, byteAnhHocSinh.length);
        holder.imageSqlite.setImageBitmap(decodebitmap);
        holder.txtMaHS.setText(thongTinHocSinh.getMaHs());
        holder.txtDiaChi.setText(thongTinHocSinh.getDiaChi());
        holder.txtTen.setText(thongTinHocSinh.getTen());
        return convertView;
    }
}
