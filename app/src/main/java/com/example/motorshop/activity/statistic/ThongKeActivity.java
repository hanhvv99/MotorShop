package com.example.motorshop.activity.statistic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motorshop.activity.R;
import com.example.motorshop.db.DBManager;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ThongKeActivity extends AppCompatActivity {
    private TextView TextViewTongSLbanduoc, TextViewTongGiabanduoc;
    private EditText editTextDateTu, editTextDateDen;
    private ImageView imageViewDateTu, imageViewDateDen, searchTK;
    private CheckBox checkBox_isSpinnerMode_tu, checkBox_isSpinnerMode_den;
    private Spinner SpinnerSL;
    private ListView listView;

    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;
    private ArrayList<ThongKeTemp> data;
    ThongKeTempAdapter thongKeTempAdapter;

    String tgTu, tgDen; int i;
    DBManager db = new DBManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setControl();

        setEvent();

        currentDate();

        loadDataXe();

    }

    public void loadXeDK(String tgTu, String tgDen) throws Exception {
        ArrayList<ThongKeTemp> list = db.loadXeTheoTG();
        ArrayList<ThongKeTemp> listTemp = new ArrayList<>();
        int sumSL = 0, sumDT = 0;

        for(ThongKeTemp tk : list){
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(tk.getNgayDat());
            Date dTu = new SimpleDateFormat("dd/MM/yyyy").parse(tgTu);
            Date dDen = new SimpleDateFormat("dd/MM/yyyy").parse(tgDen);

            if(tk.getNgayDat() != null && dTu.getTime() <= date.getTime() && date.getTime() <= dDen.getTime()){

                listTemp.add(tk);
                sumSL = sumSL + tk.getSoLuong();
                sumDT = sumDT + tk.getGiaBan();
                load(listTemp);
                TextViewTongSLbanduoc.setText(String.valueOf(sumSL));
                formatGiaTien(sumDT);
                Log.d("TAG",listTemp.toString());
                Log.d("SL sau loc", String.valueOf(sumSL));
            }
        }
    }

    public void loadXeDKLimit(String tgTu, String tgDen, String i) throws Exception {
        ArrayList<ThongKeTemp> list = db.loadXeTheoTGLimit(i);
        ArrayList<ThongKeTemp> listTemp = new ArrayList<>();
        for(ThongKeTemp tk : list){
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(tk.getNgayDat());
            Date dTu = new SimpleDateFormat("dd/MM/yyyy").parse(tgTu);
            Date dDen = new SimpleDateFormat("dd/MM/yyyy").parse(tgDen);

            if(tk.getNgayDat() != null && dTu.getTime() <= date.getTime() && date.getTime() <= dDen.getTime()){

                listTemp.add(tk);
                load(listTemp);
                Log.d("TAG",listTemp.toString());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_loc_sp_thongke, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.xe:
                xe();
                return true;
            case R.id.phutung:
                phutung();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void xe() {
        loadDataXe();
        Toast.makeText(ThongKeActivity.this, "Xe", Toast.LENGTH_SHORT).show();
    }

    private void phutung() {
        Intent intent = new Intent(this, StatisticActivity.class);
        startActivity(intent);
        Toast.makeText(ThongKeActivity.this, "PT", Toast.LENGTH_SHORT).show();
    }

    public void currentDate(){
        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    }

    private void setEvent() {
        imageViewDateTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelectDateTu();
            }
        });
        imageViewDateDen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelectDateDen();
            }
        });
        spinnerChonSL();
        searchTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tgTu = editTextDateTu.getText().toString().trim();
                tgDen = editTextDateDen.getText().toString().trim();

                if(tgTu.length()>0&&tgDen.length()>0) {
                    if(ktThoiGian(tgTu,tgDen)==true){
                        try {
                            loadXeDK(tgTu,tgDen);
                            Log.d("tg",tgTu+"-"+tgDen);
                            Log.d("sl", String.valueOf(i));
                            Toast.makeText(ThongKeActivity.this, tgTu+"-"+tgDen, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ThongKeActivity.this, "Thoi gian khong hop le", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loadDataXe();
                }
            }
        });
    }

    public String modifyDateLayout(String inputDate) throws ParseException{
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(inputDate);
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    public void spinnerChonSL(){
        final String[] strItem = {"1","2","3"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strItem);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerSL.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        SpinnerSL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object getItem = parent.getItemAtPosition(position);

                tgTu = editTextDateTu.getText().toString().trim();
                tgDen = editTextDateDen.getText().toString().trim();

                try {
                    loadXeDKLimit(tgTu,tgDen,getItem.toString());
                    Toast.makeText(ThongKeActivity.this, getItem.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void imageSelectDateDen() {
        final boolean isSpinnerModeDen = this.checkBox_isSpinnerMode_den.isChecked();

        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                editTextDateDen.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                tgDen = editTextDateDen.getText().toString().trim();

                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };

        DatePickerDialog datePickerDialog = null;

        if(isSpinnerModeDen)  {
            // Create DatePickerDialog:
            datePickerDialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        }
        // Calendar Mode (Default):
        else {
            datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        }

        // Show
        datePickerDialog.show();
    }

    private void imageSelectDateTu() {
        final boolean isSpinnerModeTu = this.checkBox_isSpinnerMode_tu.isChecked();

        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                editTextDateTu.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                tgTu = editTextDateTu.getText().toString().trim();

                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };

        DatePickerDialog datePickerDialog = null;

        if(isSpinnerModeTu)  {
            // Create DatePickerDialog:
            datePickerDialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        }
        // Calendar Mode (Default):
        else {
            datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        }

        // Show
        datePickerDialog.show();
    }

    private void setControl() {
        TextViewTongSLbanduoc = findViewById(R.id.TextViewTongSLbanduoc);
        TextViewTongGiabanduoc = findViewById(R.id.TextViewTongGiabanduoc);
        editTextDateTu = findViewById(R.id.editText_date_tu);
        editTextDateDen = findViewById(R.id.editText_date_den);
        imageViewDateTu = findViewById(R.id.imageV_date_tu);
        imageViewDateDen = findViewById(R.id.imageV_date_den);
        searchTK = findViewById(R.id.searchTK);
        checkBox_isSpinnerMode_tu = findViewById(R.id.checkBox_isSpinnerMode_tu);
        checkBox_isSpinnerMode_den = findViewById(R.id.checkBox_isSpinnerMode_den);
        SpinnerSL = findViewById(R.id.SpinnerSL);
        listView = findViewById(R.id.listview);
    }

    public boolean ktThoiGian(String dateTu, String dateDen) {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date currentDate = new Date();
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse(dateTu);
            date2 = simpleDateFormat.parse(dateDen);

            if (date2.getTime() < date1.getTime() || date1.getTime() > currentDate.getTime()) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadDataXe(){
        data = db.loadXeTheoTG();

        load(data);

        int tongSL = db.getAllSLxe();
        TextViewTongSLbanduoc.setText(String.valueOf(tongSL));

        int tongDT = db.getAllDTxe();
        formatGiaTien(tongDT);
    }

    public void formatGiaTien(int tong){
        DecimalFormat format = new DecimalFormat("###,###,###");
        String strTongDT = format.format(tong)+" VNĐ";
        TextViewTongGiabanduoc.setText(strTongDT);
    }

    public void load(ArrayList<ThongKeTemp> data){
        thongKeTempAdapter = new ThongKeTempAdapter(this,R.layout.item_list_thong_ke,data);
        listView.setAdapter(thongKeTempAdapter);
        thongKeTempAdapter.notifyDataSetChanged();
    }
}