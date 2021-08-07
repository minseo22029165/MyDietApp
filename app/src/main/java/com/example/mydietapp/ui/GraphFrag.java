package com.example.mydietapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Rating;
import android.os.Bundle;
import android.view.*;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mydietapp.R;
import com.example.mydietapp.db.DbHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.*;

public class GraphFrag extends Fragment {
    private DbHelper helper;
    private SQLiteDatabase db;

    private View v;
    private View dialogV;

    private LineChart chart;
    private LineDataSet weiSet;
    private LineDataSet exSet;
    private LineDataSet foSet;
    private ArrayList<String> dateValue;
    private ArrayList<Float> weiValue;
    private ArrayList<Float> exValue;
    private ArrayList<Float> foValue;

    private CheckBox exCheck;
    private CheckBox foCheck;
    private RatingBar fo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.frag_graph,container,false);
        dialogV= LayoutInflater.from(v.getContext()).inflate(R.layout.graph_dialog, null);
        setHasOptionsMenu(true);


        helper = new DbHelper(getActivity(), "myDiet.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        select();
        setChart();
        setXAxis();
        setLeftYAxis();
        setRightYAxis();

        return v;
    }
    public void select() {
        Cursor cursor = db.rawQuery("select * from myRecord order by record_date", null);

        dateValue=new ArrayList<>();
        weiValue=new ArrayList<>();
        foValue=new ArrayList<>();
        exValue=new ArrayList<>();

        while (cursor.moveToNext()) {
            dateValue.add(cursor.getString(1));
            weiValue.add(Float.parseFloat(cursor.getString(2)));
            exValue.add(Float.parseFloat(cursor.getString(3)));
            foValue.add(Float.parseFloat(cursor.getString(4)));
        }
    }

    public void setChart() {
        chart = v.findViewById(R.id.chart);
        ArrayList<Entry> date = new ArrayList<>();
        ArrayList<Entry> weight = new ArrayList<>(); // 그려지는 차트에 들어갈 값
        ArrayList<Entry> exercise = new ArrayList<>();
        ArrayList<Entry> food = new ArrayList<>();

        for (int i = 0; i < weiValue.size(); i++) {
            float val = weiValue.get(i);
            weight.add(new Entry(i, val)); // i값이 x축 값, val값이 y축 값
            exercise.add(new Entry(i, exValue.get(i)));
            food.add(new Entry(i, foValue.get(i)));
        }
        weiSet = new LineDataSet(weight, "몸무게"); // 차트 값, 차트 이름
        exSet =new LineDataSet(exercise,"운동량");
        foSet =new LineDataSet(food,"식사량");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>(); // 여러 차트를 넣음
        dataSets.add(weiSet); // add the data sets
        dataSets.add(exSet);
        dataSets.add(foSet);

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        weiSet.setColor(Color.BLACK);
        weiSet.setCircleColor(Color.BLACK);

        exSet.setColor(Color.RED);
        exSet.setCircleColor(Color.RED);
        exSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        exSet.setDrawValues(false);
        foSet.setColor(Color.BLUE);
        foSet.setCircleColor(Color.BLUE);
        foSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        foSet.setDrawValues(false);

        // set data
        chart.setData(data);
        chart.setVisibleXRangeMaximum(5); // allow 20 values to be displayed at once on the x-axis, not more
        chart.moveViewToX(5); // set the left edge of the chart to x-index 10
    }
    public void setXAxis() {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 위치 설정
        xAxis.setLabelCount(10, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
//        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); // X축 텍스트컬러설정
//        xAxis.setGridColor(ContextCompat.getColor(getContext(), R.color.textColor)); // X축 줄의 컬러 설정
    }
    public void setLeftYAxis() { // 몸무게 세팅
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMaximum(100f); // 최대값 100
        leftAxis.setAxisMinimum(0f); // 최소값 0
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
    }
    public void setRightYAxis() { // 5점만점 세팅
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(5); // 최대값 100
        rightAxis.setAxisMinimum(0); // 최소값 0
        rightAxis.setLabelCount(11);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setGranularityEnabled(false);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.graph_top_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                exCheck=dialogV.findViewById(R.id.exCheck);
                foCheck=dialogV.findViewById(R.id.foCheck);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("필터 선택");
                if(dialogV.getParent()!=null) // view가 한 부모 뷰에만 추가될 수 있고 중복은 에러 발생
                    ((ViewGroup) dialogV.getParent()).removeView(dialogV);
                builder.setView(dialogV);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(exCheck.isChecked())
                                    exSet.setVisible(true);
                                else
                                    exSet.setVisible(false);
                                if(foCheck.isChecked())
                                    foSet.setVisible(true);
                                else
                                    foSet.setVisible(false);
                            }
                        })
                        .setNegativeButton("취소", null);
                builder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
