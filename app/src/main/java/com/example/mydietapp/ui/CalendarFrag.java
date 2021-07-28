package com.example.mydietapp.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.example.mydietapp.Decorator.DotPrcatcice;
import com.example.mydietapp.Decorator.HighlightSundayDecorator;
import com.example.mydietapp.Decorator.HighlightTodayDecorator;
import com.example.mydietapp.Decorator.HighlightSaturdayDecorator;
import com.example.mydietapp.R;
import com.prolificinteractive.materialcalendarview.*;


import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;


@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarFrag extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    private MaterialCalendarView widget;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_calendar,container,false);
        widget=v.findViewById(R.id.calendarView);

        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);

        Set<CalendarDay> set=new HashSet<>();
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
        System.out.println("=====data selected====="+widget.getSelectedDate());
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        System.out.println("=====month changed=====");
    }





}
