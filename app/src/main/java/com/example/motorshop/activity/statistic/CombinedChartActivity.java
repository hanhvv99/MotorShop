//package com.example.motorshop.activity.statistic;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.motorshop.activity.R;
//import com.example.motorshop.db.DBManager;
//import com.github.mikephil.charting.charts.CombinedChart;
//import com.github.mikephil.charting.components.AxisBase;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.CombinedData;
//import com.github.mikephil.charting.data.DataSet;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.formatter.IAxisValueFormatter;
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
//import com.github.mikephil.charting.highlight.Highlight;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CombinedChartActivity extends AppCompatActivity implements OnChartValueSelectedListener{
//    private CombinedChart mChart;
//    DBManager db = new DBManager(this);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_combined_chart);
//
//        mChart = (CombinedChart) findViewById(R.id.combinedChart);
//        mChart.getDescription().setEnabled(false);
//        mChart.setBackgroundColor(Color.WHITE);
//        mChart.setDrawGridBackground(false);
//        mChart.setDrawBarShadow(false);
//        mChart.setHighlightFullBarEnabled(false);
//        mChart.setOnChartValueSelectedListener(this);
//
//        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setAxisMinimum(0f);
//
//        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setAxisMinimum(0f);
//
//        final List<String> xLabel = new ArrayList<>();
//        xLabel.add("Jan");
//        xLabel.add("Feb");
//        xLabel.add("Mar");
//        xLabel.add("Apr");
//        xLabel.add("May");
//        xLabel.add("Jun");
//        xLabel.add("Jul");
//        xLabel.add("Aug");
//        xLabel.add("Sep");
//        xLabel.add("Oct");
//        xLabel.add("Nov");
//        xLabel.add("Dec");
//
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisMinimum(0f);
//        xAxis.setGranularity(1f);
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabel));
//
//        CombinedData data = new CombinedData();
//        LineData lineDatas = new LineData();
//        lineDatas.addDataSet((ILineDataSet) dataChart());
//
//        data.setData(lineDatas);
//
//        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
//
//        mChart.setData(data);
//        mChart.invalidate();
//    }
//
//    @Override
//    public void onValueSelected(Entry e, Highlight h) {
//        Toast.makeText(this, "Value: "
//                + e.getY()
//                + ", index: "
//                + h.getX()
//                + ", DataSet index: "
//                + h.getDataSetIndex(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onNothingSelected() {
//
//    }
//
//    public DataSet dataChart() {
//
//        LineData d = new LineData();
////        Integer data[] = db.getThang("5");
//
//        ArrayList<Entry> entries = new ArrayList<Entry>();
//
//        for (int index = 0; index < 12; index++) {
//            entries.add(new Entry(index, data[index]));
//        }
//
//        LineDataSet set = new LineDataSet(entries, "Request Ots approved");
//        set.setColor(Color.GREEN);
//        set.setLineWidth(2.5f);
//        set.setCircleColor(Color.GREEN);
//        set.setCircleRadius(5f);
//        set.setFillColor(Color.GREEN);
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set.setDrawValues(true);
//        set.setValueTextSize(10f);
//        set.setValueTextColor(Color.GREEN);
//
//        set.setAxisDependency(YAxis.AxisDependency.LEFT);
//        d.addDataSet(set);
//
//        return set;
//    }
//}