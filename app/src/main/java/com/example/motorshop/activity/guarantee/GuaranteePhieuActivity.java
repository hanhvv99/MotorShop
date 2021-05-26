package com.example.motorshop.activity.guarantee;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.motorshop.activity.R;
import com.example.motorshop.db.DBManager;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuaranteePhieuActivity extends AppCompatActivity {
    private ListView lvGuatanteeP;
    private TextView TextViewMaDH, all, TextViewTongPhieuBH;
    private ImageView imgV_xe, imgV_phutung;
    private Spinner spinnerTenSP;
    DBManager db = new DBManager(this);
    private PhieuBaoHanhTempAdapter phieuBaoHanhTempAdapter;
    ArrayList<PhieuBaoHanhTemp> phieuBaoHanhTemps;
    String maDH ;//= getIntent().getExtras().getString("MADH");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarantee_phieu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setControl();

        getSPcuaDHlenSpinner();

        getSPcuaDHlenListview();

        setEvent();

        capNhatTongPhieuBH();
    }

    private void setControl() {
        lvGuatanteeP = findViewById(R.id.lvGuatantee_P);
        TextViewMaDH = findViewById(R.id.TextViewMaDH);
        TextViewTongPhieuBH = findViewById(R.id.TextViewTongPhieuBH);
        all = findViewById(R.id.all);
        imgV_xe = findViewById(R.id.ImageViewXe);
        imgV_phutung = findViewById(R.id.ImageViewPhuTung);
        spinnerTenSP = findViewById(R.id.SpinnerSP);
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

                phieuBaoHanhTemps = db.loadAllPhieuXe(maDH);
                phieuBaoHanhTempAdapter = new PhieuBaoHanhTempAdapter(GuaranteePhieuActivity.this, R.layout.item_list_phieu_baohanh, phieuBaoHanhTemps);
                lvGuatanteeP.setAdapter(phieuBaoHanhTempAdapter);
                phieuBaoHanhTempAdapter.notifyDataSetChanged();
            }
        });
        imgV_phutung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuaranteePhieuActivity.this, "Lọc:phụ tùng", Toast.LENGTH_SHORT).show();
                imgV_phutung.setBackgroundColor(getResources().getColor(R.color.teal_200));
                imgV_xe.setBackgroundColor(getResources().getColor(R.color.white));

                phieuBaoHanhTemps = db.loadAllPhieuPT(maDH);
                phieuBaoHanhTempAdapter = new PhieuBaoHanhTempAdapter(GuaranteePhieuActivity.this, R.layout.item_list_phieu_baohanh, phieuBaoHanhTemps);
                lvGuatanteeP.setAdapter(phieuBaoHanhTempAdapter);
                phieuBaoHanhTempAdapter.notifyDataSetChanged();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuaranteePhieuActivity.this, "Lọc:all", Toast.LENGTH_SHORT).show();
                imgV_xe.setBackgroundColor(getResources().getColor(R.color.white));
                imgV_phutung.setBackgroundColor(getResources().getColor(R.color.white));

                phieuBaoHanhTemps = db.loadAllPhieuBH(maDH);
                phieuBaoHanhTempAdapter = new PhieuBaoHanhTempAdapter(GuaranteePhieuActivity.this, R.layout.item_list_phieu_baohanh, phieuBaoHanhTemps);
                lvGuatanteeP.setAdapter(phieuBaoHanhTempAdapter);
                phieuBaoHanhTempAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getSPcuaDHlenSpinner() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            maDH = bundle.getString("MADH");
            TextViewMaDH.setText(maDH);
            List<String> labelSP = db.get1Label(maDH);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelSP);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinnerTenSP.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();

            spinnerTenSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //get values
                    Object item = parent.getItemAtPosition(position);

                    String maNV = "NV001";

                    String tenSP = String.valueOf(item);

                    String ngayBH = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());//HH:mm:ss

                    String maSP = db.get1MaSP(tenSP);
                    String maBH = "";
                    String phiBH = dkPhiBH(ngayBH,maSP);

                    startChiTiet(item,maDH,maNV,tenSP,ngayBH,maBH,phiBH);
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    public void getSPcuaDHlenListview() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            maDH = bundle.getString("MADH");
            TextViewMaDH.setText(maDH);

            phieuBaoHanhTemps = db.loadAllPhieuBH(maDH);
            phieuBaoHanhTempAdapter = new PhieuBaoHanhTempAdapter(this, R.layout.item_list_phieu_baohanh, phieuBaoHanhTemps);
            lvGuatanteeP.setAdapter(phieuBaoHanhTempAdapter);
            phieuBaoHanhTempAdapter.notifyDataSetChanged();

            lvGuatanteeP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //get values
                    Object item = parent.getItemAtPosition(position);
                    String[] tachItem = String.valueOf(item).split(",");
                    //Mã bh
                    String maBH = tachItem[0];

                    String maNV = "NV001";

                    String tenSP = tachItem[1];

                    String ngayBH = tachItem[2];

                    Log.d("ID", String.valueOf(id));
                    Log.d("MaBH", maBH);
                    Log.d("tenSP", tenSP);
                    Log.d("ngayBH", ngayBH);

                    String maSP = db.get1MaSP(tenSP);
                    String phiBH = dkPhiBH(ngayBH,maSP);

                    startChiTiet(item,maDH,maNV,tenSP,ngayBH,maBH,phiBH);
                }

                public void onNothingSelected(AdapterView<?> parent) { }
            });

            lvGuatanteeP.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    Object item = parent.getItemAtPosition(position);

                    String[] tachItem = String.valueOf(item).split(",");
                    String maBH = tachItem[0];
                    Log.d("ID", String.valueOf(id));
                    Log.d("MaBH", maBH);

                    //lấy tensp
                    String tenSP = tachItem[0];

                    //lấy mã
                    String maSP = db.get1MaSP(tenSP);

                    delPhieu(position,maBH,maSP);
                    return true;
                }
            });
        }
    }

    public String dkPhiBH(String ngayBH, String maSP) {
        String phi = null;

        DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date currentDate = new Date();
        Date date1 = null;
        Date date2 = null;

        try {
            // calculating the difference b/w startDate and endDate
            String ngayDH = db.get1NgayDH(maDH);
            ngayBH = simpleDateFormat.format(currentDate);

            date1 = simpleDateFormat.parse(ngayDH);
            date2 = simpleDateFormat.parse(ngayBH);

            long getDiff = date2.getTime() - date1.getTime();

            // using TimeUnit class from java.util.concurrent package
            long getMonthsDiff = getDiff / (12 * 24 * 60 * 60 * 1000);//1thang*1ngay*1tieng*1phut*1giay
            Log.d("hết hạn", String.valueOf(getMonthsDiff));

            long han = Long.parseLong(db.get1HanBH(maDH, maSP));
            Log.d("hạn bảo hành", String.valueOf(han));

            if (getMonthsDiff <= han) {//lấy hạn bảo hành
                phi = "0";
            } else {
                phi = "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return phi;
    }

    public void startChiTiet(Object item, String maDH, String maNV, String tenSP, String ngayBH, String maBH, String phiBH){

        // Get the state's capital from this row in the database.
        Intent intent = new Intent(this, GuaranteeChitietActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("MADDH", maDH);

        bundle.putString("MANV", maNV);

        bundle.putString("TEN_SAN_PHAM", tenSP);

        bundle.putString("NGAYBH", ngayBH);

        bundle.putString("MABH", maBH);

        bundle.putString("PHIBH", phiBH);

        intent.putExtras(bundle);
        startActivity(intent);

        Log.d("MADDH", maDH);
        Log.d("MANV", maNV);
        Log.d("TEN_SAN_PHAM", tenSP);
        Log.d("NGAYBH", ngayBH);
        Log.d("MABH", maBH);
        Log.d("PHIBH", phiBH);
        Toast.makeText(GuaranteePhieuActivity.this, "San pham:" + item, Toast.LENGTH_SHORT).show();
    }

    public void loadAdapter(){
        maDH = getIntent().getExtras().getString("MADH");
        TextViewMaDH.setText(maDH);

        phieuBaoHanhTemps = db.loadAllPhieuBH(maDH);
        phieuBaoHanhTempAdapter = new PhieuBaoHanhTempAdapter(this, R.layout.item_list_phieu_baohanh, phieuBaoHanhTemps);
        lvGuatanteeP.setAdapter(phieuBaoHanhTempAdapter);
        phieuBaoHanhTempAdapter.notifyDataSetChanged();

    }

    public void delPhieu(int pos, String maBH, String maSP){
        new AlertDialog.Builder(this)
                .setTitle("Xóa Phiếu")
                .setMessage("Bạn có muốn xóa phiếu này không?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(db.setTonTaiTblXe(maSP) == 1) {
                            db.deleteAllNoiDungBHX(pos,phieuBaoHanhTemps);
                            db.deletePhieuBH(pos,phieuBaoHanhTemps);
                            phieuBaoHanhTemps.remove(pos);
                            phieuBaoHanhTempAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Xóa Phiếu BH "+maSP+" thành công!",
                                        Toast.LENGTH_SHORT).show();
                        } else {
                            db.deleteAllNoiDungBHPT(pos,phieuBaoHanhTemps);
                            db.deletePhieuBH(pos,phieuBaoHanhTemps);
                            phieuBaoHanhTemps.remove(pos);
                            phieuBaoHanhTempAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Xóa Phiếu BH "+maSP+" thành công!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();

    }

    public void capNhatTongPhieuBH(){
        String tongPhi = String.valueOf(db.getTongPhieu());
        TextViewTongPhieuBH.setText(tongPhi);
    }
}