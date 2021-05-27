package com.example.motorshop.activity.guarantee;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.DonHang;
import com.example.motorshop.db.DBManager;

import java.util.ArrayList;

public class GuaranteeDonhangActivity extends AppCompatActivity {
    private ListView lvGuatanteeDH;
    private ArrayList<DonHang> donHangs = new ArrayList<DonHang>();
    DBManager db = new DBManager(this);
    private SimpleCursorAdapter dataAdapter;
    private EditText EditTextSearchView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarantee_donhang);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setControl();

        searchDHtheoCMND();
    }

    private void setControl() {
        lvGuatanteeDH = findViewById(R.id.lvGuatantee_DH);
        donHangs = db.loadAllDonBH();
        EditTextSearchView = findViewById(R.id.EditTextSearchView);
    }

    private void searchDHtheoCMND(){
        Cursor cursor = db.timAllDonHang();
        // The desired columns to be bound
        String[] columns = new String[] {
                "_id",
                "NGAYDAT"
        };
        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.TextViewMaDH,
                R.id.TextViewNgayTao
        };
        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.item_list_donhang_baohanh,
                cursor,
                columns,
                to,
                0);
        // Assign adapter to ListView
        lvGuatanteeDH.setAdapter(dataAdapter);
        lvGuatanteeDH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                // Get the state's capital from this row in the database.
                String maDH = cursor.getString(0);
                Intent intent = new Intent(GuaranteeDonhangActivity.this,GuaranteePhieuActivity.class);//transform
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("MADH", maDH);
                intent.putExtras(bundle);
                startActivity(intent);

                Toast.makeText(GuaranteeDonhangActivity.this,"Don Hang: "+maDH,Toast.LENGTH_SHORT).show();
            }
        });

        EditTextSearchView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });
        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return db.timDonHangTheoCMND(constraint.toString());
            }
        });
    }

}