package com.example.mydietapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mydietapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphFrag extends Fragment {
    private LineChart chart;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.frag_graph,container,false);
        setChart();
        setLeftYAxis();
        setRightYAxis();

        return v;
    }
    public void setLeftYAxis() { // 몸무게 세팅
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(100f); // 최대값 100
        leftAxis.setAxisMinimum(0f); // 최소값 0
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
    }
    public void setRightYAxis() {
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(5); // 최대값 100
        rightAxis.setAxisMinimum(0); // 최소값 0
        rightAxis.setLabelCount(11);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setGranularityEnabled(false);
    }
    public void setChart() {
        chart = v.findViewById(R.id.chart);
        ArrayList<Entry> values = new ArrayList<>(); // 그려지는 차트에 들어갈 값

        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));
        }

        LineDataSet set1; // 하나의 차트를 의미
        set1 = new LineDataSet(values, "DataSet 1"); // 차트 값, 차트 이름

        ArrayList<ILineDataSet> dataSets = new ArrayList<>(); // 여러 차트를 넣음
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        // set data
        chart.setData(data);
    }
}
