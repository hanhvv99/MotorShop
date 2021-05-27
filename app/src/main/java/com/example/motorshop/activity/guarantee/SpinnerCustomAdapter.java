package com.example.motorshop.activity.guarantee;

import android.view.Gravity;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.SanPham;
import com.squareup.picasso.Picasso;

public class SpinnerCustomAdapter extends BaseAdapter {
    Context context;
    Integer flags[];
    String[] sanPhamDonHang;
    LayoutInflater inflter;

    public SpinnerCustomAdapter(Context applicationContext, Integer[] flags, String[] sanPhamDonHang) {
        this.context = applicationContext;
        this.flags = flags;
        this.sanPhamDonHang = sanPhamDonHang;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items_choose_sp, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);

        SanPham sanPham = (SanPham) getItem(i);
//        Picasso.get().load(sanPham.getHinhAnh()).placeholder(R.drawable.noimage).error(R.drawable.iconerror).into(icon);
        icon.setImageResource(flags[i]);
        names.setGravity(Gravity.CENTER);
        names.setText(sanPhamDonHang[i]);
        return view;
    }
}
