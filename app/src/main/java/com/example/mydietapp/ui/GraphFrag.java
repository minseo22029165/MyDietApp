package com.example.mydietapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.webkit.HttpAuthHandler;
import android.widget.CheckBox;
import android.widget.RatingBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mydietapp.R;
import com.example.mydietapp.db.DbHelper;
import com.example.mydietapp.decorator.ColoredLabelXAxisRenderer;
import com.example.mydietapp.decorator.XValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private Map<String,Float> w; // date,weight
    private Map<String,Float> e;
    private Map<String,Float> f;

    private CheckBox exCheck;
    private CheckBox foCheck;

    private SimpleDateFormat format;
    private List<Integer> indexes;
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

        select();
        try {
            setChart();
            setXAxis();
        } catch (ParseException parseException) {
            System.out.println("엥");
        }
        setLeftYAxis();
        setRightYAxis();

//        String a=format.format(CalendarDay.today())
        System.out.println("날짜:"+ (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY));

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

    public void setChart() throws ParseException {
        chart = v.findViewById(R.id.chart);
        ArrayList<Entry> date = new ArrayList<>();
        ArrayList<Entry> weight = new ArrayList<>(); // 그려지는 차트에 들어갈 값
        ArrayList<Entry> exercise = new ArrayList<>();
        ArrayList<Entry> food = new ArrayList<>();

        Calendar startDate=Calendar.getInstance();
        startDate.setTime(format.parse(dateValue.get(0)));
        startDate.add(Calendar.DATE,-8);

        Calendar endDate=CalendarDay.today().getCalendar();
        endDate.add(Calendar.DATE,3);

        int index=0;
//        indexes=new ArrayList<>();
//        weight.add(new Entry(index++,Float.NaN));
        for(Calendar c=startDate;c.before(endDate);c.add(Calendar.DATE,1)) {
            System.out.println("c:1 "+format.format(c.getTime()));
            if(dateValue.contains(format.format(c.getTime()))) {
                int i=dateValue.indexOf(format.format(c.getTime()));
                int a = (int) (c.getTimeInMillis()/100000);
                System.out.println("value:2 "+c.getTime().getTime()+" "+format.format(new Date(c.getTime().getTime())));

                Calendar c2= Calendar.getInstance();
                c2.setTime(format.parse(dateValue.get(0)));
                c2.add(Calendar.DATE,40);
                System.out.println("value: al "+format.format(c2.getTime()));

                Calendar c1= Calendar.getInstance();
                c1.setTime(format.parse(dateValue.get(0)));
                c1.add(Calendar.DATE,-1);
                for(int m=0;m<365;m++) {
                    System.out.println("value: ii1 "+m+" "+format.format(c1.getTime()));
                    if((format.format(c1.getTime())).equals(format.format(c.getTime()))) {
                        System.out.println("value: ii2 "+m+" "+format.format(c1.getTime()));
                        weight.add(new Entry(m,weiValue.get(i)));
//                        exercise.add(new Entry(ii,exValue.get(i)));
//                        food.add(new Entry(ii,foValue.get(i)));
                        break;
                    }
                    c1.add(Calendar.DATE,1);
                }

//                weight.add(new Entry(index*10,weiValue.get(i)));
//                index++;

//                weight.add(new Entry( ,weiValue.get(i)));
////                weight.add(new Entry(1 ,weiValue.get(i)));
//                exercise.add(new Entry(,exValue.get(i)));
//                food.add(new Entry(,foValue.get(i)));
            }

        }


        weiSet =new LineDataSet(weight, "몸무게"); // 차트 값, 차트 이름
//        exSet =new LineDataSet(exercise,"운동량");
//        foSet =new LineDataSet(food,"식사량");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>(); // 여러 차트를 넣음
        dataSets.add(weiSet); // add the data sets
//        dataSets.add(exSet);
//        dataSets.add(foSet);

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        weiSet.setColor(Color.BLACK);
        weiSet.setCircleColor(Color.BLACK);

//        exSet.setColor(Color.RED);
//        exSet.setCircleColor(Color.RED);
//        exSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
//        exSet.setDrawValues(false);
//        foSet.setColor(Color.BLUE);
//        foSet.setCircleColor(Color.BLUE);
//        foSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
//        foSet.setDrawValues(false);

        // set data
        chart.setDescription(null);
        chart.setData(data);
        chart.setVisibleXRangeMaximum(7); // setLabelCount와 동일하면 label의 세로줄과 딱 맞쳐짐
        chart.moveViewToX(Integer.MAX_VALUE); // 데이터 총 개수보다 몇개 더 많아야됨
        chart.setXAxisRenderer(new ColoredLabelXAxisRenderer(chart.getViewPortHandler(), chart.getXAxis(), chart.getTransformer(YAxis.AxisDependency.LEFT)));
    }
    public void setXAxis() throws ParseException {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 위치 설정
//        xAxis.setGranularity(1f); // 줌 간격(3개월이나 1년 단위일때 한번 해보기)
        xAxis.setLabelCount(7, false); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        xAxis.setValueFormatter(new XValueFormatter(dateValue.get(0)));

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
