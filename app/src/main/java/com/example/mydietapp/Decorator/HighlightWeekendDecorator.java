package com.example.mydietapp.Decorator;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.time.DayOfWeek;
import java.util.Calendar;

/**
 * Highlight Saturdays and Sundays with a background
 */
public class HighlightWeekendDecorator implements DayViewDecorator {

    private final Drawable highlightDrawable;
    private static final int color = Color.BLUE;

    private final Calendar calendar = Calendar.getInstance();
    private CalendarDay date; // 현재 날짜

    public HighlightWeekendDecorator() {
        highlightDrawable = new ColorDrawable(color);
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) { // day는 모든 날짜 for문 반복
//        final DayOfWeek weekDay = day.getDay().dayOfWeek();
//        return weekDay == DayOfWeek.SATURDAY || weekDay == DayOfWeek.SUNDAY;
//        day.copyTo(calendar);

        return day.getDate().getDay()==6; // 토요일
    }

    @Override
    public void decorate(final DayViewFacade view) {
        view.setBackgroundDrawable(highlightDrawable);
    }
}