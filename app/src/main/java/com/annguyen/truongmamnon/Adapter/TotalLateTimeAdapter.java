package com.annguyen.truongmamnon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.annguyen.truongmamnon.Model.ThongTinThongKe;
import com.annguyen.truongmamnon.R;

import java.util.List;

public class TotalLateTimeAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<ThongTinThongKe> thongTinThongKes;

    public TotalLateTimeAdapter(Context context, int layout, List<ThongTinThongKe> thongTinThongKes) {
        this.context = context;
        this.layout = layout;
        this.thongTinThongKes = thongTinThongKes;
    }

    @Override
    public int getCount() {
        return thongTinThongKes.size();
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
        TextView txtPhut;
        TextView txtMoney;
        TextView txtStatusPayment;
        ImageView imgStatusPayment;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TotalLateTimeAdapter.ViewHolder holder;
        if (convertView == null){
            holder = new TotalLateTimeAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtMaHS = convertView.findViewById(R.id.tvCodeFragmentTotalLateMinute);
            holder.txtTen = convertView.findViewById(R.id.tvNameFragmentTotalLateMinute);
            holder.txtPhut = convertView.findViewById(R.id.tvMinuteFragmentTotalLateMinute);
            holder.txtMoney = convertView.findViewById(R.id.tvMoneyFragmentTotalLateMinute);
            holder.txtStatusPayment = convertView.findViewById(R.id.tvStatusPayment);
            holder.imgStatusPayment = convertView.findViewById(R.id.imageStatusPayment);
            convertView.setTag(holder);
        }else {
            holder = (TotalLateTimeAdapter.ViewHolder) convertView.getTag();
        }

        ThongTinThongKe thongTinThongKe = thongTinThongKes.get(position);
        holder.txtMaHS.setText(thongTinThongKe.getMaHS());
        holder.txtMoney.setText(thongTinThongKe.getMoney());
        holder.txtTen.setText(thongTinThongKe.getHoTen());
        holder.txtPhut.setText(String.valueOf(thongTinThongKe.getThoiGian()));
        holder.txtStatusPayment.setText(String.valueOf(thongTinThongKe.getStatusPayment()));
        if (Integer.parseInt(holder.txtStatusPayment.getText().toString().trim()) == 1){
            holder.imgStatusPayment.setImageResource(R.drawable.ic_check);
        }else {
            holder.imgStatusPayment.setImageResource(R.drawable.ic_priority_high);
        }
        return convertView;
    }
}
