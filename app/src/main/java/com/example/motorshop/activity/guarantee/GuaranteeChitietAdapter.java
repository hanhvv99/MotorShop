package com.example.motorshop.activity.guarantee;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.DanhSachSanPhamBaoHanh;
import com.example.motorshop.db.DBManager;

import java.util.ArrayList;

public class GuaranteeChitietAdapter extends BaseAdapter {

    private ArrayList<DanhSachSanPhamBaoHanh> danhSachs;

    GuaranteeChitietAdapter(GuaranteeChitietActivity guaranteeChitietActivity, ArrayList<DanhSachSanPhamBaoHanh> danhSachs) {
        this.danhSachs = danhSachs;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return danhSachs.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return danhSachs.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewHolder;
        if (convertView == null) {
            viewHolder = View.inflate(parent.getContext(), R.layout.item_list_chitiet_baohanh, null);
        } else viewHolder = convertView;

        //Đưa dữ liệu phần tử vào View
        DanhSachSanPhamBaoHanh ds = (DanhSachSanPhamBaoHanh) getItem(position);
        ((TextView) viewHolder.findViewById(R.id.EditTextNDBH)).setText(ds.getNoiDungBH());
        ((TextView) viewHolder.findViewById(R.id.EditTextPBH)).setText(String.valueOf(ds.getPhiBH()));

        return viewHolder;
    }
}
