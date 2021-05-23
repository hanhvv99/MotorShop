package com.example.motorshop.activity.guarantee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.DanhSachSanPhamBaoHanh;

import java.util.List;

public class DanhSachNoiDungAdapter extends ArrayAdapter<DanhSachSanPhamBaoHanh> {
    List<DanhSachSanPhamBaoHanh> danhSachSanPhamBaoHanhs;
    Context context;
    int resource;

    public DanhSachNoiDungAdapter(@NonNull Context context, int resource, List<DanhSachSanPhamBaoHanh> danhSachSanPhamBaoHanhs) {
        super(context,resource,danhSachSanPhamBaoHanhs);
        this.danhSachSanPhamBaoHanhs = danhSachSanPhamBaoHanhs;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return danhSachSanPhamBaoHanhs.size();
    }


    @Override
    public DanhSachSanPhamBaoHanh getItem(int position) {
        return danhSachSanPhamBaoHanhs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        TextView EditTextNDBH, EditTextPBH;
        ImageView TextViewEditBH, TextViewDelBH;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DanhSachNoiDungAdapter.ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_chitiet_baohanh,parent,false);
            viewHolder = new DanhSachNoiDungAdapter.ViewHolder();
            viewHolder.EditTextNDBH = convertView.findViewById(R.id.EditTextNDBH);
            viewHolder.EditTextPBH = convertView.findViewById(R.id.EditTextPBH);
            viewHolder.TextViewEditBH = convertView.findViewById(R.id.TextViewEditBH);
            viewHolder.TextViewDelBH = convertView.findViewById(R.id.TextViewDelBH);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DanhSachSanPhamBaoHanh ds = (DanhSachSanPhamBaoHanh) getItem(position);
        viewHolder.EditTextNDBH.setText(ds.getNoiDungBH());
        viewHolder.EditTextPBH.setText(ds.getPhiBH());
        viewHolder.TextViewEditBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.TextViewDelBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }
}
