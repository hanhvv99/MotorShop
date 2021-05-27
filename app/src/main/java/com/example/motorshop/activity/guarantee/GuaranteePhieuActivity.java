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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuaranteePhieuActivity extends AppCompatActivity {
    private ListView lvGuatanteeP;
    private TextView TextViewMaDH, all;
    private ImageView imgV_xe, imgV_phutung;
    private Spinner spinnerTenSP;
    DBManager db = new DBManager(this);
    private PhieuBaoHanhTempAdapter phieuBaoHanhTempAdapter;
    ArrayList<PhieuBaoHanhTemp> phieuBaoHanhTemps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarantee_phieu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setControl();

        getSPcuaDHlenSpinner();

        getSPcuaDHlenListview();

        setEvent();

        loadAllAdapter();
    }

    private void setControl() {
        lvGuatanteeP = findViewById(R.id.lvGuatantee_P);
        TextViewMaDH = findViewById(R.id.TextViewMaDH);
        all = findViewById(R.id.all);
        imgV_xe = findViewById(R.id.ImageViewXe);
        imgV_phutung = findViewById(R.id.ImageViewPhuTung);
        spinnerTenSP = findViewById(R.id.SpinnerSP);
        phieuBaoHanhTemps = new ArrayList<PhieuBaoHanhTemp>();
        String maDH = getIntent().getExtras().getString("MADH");
        phieuBaoHanhTemps = db.loadAllPhieuBH(maDH);
    }

    private void setEvent() {
        imgV_xe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuaranteePhieuActivity.this, "Lọc:xe", Toast.LENGTH_SHORT).show();
                imgV_xe.setBackgroundColor(getResources().getColor(R.color.teal_200));
                imgV_phutung.setBackgroundColor(getResources().getColor(R.color.white));

                String maDH = getIntent().getExtras().getString("MADH");
                phieuBaoHanhTemps = db.loadAllPhieuXe(maDH);
                loadAdapter(phieuBaoHanhTemps);
            }
        });
        imgV_phutung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuaranteePhieuActivity.this, "Lọc:phụ tùng", Toast.LENGTH_SHORT).show();
                imgV_phutung.setBackgroundColor(getResources().getColor(R.color.teal_200));
                imgV_xe.setBackgroundColor(getResources().getColor(R.color.white));

                String maDH = getIntent().getExtras().getString("MADH");
                phieuBaoHanhTemps = db.loadAllPhieuPT(maDH);
                loadAdapter(phieuBaoHanhTemps);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuaranteePhieuActivity.this, "Lọc:all", Toast.LENGTH_SHORT).show();
                imgV_xe.setBackgroundColor(getResources().getColor(R.color.white));
                imgV_phutung.setBackgroundColor(getResources().getColor(R.color.white));

                String maDH = getIntent().getExtras().getString("MADH");
                phieuBaoHanhTemps = db.loadAllPhieuBH(maDH);
                loadAdapter(phieuBaoHanhTemps);
            }
        });
    }

    public void getSPcuaDHlenSpinner() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String maDH = bundle.getString("MADH");
            TextViewMaDH.setText(maDH);

            String[] sanPhamDonHang= db.get1LabelNames(maDH);

            Integer flags[] = db.get1LabelImage(maDH);

            SpinnerCustomAdapter spinnerCustomAdapter = new SpinnerCustomAdapter(getApplicationContext(),flags, sanPhamDonHang);
            spinnerTenSP.setAdapter(spinnerCustomAdapter);

            spinnerTenSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = sanPhamDonHang[position];

                    String maNV = "NV001";

                    String tenSP = String.valueOf(item);

                    String ngayBH = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());

                    String maSP = db.get1MaSP(tenSP);

                    String ID = db.getID();
                    String[] tachID = ID.split("BH");
                    String sauKhiTach = tachID[1];
                    int convertID = Integer.parseInt(sauKhiTach);
                    String maBH = "BH0"+(convertID+1);Log.d("MA BH",maBH);

                    String phiBH = dkPhiBH(ngayBH,maSP);

                    startChiTiet(item,maDH,maNV,tenSP,ngayBH,maBH,phiBH);

                    Log.d("Spinner",item);
                    Toast.makeText(getApplicationContext(), item, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void getSPcuaDHlenListview() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String maDH = bundle.getString("MADH");
            TextViewMaDH.setText(maDH);

            phieuBaoHanhTemps = db.loadAllPhieuBH(maDH);
            loadAdapter(phieuBaoHanhTemps);

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

        DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date currentDate = new Date();
        Date date1 = null;
        Date date2 = null;

        try {
            String maDH = getIntent().getExtras().getString("MADH");

            // calculating the difference b/w startDate and endDate
            String ngayDH = db.get1NgayDH(maDH);
            ngayBH = simpleDateFormat.format(currentDate);

            date1 = simpleDateFormat.parse(ngayDH);
            date2 = simpleDateFormat.parse(ngayBH);

            long getDiff = date2.getTime() - date1.getTime();

            // using TimeUnit class from java.util.concurrent package
            long getMonthsDiff = getDiff / (12 * 24 * 60 * 60 * 1000);//1thang*1ngay*1tieng*1phut*1giay
            Log.d("hết hạn", String.valueOf(getMonthsDiff));

            long han = db.get1HanBH(maDH, maSP);
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

    public void loadAllAdapter(){
        String maDH = getIntent().getExtras().getString("MADH");
        TextViewMaDH.setText(maDH);

        phieuBaoHanhTemps = db.loadAllPhieuBH(maDH);

        loadAdapter(phieuBaoHanhTemps);
    }

    public void loadAdapter(ArrayList<PhieuBaoHanhTemp> ds){
        phieuBaoHanhTempAdapter = new PhieuBaoHanhTempAdapter(this, R.layout.item_list_phieu_baohanh, ds);
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("resume","Phieu bao hanh");
        loadAllAdapter();
    }
}