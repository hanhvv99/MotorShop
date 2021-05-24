package com.example.motorshop.activity.guarantee;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.ChiTietBaoHanh;
import com.example.motorshop.db.DBManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GuaranteePhieuActivity extends AppCompatActivity {
    private ListView lvGuatanteeP;
    private TextView TextViewMaDH, loc;
    private ImageView imgV_xe, imgV_phutung;
    private Spinner spinnerTenSP;
    DBManager db = new DBManager(this);
    private GuaranteePhieuAdapter guaranteePhieuAdapter;
    private PhieuBaoHanhTempAdapter phieuBaoHanhTempAdapter;
    ArrayList<ChiTietBaoHanh> chiTietBaoHanhs;
    ArrayList<PhieuBaoHanhTemp> phieuBaoHanhTemps;
    private String tenSP, maDH, maBH, ngayTao, maNV, maSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarantee_phieu);

        setControl();

        getSPcuaDHlenSpinner();

//        getSPcuaDHlenListview();

        showAdapterPBH();

        setEvent();
    }

    private void setControl() {
        lvGuatanteeP = findViewById(R.id.lvGuatantee_P);
        TextViewMaDH = findViewById(R.id.TextViewMaDH);
        loc = findViewById(R.id.loc);
        imgV_xe = findViewById(R.id.ImageViewXe);
        imgV_phutung = findViewById(R.id.ImageViewPhuTung);
        spinnerTenSP = findViewById(R.id.SpinnerSP);
        chiTietBaoHanhs = new ArrayList<ChiTietBaoHanh>();
        phieuBaoHanhTemps = new ArrayList<PhieuBaoHanhTemp>();
        phieuBaoHanhTemps = db.loadAllPhieuBH(maDH);
    }

    private void setEvent() {
        imgV_xe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuaranteePhieuActivity.this, "Lọc:xe", Toast.LENGTH_SHORT).show();
                imgV_xe.setBackgroundColor(getResources().getColor(R.color.teal_200));
                imgV_phutung.setBackgroundColor(getResources().getColor(R.color.white));

                ArrayList<ChiTietBaoHanh> dsct = db.loadAllPhieuXe(maDH);
                guaranteePhieuAdapter = new GuaranteePhieuAdapter(GuaranteePhieuActivity.this, R.layout.item_list_phieu_baohanh, dsct);
                lvGuatanteeP.setAdapter(guaranteePhieuAdapter);
                guaranteePhieuAdapter.notifyDataSetChanged();
            }
        });
        imgV_phutung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuaranteePhieuActivity.this, "Lọc:phụ tùng", Toast.LENGTH_SHORT).show();
                imgV_phutung.setBackgroundColor(getResources().getColor(R.color.teal_200));
                imgV_xe.setBackgroundColor(getResources().getColor(R.color.white));

                ArrayList<ChiTietBaoHanh> dsct = db.loadAllPhieuPT(maDH);
                guaranteePhieuAdapter = new GuaranteePhieuAdapter(GuaranteePhieuActivity.this, R.layout.item_list_phieu_baohanh, dsct);
                lvGuatanteeP.setAdapter(guaranteePhieuAdapter);
                guaranteePhieuAdapter.notifyDataSetChanged();
            }
        });
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuaranteePhieuActivity.this, "Lọc:all", Toast.LENGTH_SHORT).show();
                imgV_xe.setBackgroundColor(getResources().getColor(R.color.white));
                imgV_phutung.setBackgroundColor(getResources().getColor(R.color.white));

                phieuBaoHanhTemps=db.loadAllPhieuBH(maDH);
                phieuBaoHanhTempAdapter = new PhieuBaoHanhTempAdapter(GuaranteePhieuActivity.this, R.layout.item_list_phieu_baohanh, phieuBaoHanhTemps);

                lvGuatanteeP.setAdapter(phieuBaoHanhTempAdapter);
                phieuBaoHanhTempAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getSPcuaDHlenSpinner(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            maDH = bundle.getString("MADH");
            TextViewMaDH.setText(maDH);
            List<String> labelSP = db.get1Label(maDH);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labelSP);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinnerTenSP.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();

            spinnerTenSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //get values
                    Object item = parent.getItemAtPosition(position);

                    // Get the state's capital from this row in the database.
                    Intent intent = new Intent(GuaranteePhieuActivity.this,GuaranteeChitietActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("MADH", maDH);

                    maNV = "NV001";
                    bundle.putString("MANV", maNV);

                    tenSP = String.valueOf(item);
                    bundle.putString("TEN_SAN_PHAM", tenSP);

                    ngayTao = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());//HH:mm:ss
                    bundle.putString("NGAYTAO", ngayTao);

                    maSP = db.get1MaSP(tenSP);
                    if(db.setTonTaiMaBHtrongCTBH(maSP) == 1){
                        maBH = db.get1MaBH(maSP);
                        bundle.putString("MABH", maBH);
                    } else {
                        maBH = "";
                        bundle.putString("MABH", maBH);
                    }

                    intent.putExtras(bundle);
                    startActivity(intent);

                    Log.d("MADH", maDH);
                    Log.d("MANV", maNV);
                    Log.d("TEN_SAN_PHAM", tenSP);
                    Log.d("NGAYTAO", ngayTao);
                    Toast.makeText(GuaranteePhieuActivity.this,"San pham:"+item,Toast.LENGTH_SHORT).show();
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    public void getSPcuaDHlenListview(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            maDH = bundle.getString("MADH");
            TextViewMaDH.setText(maDH);

            phieuBaoHanhTemps=db.loadAllPhieuBH(maDH);
            phieuBaoHanhTempAdapter = new PhieuBaoHanhTempAdapter(this, R.layout.item_list_phieu_baohanh,phieuBaoHanhTemps);
            lvGuatanteeP.setAdapter(phieuBaoHanhTempAdapter);
            phieuBaoHanhTempAdapter.notifyDataSetChanged();

            lvGuatanteeP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //get values
                    Object item = parent.getItemAtPosition(position);

                    // Get the state's capital from this row in the database.
                    Intent intent = new Intent(GuaranteePhieuActivity.this,GuaranteeChitietActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("MADH", maDH);

                    maNV = "NV001";
                    bundle.putString("MANV", maNV);

                    tenSP = String.valueOf(item);
                    bundle.putString("TEN_SAN_PHAM", tenSP);

                    ngayTao = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());//HH:mm:ss
                    bundle.putString("NGAYTAO", ngayTao);

                    maSP = db.get1MaSP(tenSP);
                    if(db.setTonTaiMaBHtrongCTBH(maSP) == 1){
                        maBH = db.get1MaBH(maSP);
                        bundle.putString("MABH", maBH);
                    } else {
                        maBH = "";
                        bundle.putString("MABH", maBH);
                    }

                    intent.putExtras(bundle);
                    startActivity(intent);

                    Log.d("MADH", maDH);
                    Log.d("MANV", maNV);
                    Log.d("TEN_SAN_PHAM", tenSP);
                    Log.d("NGAYTAO", ngayTao);
                    Toast.makeText(GuaranteePhieuActivity.this,"San pham:"+item,Toast.LENGTH_SHORT).show();
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    public void showAdapterPBH(){
        if(phieuBaoHanhTempAdapter == null){
            phieuBaoHanhTemps=db.loadAllPhieuBH(maDH);
            phieuBaoHanhTempAdapter = new PhieuBaoHanhTempAdapter(this, R.layout.item_list_phieu_baohanh,phieuBaoHanhTemps);
            lvGuatanteeP.setAdapter(phieuBaoHanhTempAdapter);
        } else{
            phieuBaoHanhTempAdapter.notifyDataSetChanged();
            lvGuatanteeP.setSelection(phieuBaoHanhTempAdapter.getCount()-1);
        }
    }
}