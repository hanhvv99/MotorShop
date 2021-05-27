package com.example.motorshop.activity.guarantee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.motorshop.activity.R;

import java.util.ArrayList;


public class PhieuBaoHanhTempAdapter extends ArrayAdapter<PhieuBaoHanhTemp> {
    ArrayList<PhieuBaoHanhTemp> phieuBaoHanhTemps;
    Context context;
    int resource;


    public PhieuBaoHanhTempAdapter(Context context, int resource, ArrayList<PhieuBaoHanhTemp> phieuBaoHanhTemps) {
        super(context, resource, phieuBaoHanhTemps);
        this.phieuBaoHanhTemps = phieuBaoHanhTemps;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return phieuBaoHanhTemps.size();
    }


    @Override
    public PhieuBaoHanhTemp getItem(int position) {
        return phieuBaoHanhTemps.get(position);
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
        PhieuBaoHanhTemp phieuBaoHanhTemp = (PhieuBaoHanhTemp) getItem(position);

        viewHolder.TextViewMaBH.setText(phieuBaoHanhTemp.getMaBH());
        viewHolder.TextViewTenSP.setText(phieuBaoHanhTemp.getTenSP());
        viewHolder.TextViewNgayTao.setText(phieuBaoHanhTemp.getNgayBH());

        return convertView;
    }

}
