package com.annguyen.truongmamnon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.annguyen.truongmamnon.Model.IconHomeActivity;
import com.annguyen.truongmamnon.R;

import java.util.List;

public class IconHomeAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<IconHomeActivity> iconsList;

    public IconHomeAdapter(Context context, int layout, List<IconHomeActivity> iconsList) {
        this.context = context;
        this.layout = layout;
        this.iconsList = iconsList;
    }

    @Override
    public int getCount() {
        return iconsList.size();
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
        ImageView iconHomeActivity;
        TextView nameIconHomeActivity;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.iconHomeActivity = convertView.findViewById(R.id.imgIconsHomeActivity);
            holder.nameIconHomeActivity = convertView.findViewById(R.id.txtNameIconsHomeActivity);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        IconHomeActivity iconHomeActivity = iconsList.get(position);
        holder.nameIconHomeActivity.setText(iconHomeActivity.getTen());
        holder.iconHomeActivity.setImageResource(iconHomeActivity.getHinhAnh());
        return convertView;
    }
}
