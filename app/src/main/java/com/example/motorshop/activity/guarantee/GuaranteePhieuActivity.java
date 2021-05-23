package com.example.motorshop.activity.guarantee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.ChiTietBaoHanh;
import com.example.motorshop.datasrc.DanhSachSanPhamBaoHanh;
import com.example.motorshop.db.DBManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GuaranteePhieuActivity extends AppCompatActivity {
    private FloatingActionButton btnActionAddP;
    private ListView lvGuatanteeP;
    private TextView TextViewMaDH;
    private ImageView imgV_xe, imgV_phutung;
    private Spinner spinnerTenSP;
    DBManager db = new DBManager(this);
    private GuaranteePhieuAdapter guaranteeChitietAdapter;
    private ArrayList<ChiTietBaoHanh> chiTietBaoHanhs;
    private ArrayList<DanhSachSanPhamBaoHanh> danhSachSanPhamBaoHanhs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarantee_phieu);

        setControl();

        btnActionAddChiTiet();

        getSPcuaDHlenSpinner();
        
        setEvent();

        //SHOW CÁC PHIẾU BH
        showAdapterPhieuBH();
        db.loadAllPhieuBaoHanh();
    }

    private void setControl() {
        lvGuatanteeP = findViewById(R.id.lvGuatantee_P);
        TextViewMaDH = findViewById(R.id.TextViewMaDH);
        imgV_xe = findViewById(R.id.ImageViewXe);
        imgV_phutung = findViewById(R.id.ImageViewPhuTung);
        spinnerTenSP = findViewById(R.id.SpinnerSP);
        btnActionAddP = findViewById(R.id.btnAddBH_P);
        chiTietBaoHanhs = new ArrayList<ChiTietBaoHanh>();
        danhSachSanPhamBaoHanhs = new ArrayList<DanhSachSanPhamBaoHanh>();
    }

    private void setEvent() {
        imgV_xe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuaranteePhieuActivity.this,"Lọc:xe",Toast.LENGTH_SHORT).show();
                imgV_xe.setBackgroundColor(getResources().getColor(R.color.teal_200));
                imgV_phutung.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
        imgV_phutung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuaranteePhieuActivity.this,"Lọc:phụ tùng",Toast.LENGTH_SHORT).show();
                imgV_phutung.setBackgroundColor(getResources().getColor(R.color.teal_200));
                imgV_xe.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
    }

    private void btnActionAddChiTiet() {
        btnActionAddP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(GuaranteePhieuActivity.this)
                //set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
                //set title
                .setTitle("Phiếu Bảo Hành")
                //set message
                .setMessage("Bạn có muốn thêm phiếu?")
                //set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        Intent intent = new Intent(getApplicationContext(), GuaranteeChitietActivity.class);
                        startActivity(intent);
                    }
                })
                //set negative button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        return;
                    }
                })
                .show();
            }
        });
    }

    public void getSPcuaDHlenSpinner(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String maDH = bundle.getString("MADH", "");
            TextViewMaDH.setText(maDH);
            List<String> labelSP = db.get1Label(maDH);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labelSP);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinnerTenSP.setAdapter(dataAdapter);
            Log.d("Cac SP cua "+maDH, String.valueOf(labelSP));

            spinnerTenSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //get values
                    Object item = parent.getItemAtPosition(position);

                    // Get the state's capital from this row in the database.
                    Intent intent = new Intent(GuaranteePhieuActivity.this,GuaranteeChitietActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("MADH", maDH);

                    String maNV = "NV001";
                    bundle.putString("MANV", maNV);

                    String tenSP = String.valueOf(item);
                    bundle.putString("TEN_SAN_PHAM", tenSP);

                    String ngayTao = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());//HH:mm:ss
                    bundle.putString("NGAYTAO", ngayTao);

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


    public void showAdapterPhieuBH(){
        if(guaranteeChitietAdapter == null){
            guaranteeChitietAdapter = new GuaranteePhieuAdapter(this,R.layout.item_list_phieu_baohanh, chiTietBaoHanhs);
            lvGuatanteeP.setAdapter(guaranteeChitietAdapter);
        } else{
            guaranteeChitietAdapter.notifyDataSetChanged();
            lvGuatanteeP.setSelection(guaranteeChitietAdapter.getCount()-1);
            lvGuatanteeP.setVisibility(View.GONE);
        }
    }
}