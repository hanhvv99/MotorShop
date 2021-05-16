package com.example.motorshop.activity.guarantee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.ChiTietBaoHanh;
import com.example.motorshop.datasrc.DanhSachSanPhamBaoHanh;
import com.example.motorshop.helper.Icon_Manager;

import java.util.ArrayList;

public class GuaranteeAdapter extends ArrayAdapter<ChiTietBaoHanh> {
    ArrayList<ChiTietBaoHanh> chiTietBaoHanhs;
    Context context;
    int resource;

    public GuaranteeAdapter(Context context, int resource, ArrayList<ChiTietBaoHanh> chiTietBaoHanhs) {
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
        TextView txtTenSP, txtNoiDungBH, txtNgayBH, txtPhiBH, txtEditBH, txtDelBH;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GuaranteeAdapter.ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_guarantee_dssp,parent,false);
            viewHolder = new GuaranteeAdapter.ViewHolder();
            viewHolder.txtTenSP = convertView.findViewById(R.id.txt_tensp);
            viewHolder.txtNoiDungBH = convertView.findViewById(R.id.txt_noi_dung_bh);
            viewHolder.txtNgayBH = convertView.findViewById(R.id.txt_ngay_bh);
            viewHolder.txtPhiBH = convertView.findViewById(R.id.txt_phi_bh);
            viewHolder.txtEditBH = convertView.findViewById(R.id.tv_edit_bh);
            viewHolder.txtDelBH = convertView.findViewById(R.id.tv_delete_bh);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (GuaranteeAdapter.ViewHolder) convertView.getTag();
        }
        ChiTietBaoHanh chiTietBaoHanh = (ChiTietBaoHanh) getItem(position);
        DanhSachSanPhamBaoHanh danhSachSanPhamBaoHanh = new DanhSachSanPhamBaoHanh();
        viewHolder.txtTenSP.setText(danhSachSanPhamBaoHanh.getTenSP());
        viewHolder.txtNoiDungBH.setText(danhSachSanPhamBaoHanh.getNoiDungBH());
        viewHolder.txtNgayBH.setText(chiTietBaoHanh.getNgayBH());
        viewHolder.txtPhiBH.setText(danhSachSanPhamBaoHanh.getPhiBH());

        Icon_Manager icon_manager = new Icon_Manager();
        viewHolder.txtEditBH.setTypeface(icon_manager.get_icons("fonts/fa-brands-400.ttf",getContext()));
        viewHolder.txtEditBH.setTypeface(icon_manager.get_icons("fonts/fa-regular-400.ttf",getContext()));
        viewHolder.txtEditBH.setTypeface(icon_manager.get_icons("fonts/fa-solid-900.ttf",getContext()));
        viewHolder.txtDelBH.setTypeface(icon_manager.get_icons("fonts/fa-brands-400.ttf",getContext()));
        viewHolder.txtDelBH.setTypeface(icon_manager.get_icons("fonts/fa-regular-400.ttf",getContext()));
        viewHolder.txtDelBH.setTypeface(icon_manager.get_icons("fonts/fa-solid-900.ttf",getContext()));

        viewHolder.txtDelBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuaranteeActivity)context).deleteGuarantee(chiTietBaoHanh);
                notifyDataSetChanged();
                ((GuaranteeActivity)context).loadGuarantee();
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
