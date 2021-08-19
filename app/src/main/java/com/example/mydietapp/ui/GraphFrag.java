package com.example.mydietapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mydietapp.MainActivity;
import com.example.mydietapp.R;
import com.example.mydietapp.db.DbHelper;
import com.example.mydietapp.decorator.MyMarkerView;
import com.example.mydietapp.decorator.XValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private XAxis xAxis;
    private YAxis rightAxis;
    private YAxis leftAxis;

    private ArrayList<String> dateValue;
    private ArrayList<Float> weiValue;
    private ArrayList<Float> exValue;
    private ArrayList<Float> foValue;

    private CheckBox exCheck;
    private CheckBox foCheck;

    private SimpleDateFormat format;
    private float minWeight;
    private float maxWeight;

    private RadioGroup radioGroup;
    private RadioButton radio_week;
    private RadioButton radio_month;
    private RadioButton radio_month3;
    private RadioButton radio_year;
    private int between;

    private MyMarkerView mv;

    public RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            switch(i) {
                case R.id.week:
                    xAxis.resetAxisMaximum();
                    xAxis.resetAxisMinimum();
                    chart.notifyDataSetChanged();
                    chart.fitScreen();
                    try {
                        setXAxis(7,3,"week");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    chartDetailed(7,8);
                    weiSet.setDrawValues(false);
                    weiSet.setDrawCircles(true);
                    foSet.setDrawCircles(true);
                    exSet.setDrawCircles(true);

                    mv.setChartView(chart);
                    chart.setMarker(mv);
                    break;
                case R.id.month:
                    xAxis.resetAxisMaximum();
                    xAxis.resetAxisMinimum();
                    chart.notifyDataSetChanged();
                    chart.fitScreen();

                    try {
                        setXAxis(8,25,"month");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    chartDetailed(25,45);
                    weiSet.setDrawValues(false);
                    weiSet.setDrawCircles(true);
                    foSet.setDrawCircles(true);
                    exSet.setDrawCircles(true);

                    mv.setChartView(chart);
                    chart.setMarker(mv);
                    break;
                case R.id.month3:
                    xAxis.resetAxisMaximum();
                    xAxis.resetAxisMinimum();
                    chart.notifyDataSetChanged();
                    chart.fitScreen();

                    try {
                        setXAxis(8,105,"month3");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    chartDetailed(105,190);
                    weiSet.setDrawValues(false);
                    weiSet.setDrawCircles(false);
                    foSet.setDrawCircles(false);
                    exSet.setDrawCircles(false);

                    mv.setChartView(null);
                    chart.setMarker(null);
                    break;
                case R.id.year:
                    xAxis.resetAxisMaximum();
                    xAxis.resetAxisMinimum();
                    chart.notifyDataSetChanged();
                    chart.fitScreen();

                    try {
                        setXAxis(12,365,"year");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    chartDetailed(365,670);
                    weiSet.setDrawValues(false);
                    weiSet.setDrawCircles(false);
                    foSet.setDrawCircles(false);
                    exSet.setDrawCircles(false);

                    mv.setChartView(null);
                    chart.setMarker(null);
                    break;
            }
            System.out.println("max2:"+ chart.getXChartMax());

            chart.notifyDataSetChanged();
            chart.invalidate();

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.frag_graph,container,false);
        dialogV= LayoutInflater.from(v.getContext()).inflate(R.layout.graph_dialog, null);
        setHasOptionsMenu(true);
        format = new SimpleDateFormat ( "yyyy-MM-dd");

        helper = new DbHelper(getActivity(), "myDiet.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        radioGroup=v.findViewById(R.id.radioGroup);
        radio_week=v.findViewById(R.id.week);
        radio_month=v.findViewById(R.id.month);
        radio_month3=v.findViewById(R.id.month3);
        radio_year=v.findViewById(R.id.year);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        chart = v.findViewById(R.id.chart);

        select();
        if(dateValue.size()==0) {
            TextView t=v.findViewById(R.id.alertText);
            t.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
            chart.setVisibility(View.GONE);
            return v;
        }
        try {
            setChart();
            setXAxis(7,3,"week");
        } catch (ParseException parseException) {
            System.out.println("엥");
        }
        setLeftYAxis();
        setRightYAxis();
        chartDetailed(7,8);

//        chart.setDragEnabled(true);
//        chart.setScaleEnabled(true);
//        chart.setPinchZoom(true);


        mv = new MyMarkerView(getActivity(),R.layout.custom_marker_view,LayoutInflater.from(v.getContext()).inflate(R.layout.custom_marker_view, null));
        mv.setChartView(chart);
        chart.setMarker(mv);
        return v;
    }
    public void select() {
        Cursor cursor = db.rawQuery("select * from myRecord order by record_date", null);

        dateValue=new ArrayList<>();
        weiValue=new ArrayList<>();
        foValue=new ArrayList<>();
        exValue=new ArrayList<>();

        minWeight=Float.MAX_VALUE;
        maxWeight=Float.MIN_VALUE;

        while (cursor.moveToNext()) {
            dateValue.add(cursor.getString(1));
            weiValue.add(Float.parseFloat(cursor.getString(2)));
            exValue.add(Float.parseFloat(cursor.getString(3)));
            foValue.add(Float.parseFloat(cursor.getString(4)));

            if(minWeight>Float.parseFloat(cursor.getString(2)))
                minWeight=Float.parseFloat(cursor.getString(2));
            if(maxWeight<Float.parseFloat(cursor.getString(2)))
                maxWeight=Float.parseFloat(cursor.getString(2));
        }
    }

    public void setChart() throws ParseException {
        ArrayList<Entry> date = new ArrayList<>();
        ArrayList<Entry> weight = new ArrayList<>(); // 그려지는 차트에 들어갈 값
        ArrayList<Entry> exercise = new ArrayList<>();
        ArrayList<Entry> food = new ArrayList<>();

        Calendar startDate=Calendar.getInstance();
        startDate.setTime(format.parse(dateValue.get(0)));
        Calendar endDate=CalendarDay.today().getCalendar();
        endDate.add(Calendar.DATE,1);

        between=0;
        for(Calendar c=startDate;c.before(endDate);c.add(Calendar.DATE,1)) {
            if(dateValue.contains(format.format(c.getTime()))) {
                int i=dateValue.indexOf(format.format(c.getTime()));

                Calendar c1= Calendar.getInstance();
                c1.setTime(format.parse(dateValue.get(0)));
                c1.add(Calendar.DATE,-1);

                for(int m=0;m<365;m++) {
                    if((format.format(c1.getTime())).equals(format.format(c.getTime()))) {
                        weight.add(new Entry(m,weiValue.get(i)));
                        exercise.add(new Entry(m,exValue.get(i)));
                        food.add(new Entry(m,foValue.get(i)));
                        break;
                    }
                    c1.add(Calendar.DATE,1);
                }
            }
            between++;
        }


        weiSet =new LineDataSet(weight, "몸무게"); // 차트 값, 차트 이름
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
        weiSet.setDrawValues(false);

        exSet.setColor(Color.RED);
        exSet.setCircleColor(Color.RED);
        exSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        exSet.setDrawValues(false);
        foSet.setColor(Color.BLUE);
        foSet.setCircleColor(Color.BLUE);
        foSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        foSet.setDrawValues(false);
        chart.setDescription(null);
        chart.setData(data);
//        chart.setXAxisRenderer(new ColoredLabelXAxisRenderer(chart.getViewPortHandler(), chart.getXAxis(), chart.getTransformer(YAxis.AxisDependency.LEFT)));
    }
    public void chartDetailed(int count,float value) {
        chart.setVisibleXRangeMaximum(count); // setLabelCount와 동일하면 label의 세로줄과 딱 맞쳐짐
        chart.moveViewToX(chart.getXChartMax()-value); // 데이터 총 개수보다 몇개 더 많아야됨
    }

    public void setXAxis(int count,float range,String type) throws ParseException {
        xAxis = chart.getXAxis();
        xAxis.setLabelRotationAngle(-30);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 위치 설정
        xAxis.setGranularity(1f); // 줌 간격(3개월이나 1년 단위일때 한번 해보기)
        xAxis.setLabelCount(count, false); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정, force가 false면 줄이 스크롤에 고정돼서 같이 움직임
        xAxis.setValueFormatter(new XValueFormatter(dateValue.get(0),type));
        xAxis.setAxisMinimum((int)(chart.getXChartMin()-range)); // xaxis 시작전 여백
        xAxis.setAxisMaximum((int)(chart.getXChartMax()+range)); // xaxis 끝나고 여백

    }
    public void setLeftYAxis() { // 몸무게 세팅
        leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMaximum(maxWeight+5);
        leftAxis.setAxisMinimum(minWeight-3);

        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(0.1f);
    }
    public void setRightYAxis() { // 5점만점 세팅
        rightAxis = chart.getAxisRight();
        rightAxis.setTextColor(R.color.purple);
        rightAxis.setAxisMaximum(5);
        rightAxis.setAxisMinimum(0);
        rightAxis.setLabelCount(11);

        rightAxis.setGranularity(0.5f);
        rightAxis.setDrawGridLines(true);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(dateValue.size()!=0)
            inflater.inflate(R.menu.graph_top_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                mv.setClickable(false);
                chart.setMarker(null);

                exCheck=dialogV.findViewById(R.id.exCheck);
                foCheck=dialogV.findViewById(R.id.foCheck);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("필터 선택");
                if(dialogV.getParent()!=null) // view가 한 부모 뷰에만 추가될 수 있고 중복은 에러 발생
                    ((ViewGroup) dialogV.getParent()).removeView(dialogV);
                builder.setView(dialogV);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ArrayList<ILineDataSet> dataSets = new ArrayList<>(); // 여러 차트를 넣음
                                dataSets.add(weiSet); // add the data sets

                                if (exCheck.isChecked()) {
                                    exSet.setVisible(true);
                                    dataSets.add(exSet);
                                } else {
                                    exSet.setVisible(false);
                                }
                                if (foCheck.isChecked()) {
                                    foSet.setVisible(true);
                                    dataSets.add(foSet);
                                } else {
                                    foSet.setVisible(false);
                                }
                                LineData data = new LineData(dataSets);
                                // black lines and points
                                weiSet.setColor(Color.BLACK);
                                weiSet.setCircleColor(Color.BLACK);
                                weiSet.setDrawValues(false);
                                exSet.setColor(Color.RED);
                                exSet.setCircleColor(Color.RED);
                                exSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
                                exSet.setDrawValues(false);
                                foSet.setColor(Color.BLUE);
                                foSet.setCircleColor(Color.BLUE);
                                foSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
                                foSet.setDrawValues(false);
                                chart.setDescription(null);
                                chart.setData(data);

                                chart.notifyDataSetChanged();
                                chart.invalidate();

                                mv.setChartView(chart);
                                chart.setMarker(mv);
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
