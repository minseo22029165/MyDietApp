package com.example.mydietapp.ui;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.example.mydietapp.MainActivity;
import com.example.mydietapp.custom.NumberDecimalInputFilter;
import com.example.mydietapp.db.DbHelper;
import com.example.mydietapp.decorator.DotPrcatcice;
import com.example.mydietapp.decorator.HighlightSundayDecorator;
import com.example.mydietapp.decorator.HighlightTodayDecorator;
import com.example.mydietapp.decorator.HighlightSaturdayDecorator;
import com.example.mydietapp.R;
import com.prolificinteractive.materialcalendarview.*;


import java.util.HashSet;
import java.util.Set;


@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarFrag extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {
    private MaterialCalendarView widget;
    private Set<CalendarDay> set;

    private DbHelper helper;
    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_calendar,container,false);
        set=new HashSet<>();

        helper = new DbHelper(getActivity(), "myDiet.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);
//        insert문
//        String sql = "INSERT INTO myRecord('record_date','weight','exercise','food') values('2021-06-28 04:20:20',12,2.5,2.0);";
//        db.execSQL(sql);
        System.out.println("gilbomi"); // MainActicity에 이 fragment를 바로 띄웠기 때문에 기본적으로 두번 반복되고 폰 화면켜서 보면 총 세번 반복됨
//        DebugDB.getAddressLog(); // android-debug-database 사용
        String sql2 = "select * from myRecord;";
        Cursor c = db.rawQuery(sql2, null);
        while(c.moveToNext()){
            String[] date=c.getString(1).split(" ")[0].split("-");
            set.add(new CalendarDay(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2])));
        }
        widget=v.findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);


        set.add(new CalendarDay(2021,6,7));
        widget.addDecorators(
                new HighlightTodayDecorator(getActivity()), // 액티비티가 context를 의미하므로 getActivity() 사용함
                new HighlightSaturdayDecorator(),
                new HighlightSundayDecorator(),
                new DotPrcatcice(Color.CYAN,set)
        );

        return v;
    }

    @Override
    public void onDateSelected(
            @NonNull MaterialCalendarView widget,
            @NonNull CalendarDay date,
            boolean selected) {
        if(set.contains(date)) {
//            date.getCalendar().after(1);
            System.out.println("bom:"+date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDay());

            StringBuffer sb = new StringBuffer();
            sb.append("select * from myRecord where record_date like ?");
            String[] params = {date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDay()+"%"};
            Cursor cursor = db.rawQuery(sb.toString(), params);
            String weight="",exercise="",food="";
            while( cursor.moveToNext() ) {
                weight=cursor.getString(2);
                exercise=cursor.getString(3);
                food=cursor.getString(4);
            }
            System.out.println("bom1:"+weight);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("weight:"+weight+"\nexercise:"+exercise+"\nfood:"+food)
                    .setTitle(date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDay());
            builder.show();
        } else {
            // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
            ((MainActivity)getActivity()).replaceFragment(AddDataFrag.newInstance(date,set));    // 새로 불러올 Fragment의 Instance를 Main으로 전달
        }
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        System.out.println("=====month changed=====");
    }





}
