package com.example.mydietapp.Decorator;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.example.mydietapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

/**
 * Highlight Saturdays and Sundays with a background
 */
public class HighlightTodayDecorator implements DayViewDecorator {

    private final Drawable highlightDrawable;
    private static final int color = Color.RED;

    private final Calendar calendar = Calendar.getInstance();
    private CalendarDay date; // 현재 날짜

    public HighlightTodayDecorator(Activity context) {
        highlightDrawable = ContextCompat.getDrawable(context,R.drawable.complete_circle_background);
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) { // day는 모든 날짜 for문 반복
        day.copyTo(calendar);
        System.out.println("******day:"+day+", date:"+date);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(final DayViewFacade view) {
        view.setBackgroundDrawable(highlightDrawable);
    }
}