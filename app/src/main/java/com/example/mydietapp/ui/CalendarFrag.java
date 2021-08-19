package com.example.mydietapp.ui;

import android.app.ActionBar;
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
import androidx.appcompat.app.AppCompatActivity;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


        String sql2 = "select * from myRecord;";
        Cursor c = db.rawQuery(sql2, null);
        while(c.moveToNext()){
            String[] date=c.getString(1).split(" ")[0].split("-");
//            set.add(new CalendarDay(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2])));
            try {
                set.add(new CalendarDay(new SimpleDateFormat( "yyyy-MM-dd").parse(c.getString(1))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        widget=v.findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);

        widget.addDecorators(
                new HighlightTodayDecorator(getActivity()), // 액티비티가 context를 의미하므로 getActivity() 사용함
                new HighlightSaturdayDecorator(),
                new HighlightSundayDecorator(),
                new DotPrcatcice(Color.CYAN,set,getActivity())
        );

        return v;
    }

    @Override
    public void onDateSelected(
            @NonNull MaterialCalendarView widget,
            @NonNull CalendarDay date,
            boolean selected) {
            MainActivity.fragmentStack.push(this);
            ((MainActivity)getActivity()).replaceFragment(AddDataFrag.newInstance(date,set));    // 새로 불러올 Fragment의 Instance를 Main으로 전달

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
//        System.out.println("=====month changed=====");
    }





}
