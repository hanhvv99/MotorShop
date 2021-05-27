package com.example.motorshop.activity.guarantee;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.DanhSachSanPhamBaoHanh;
import com.example.motorshop.db.DBManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GuaranteeChitietActivity extends AppCompatActivity {
    EditText EditTextMaBH,EditTextNDBH,EditTextPBH;
    TextView TextViewMaNV,TextViewMaDH,TextViewNgayTao,TextViewTenSP,TextViewTongPBH;
    ImageButton ImageButtonEdit;
    ListView lvGuatantee_CT;
    ArrayList<DanhSachSanPhamBaoHanh> ds;
    DanhSachNoiDungAdapter danhSachNoiDungAdapter;

    DBManager db = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarantee_chitiet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        TextViewTongPBH = findViewById(R.id.TextViewTongPBH);
        ImageButtonEdit = findViewById(R.id.ImageButtonEdit);
    }

    private void addNoiDung() {
        ImageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(GuaranteeChitietActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Thêm Nội Dung")
                .setMessage("Bạn có muốn thêm nội dung và phí BH?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String maBH = EditTextMaBH.getText().toString().trim().toUpperCase();
                        String noiDung = EditTextNDBH.getText().toString().trim();
                        String phiBH = EditTextPBH.getText().toString().trim();
                        //lay du lieu tu intent
                        String maDH = getIntent().getExtras().getString("MADDH");
                        String ngayBH = getIntent().getExtras().getString("NGAYBH");
                        String maNV = getIntent().getExtras().getString("MANV");
                        String tenSP = getIntent().getExtras().getString("TEN_SAN_PHAM");

                        if(EditTextMaBH.getText().toString().trim().toUpperCase().length() > 0 &&
                                EditTextNDBH.getText().toString().trim().length() > 0 &&
                                    EditTextPBH.getText().toString().trim().length() > 0) {

                            Log.d("MABH", maBH);
                            Log.d("NOI DUNG", noiDung);
                            Log.d("PHI BH", phiBH);

                            String maSP = db.get1MaSP(tenSP);
                            Log.d("MA SP", maSP);

                            if(db.setTonTaiMaBH(maBH) == 1){
                                Toast.makeText(getApplicationContext(), "Có mã BH này!"+maSP,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Đã tạo mã BH!"+maSP,
                                        Toast.LENGTH_SHORT).show();

                                db.createPhieuBH(maBH, maDH, ngayBH, maNV);
                                showAdapterNDBH();
                            }

                            //save dữ liệu CTBH
                            if(db.setTonTaiTblXe(maSP) == 1){
                                getData(tenSP, noiDung, Integer.parseInt(phiBH),maBH);
                                Toast.makeText(getApplicationContext(), "Có ma sp Xe này "+maSP,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                getData(tenSP, noiDung, Integer.parseInt(phiBH),maBH);
                                Toast.makeText(getApplicationContext(), "Có ma sp PT này"+maSP,
                                        Toast.LENGTH_SHORT).show();
                            }

                            //reset
                            EditTextNDBH.setText("");
                            String phi = getIntent().getExtras().getString("PHIBH");
                            EditTextPBH.setText(phi);
                            capNhatTongPhiBH(maBH,maSP);

                        } else {
                            Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ ô trống!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
            }
        });
    }

    private void getThongTinChiTiet() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String maDH = bundle.getString("MADDH");
            TextViewMaDH.setText(maDH);

            String maNV = bundle.getString("MANV");
            TextViewMaNV.setText(maNV);

            String tenSP = bundle.getString("TEN_SAN_PHAM");
            TextViewTenSP.setText(tenSP);

            String ngayTao = bundle.getString("NGAYBH");
            TextViewNgayTao.setText(ngayTao);

            String maBH = bundle.getString("MABH");
            EditTextMaBH.setText(maBH);

            String phiBH = bundle.getString("PHIBH");
            EditTextPBH.setText(phiBH);

            Log.d("MADDH", maDH);
            Log.d("MANV", maNV);
            Log.d("TEN_SAN_PHAM", tenSP);
            Log.d("NGAYBH", ngayTao);
            Log.d("MABH", maBH);
            Log.d("PHIBH", phiBH);
        }
    }

    public void getData(String tenSP, String noiDung, int phiBH, String maBH){
        String maSP = db.get1MaSP(tenSP);
        ds.add(new DanhSachSanPhamBaoHanh(tenSP, noiDung, phiBH));
        if(db.setTonTaiTblXe(maSP) == 1) {
            db.createChiTietBHXE(maBH,maSP,noiDung,phiBH);
            ds = db.loadAllNdBhXe(maBH, maSP);
            ds.clear();
        } else {
            db.createChiTietBHPT(maBH,maSP,noiDung,phiBH);
            ds = db.loadAllNdBhPT(maBH, maSP);
            ds.clear();
        }
        loadAdapter(ds);
        capNhatTongPhiBH(maBH,maSP);
    }

    public void showAdapterNDBH(){
        String maBH = getIntent().getExtras().getString("MABH");
        String tenSP = getIntent().getExtras().getString("TEN_SAN_PHAM");
        String maSP = db.get1MaSP(tenSP);
        if(db.setTonTaiTblXe(maSP) == 1) {
            ds = db.loadAllNdBhXe(maBH, maSP);
        } else {
            ds = db.loadAllNdBhPT(maBH, maSP);
        }
        loadAdapter(ds);
        capNhatTongPhiBH(maBH,maSP);
    }

    public void capNhatTongPhiBH(String maBH, String maSP){
        int tongPhi = db.getTongPhi(maBH,maSP);

        DecimalFormat format = new DecimalFormat("###,###,###");
        String strTongPhi = format.format(tongPhi)+" VNĐ";
        TextViewTongPBH.setText(strTongPhi);
    }

    public void loadAdapter(ArrayList<DanhSachSanPhamBaoHanh> ds){
        danhSachNoiDungAdapter = new DanhSachNoiDungAdapter(this, R.layout.item_list_chitiet_baohanh,ds);
        lvGuatantee_CT.setAdapter(danhSachNoiDungAdapter);
        danhSachNoiDungAdapter.notifyDataSetChanged();

        lvGuatantee_CT.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                delNoiDung(position);
                return false;
            }
        });
    }

    public void delNoiDung(int pos){
        new AlertDialog.Builder(this)
            .setTitle("Xóa Nội Dung")
            .setMessage("Bạn có muốn xóa nội dung này không?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String maBH = getIntent().getExtras().getString("MABH");
                    String tenSP = getIntent().getExtras().getString("TEN_SAN_PHAM");
                    String maSP = db.get1MaSP(tenSP);
                    if(db.setTonTaiTblXe(maSP) == 1) {
                        db.deleteNoiDungBHX(pos,ds);
                        ds.remove(pos);
                        danhSachNoiDungAdapter.notifyDataSetChanged();
                        showAdapterNDBH();
                    } else {
                        db.deleteNoiDungBHPT(pos,ds);
                        ds.remove(pos);
                        danhSachNoiDungAdapter.notifyDataSetChanged();
                        showAdapterNDBH();
                    }
                }
            })
            .setNegativeButton("Cancel",null)
            .show();

    }
}