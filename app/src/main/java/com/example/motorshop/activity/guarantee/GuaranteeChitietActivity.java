package com.example.motorshop.activity.guarantee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.DanhSachSanPhamBaoHanh;
import com.example.motorshop.db.DBManager;

import java.util.ArrayList;

public class GuaranteeChitietActivity extends AppCompatActivity {
    private EditText EditTextMaBH,EditTextNDBH,EditTextPBH;
    private TextView TextViewMaNV,TextViewMaDH,TextViewNgayTao,TextViewTenSP;
    private ImageButton ImageButtonSave;
    private ListView lvGuatantee_CT;
    private ArrayList<DanhSachSanPhamBaoHanh> ds;
    private GuaranteeChitietAdapter guaranteeChitietAdapter;
    private DanhSachNoiDungAdapter danhSachNoiDungAdapter;
    private String tenSP, maDH, maBH, ngayTao, noiDung, maNV;
    private int phiBH;
    DBManager db = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarantee_chitiet);

        setControl();

        getThongTinChiTiet();

        addNoiDung();

        showAdapterNDBH();

    }

    private void setControl() {
        lvGuatantee_CT = findViewById(R.id.lvGuatantee_CT);
        ds = new ArrayList<DanhSachSanPhamBaoHanh>();
        EditTextMaBH = findViewById(R.id.EditTextMaBH);
        EditTextNDBH = findViewById(R.id.EditTextNDBH);
        EditTextPBH = findViewById(R.id.EditTextPBH);
        TextViewMaNV = findViewById(R.id.TextViewMaNV);
        TextViewMaDH = findViewById(R.id.TextViewMaDH);
        TextViewNgayTao = findViewById(R.id.TextViewNgayTao);
        TextViewTenSP = findViewById(R.id.TextViewTenSP);
        ImageButtonSave = findViewById(R.id.ImageButtonSave);
    }

    private void addNoiDung() {
        ImageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(GuaranteeChitietActivity.this)
                //set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
                //set title
                .setTitle("Thêm Nội Dung")
                //set message
                .setMessage("Bạn có muốn thêm nội dung và phí BH?")
                //set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        maBH = EditTextMaBH.getText().toString().trim().toUpperCase();
                        noiDung = EditTextNDBH.getText().toString().trim();
                        phiBH = Integer.parseInt(EditTextPBH.getText().toString().trim());

                        if(String.valueOf(phiBH).trim().length() > 0 && maBH.trim().length() > 0 && noiDung.trim().length() > 0) {
                            getData(tenSP, noiDung, phiBH);

                            Log.d("MABH", maBH);
                            Log.d("NOI DUNG", noiDung);
                            Log.d("PHI BH", String.valueOf(phiBH));

                            //save dữ liệu vào BH
                            db.createPhieuBH(maBH, maDH, ngayTao, maNV);

                            //lấy mã để lưu dữ liệu
                            String maSP = db.get1MaSP(tenSP);

                            //save dữ liệu CTBH
                            if(db.setTonTaiTblXe(maSP) == 1){
                                db.createChiTietBHXE(maBH,maSP,noiDung,phiBH);
                                ds.clear();
                                ds.addAll(db.loadAllNdBhXe(maBH, maSP));
                                showAdapterNDBH();
                                Toast.makeText(getApplicationContext(), "Có ma sp Xe này "+maSP,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                db.createChiTietBHPT(maBH,maSP,noiDung,phiBH);
                                ds.clear();
                                ds.addAll(db.loadAllNdBhPT(maBH, maSP));
                                showAdapterNDBH();
                                Toast.makeText(getApplicationContext(), "Có ma sp PT này"+maSP,
                                        Toast.LENGTH_SHORT).show();
                            }

                            //sau khi luu xong
                            //reset
                            EditTextMaBH.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                    if ((event.getAction() == KeyEvent.ACTION_DOWN)
                                            && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                                      block edittext
                                    }
                                    return false;
                                }
                            });
                            EditTextNDBH.setText("");
                            EditTextPBH.setText("");

                        } else {
                            Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ ô trống!",
                                    Toast.LENGTH_SHORT).show();
                        }
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

    private void getThongTinChiTiet() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            maDH = bundle.getString("MADH", "");
            TextViewMaDH.setText(maDH);

            maNV = bundle.getString("MANV", "");
            TextViewMaNV.setText(maNV);

            tenSP = bundle.getString("TEN_SAN_PHAM", "");
            TextViewTenSP.setText(tenSP);

            ngayTao = bundle.getString("NGAYTAO", "");
            TextViewNgayTao.setText(ngayTao);

            //Dieu kien cho phi san pham

        }
    }

    public void getData(String tensp, String noiDung, int phiBH){
        ds.add(new DanhSachSanPhamBaoHanh(tensp, noiDung, phiBH));
        guaranteeChitietAdapter = new GuaranteeChitietAdapter(GuaranteeChitietActivity.this, ds);

        lvGuatantee_CT.setAdapter(guaranteeChitietAdapter);
        guaranteeChitietAdapter.notifyDataSetChanged();
    }

    public void showAdapterNDBH(){
        if(danhSachNoiDungAdapter == null){
            danhSachNoiDungAdapter = new DanhSachNoiDungAdapter(this,R.layout.item_list_chitiet_baohanh,ds);
            lvGuatantee_CT.setAdapter(danhSachNoiDungAdapter);
        } else{
            danhSachNoiDungAdapter.notifyDataSetChanged();
            lvGuatantee_CT.setSelection(danhSachNoiDungAdapter.getCount()-1);
        }
    }
}