package com.example.mydietapp.decorator;


import android.graphics.Color;
import android.os.Build;
import android.text.style.ForegroundColorSpan;
import androidx.annotation.RequiresApi;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.time.DayOfWeek;
import java.util.Calendar;

/**
 * Highlight Saturdays and Sundays with a background
 */
public class HighlightSaturdayDecorator implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();
    private CalendarDay date; // 현재 날짜

    public HighlightSaturdayDecorator() {
        date = CalendarDay.today();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean shouldDecorate(CalendarDay day) { // day는 모든 날짜 for문 반복
        day.copyTo(calendar);
        return day.getDate().getDay()==DayOfWeek.SATURDAY.getValue(); // 토요일
    }

    @Override
    public void decorate(final DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLUE));

    }
}