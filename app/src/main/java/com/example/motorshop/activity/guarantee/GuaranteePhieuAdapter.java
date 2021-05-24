package com.example.motorshop.activity.guarantee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.ChiTietBaoHanh;
import com.example.motorshop.datasrc.DanhSachSanPhamBaoHanh;

import java.util.ArrayList;

public class GuaranteePhieuAdapter extends ArrayAdapter<ChiTietBaoHanh> {
    ArrayList<ChiTietBaoHanh> chiTietBaoHanhs;
    Context context;
    int resource;


    public GuaranteePhieuAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ChiTietBaoHanh> chiTietBaoHanhs) {
        super(context, resource, chiTietBaoHanhs);
        this.chiTietBaoHanhs = chiTietBaoHanhs;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return chiTietBaoHanhs.size();
    }


    @Override
    public ChiTietBaoHanh getItem(int position) {
        return chiTietBaoHanhs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        TextView TextViewMaBH, TextViewTenSP, TextViewNgayTao;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_phieu_baohanh,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.TextViewMaBH = convertView.findViewById(R.id.TextViewMaBH);
            viewHolder.TextViewTenSP = convertView.findViewById(R.id.TextViewTenSP);
            viewHolder.TextViewNgayTao = convertView.findViewById(R.id.TextViewNgayTao);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ChiTietBaoHanh chiTietBaoHanh = (ChiTietBaoHanh) getItem(position);
        ArrayList<DanhSachSanPhamBaoHanh> danhSachSanPhamBaoHanhs = new ArrayList<>();

        viewHolder.TextViewMaBH.setText(chiTietBaoHanh.getMaBH());
        DanhSachSanPhamBaoHanh ds = new DanhSachSanPhamBaoHanh();
        viewHolder.TextViewTenSP.setText(ds.getTenSP());
        danhSachSanPhamBaoHanhs.add(ds);
        chiTietBaoHanh.setDanhSachSPBH(danhSachSanPhamBaoHanhs);
        viewHolder.TextViewNgayTao.setText(chiTietBaoHanh.getNgayBH());
        return convertView;
    }
}
