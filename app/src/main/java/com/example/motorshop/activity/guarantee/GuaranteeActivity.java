package com.example.motorshop.activity.guarantee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.ChiTietBaoHanh;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.arlib.floatingsearchview.FloatingSearchView;

import java.util.ArrayList;

public class GuaranteeActivity extends AppCompatActivity {
    ImageView imgv_xe, imgv_phutung;
    FloatingSearchView floating_search_view;
    TextView txt_mabh, txt_manv,txt_cmnd, txt_tenkh, txt_ten_loai;
    ListView lvGuatantee;
    FloatingActionButton btnAddBH;
    ArrayList<ChiTietBaoHanh> chiTietBaoHanhs =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarantee);

        setControl();
        setEvent();
    }

    private void setEvent() {
        btnAddBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(GuaranteeActivity.this, CreateGuarantee.class);
                startActivity(intt);
            }
        });

        imgv_xe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imgv_phutung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setControl() {
        imgv_xe = findViewById(R.id.imgV_xe);
        imgv_phutung = findViewById(R.id.imgV_phu_tung);
        floating_search_view = findViewById(R.id.floating_search_view);
        txt_mabh = findViewById(R.id.edit_mabh);
        txt_manv = findViewById(R.id.edit_nvbh);
        txt_cmnd = findViewById(R.id.edit_cmnd_kh);
        txt_tenkh = findViewById(R.id.edit_ten_kh);
        txt_ten_loai = findViewById(R.id.tv_ten_loai_bh);
        lvGuatantee = findViewById(R.id.lvGuatantee);
        btnAddBH = findViewById(R.id.btnAddBH);
    }



    public void deleteGuarantee(ChiTietBaoHanh chiTietBaoHanh) {

    }

    public void loadGuarantee() {

    }
}