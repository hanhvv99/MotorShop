package com.example.motorshop.activity.guarantee;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.BaoHanh;
import com.example.motorshop.datasrc.DanhSachSanPhamBaoHanh;
import com.example.motorshop.db.DBManager;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GuaranteeChitietActivity extends AppCompatActivity {
    private EditText EditTextMaBH,EditTextNDBH,EditTextPBH;
    private TextView TextViewMaNV,TextViewMaDH,TextViewNgayTao,TextViewTenSP,TextViewTongPBH;
    private ImageButton ImageButtonEdit;
    private ListView lvGuatantee_CT;
    private ArrayList<DanhSachSanPhamBaoHanh> ds;
    private DanhSachNoiDungAdapter danhSachNoiDungAdapter;
    private String tenSP, maDH, maBH, ngayTao, noiDung, maNV, maSP, phiBH;

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
                        phiBH = EditTextPBH.getText().toString().trim();

                        if(EditTextMaBH.getText().toString().trim().toUpperCase().length() > 0 &&
                                EditTextNDBH.getText().toString().trim().length() > 0 &&
                                    EditTextPBH.getText().toString().trim().length() > 0) {
//                            if(checkNhapMa(maBH) == true){
                                Log.d("MABH", maBH);
                                Log.d("NOI DUNG", noiDung);
                                Log.d("PHI BH", phiBH);

                                if(db.setTonTaiMaBH(maBH) == 1){
                                    Toast.makeText(getApplicationContext(), "Có mã BH này!"+maSP,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Đã tạo mã BH!"+maSP,
                                            Toast.LENGTH_SHORT).show();
                                    //save dữ liệu vào BH
                                    db.createPhieuBH(maBH, maDH, ngayTao, maNV);
                                }

//                            lấy mã để lưu dữ liệu
                                maSP = db.get1MaSP(tenSP);
                                Log.d("MA SP", maSP);

                                getData(tenSP, noiDung, Integer.parseInt(phiBH));

                                //save dữ liệu CTBH
                                if(db.setTonTaiTblXe(maSP) == 1){
                                    db.createChiTietBHXE(maBH,maSP,noiDung,Integer.parseInt(phiBH));
                                    ds.clear();
                                    ds.addAll(db.loadAllNdBhXe(maBH, maSP));
                                    Toast.makeText(getApplicationContext(), "Có ma sp Xe này "+maSP,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    db.createChiTietBHPT(maBH,maSP,noiDung,Integer.parseInt(phiBH));
                                    ds.clear();
                                    ds.addAll(db.loadAllNdBhPT(maBH, maSP));
                                    Toast.makeText(getApplicationContext(), "Có ma sp PT này"+maSP,
                                            Toast.LENGTH_SHORT).show();
                                }

                                //reset
                                EditTextNDBH.setText("");
                                EditTextPBH.setText("");

                                showAdapterNDBH();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Nhập theo định dạng BH{SỐ}!",
//                                        Toast.LENGTH_SHORT).show();
//                                EditTextMaBH.setText("BH");
//                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ ô trống!",
                                    Toast.LENGTH_SHORT).show();
                            return;
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
            maDH = bundle.getString("MADDH");
            TextViewMaDH.setText(maDH);

            maNV = bundle.getString("MANV");
            TextViewMaNV.setText(maNV);

            tenSP = bundle.getString("TEN_SAN_PHAM");
            TextViewTenSP.setText(tenSP);

            ngayTao = bundle.getString("NGAYBH");
            TextViewNgayTao.setText(ngayTao);

            maBH = bundle.getString("MABH");
            EditTextMaBH.setText(maBH);

            phiBH = bundle.getString("PHIBH");
            EditTextPBH.setText(phiBH);
        }
    }

    public void getData(String tensp, String noiDung, int phiBH){
        maBH = getIntent().getExtras().getString("MABH");
        tenSP = getIntent().getExtras().getString("TEN_SAN_PHAM");
        maSP = db.get1MaSP(tenSP);
        ds.add(new DanhSachSanPhamBaoHanh(tensp, noiDung, phiBH));
        if(db.setTonTaiTblXe(maSP) == 1) {
            ds = db.loadAllNdBhXe(maBH, maSP);
        } else {
            ds = db.loadAllNdBhPT(maBH, maSP);
        }
        loadAdapter(ds);
    }

    public void showAdapterNDBH(){
        maBH = getIntent().getExtras().getString("MABH");
        tenSP = getIntent().getExtras().getString("TEN_SAN_PHAM");
        maSP = db.get1MaSP(tenSP);
        if(db.setTonTaiTblXe(maSP) == 1) {
            ds = db.loadAllNdBhXe(maBH, maSP);
        } else {
            ds = db.loadAllNdBhPT(maBH, maSP);
        }
        loadAdapter(ds);
        capNhatTongPhiBH(maBH,maSP);
    }

    public void checkMA(String inputMa){
        if(db.setTonTaiMaBH(inputMa) == 1){
            Toast.makeText(getApplicationContext(), "Mã tồn tại mời nhập mã khác!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkNhapMa(String inputMa){
        String regex = "^BH+[0-9]{1,}$";
        try {
            if(!Pattern.matches(regex,inputMa)){
                return false;
            } else return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void capNhatTongPhiBH(String maBH, String maSP){
        maBH = getIntent().getExtras().getString("MABH");
        tenSP = getIntent().getExtras().getString("TEN_SAN_PHAM");
        maSP = db.get1MaSP(tenSP);
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
                    maBH = getIntent().getExtras().getString("MABH");
                    tenSP = getIntent().getExtras().getString("TEN_SAN_PHAM");
                    maSP = db.get1MaSP(tenSP);
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