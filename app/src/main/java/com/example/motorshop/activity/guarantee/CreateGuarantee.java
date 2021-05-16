package com.example.motorshop.activity.guarantee;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.motorshop.activity.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateGuarantee extends AppCompatActivity {
    FloatingSearchView floating_search_view;
    EditText edit_mabh, edit_manv,edit_cmnd, edit_tenkh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guarantee_edit);

        setControl();
        setEvent();
    }

    private void setEvent() {

    }

    private void setControl() {
        floating_search_view = findViewById(R.id.floating_search_view);
        edit_mabh = findViewById(R.id.edit_mabh);
        edit_manv = findViewById(R.id.edit_manv);
        edit_cmnd = findViewById(R.id.edit_cmnd_kh);
        edit_tenkh = findViewById(R.id.edit_tenkh);
    }
}
