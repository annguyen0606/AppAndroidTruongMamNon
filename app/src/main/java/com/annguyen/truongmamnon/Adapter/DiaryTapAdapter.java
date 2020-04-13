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

import com.annguyen.truongmamnon.Model.ThongTinHocSinhRutGon;
import com.annguyen.truongmamnon.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiaryTapAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<ThongTinHocSinhRutGon> thongTinHocSinhs;

    public DiaryTapAdapter(Context context, int layout, List<ThongTinHocSinhRutGon> thongTinHocSinhs) {
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
        TextView txtQuanHe;
        TextView txtStatus;
        ImageView imageStatus;
        CircleImageView imageSqlite;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DiaryTapAdapter.ViewHolder holder;
        if (convertView == null){
            holder = new DiaryTapAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtMaHS = convertView.findViewById(R.id.txtTenHocSinhDongDuLieuFragmentChamThe);
            holder.txtTen = convertView.findViewById(R.id.txtTenDongDuLieuFragmentChamThe);
            holder.txtQuanHe = convertView.findViewById(R.id.txtQuanHeDongDuLieuFragmentChamThe);
            holder.txtStatus = convertView.findViewById(R.id.tvStatusConfirmDongDuLieu);
            holder.imageStatus = convertView.findViewById(R.id.imageStatusFragmentDanhSachChamThe);
            holder.imageSqlite = convertView.findViewById(R.id.circleHinhAnhDongDuLieuFragmentChamThe);
            convertView.setTag(holder);
        }else {
            holder = (DiaryTapAdapter.ViewHolder) convertView.getTag();
        }

        ThongTinHocSinhRutGon thongTinHocSinh = thongTinHocSinhs.get(position);
        byte[] byteAnhHocSinh = thongTinHocSinh.getHinhAnh();
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(byteAnhHocSinh,
                0, byteAnhHocSinh.length);
        holder.imageSqlite.setImageBitmap(decodebitmap);
        holder.txtMaHS.setText(thongTinHocSinh.getMaHs());
        holder.txtQuanHe.setText(thongTinHocSinh.getDiaChi());
        holder.txtTen.setText(thongTinHocSinh.getTen());
        holder.txtStatus.setText(String.valueOf(thongTinHocSinh.getStatus()));
        holder.txtStatus.setVisibility(View.INVISIBLE);
        if (Integer.parseInt(holder.txtStatus.getText().toString().trim()) == 1){
            holder.imageStatus.setImageResource(R.drawable.ic_check);
        }else {
            holder.imageStatus.setImageResource(R.drawable.ic_priority_high);
        }
        return convertView;
    }
}