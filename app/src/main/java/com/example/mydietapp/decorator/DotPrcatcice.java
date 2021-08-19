package com.example.mydietapp.decorator;


import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.example.mydietapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Highlight Saturdays and Sundays with a background
 */
public class DotPrcatcice implements DayViewDecorator {
    private final Drawable highlightDrawable;
    private final int color;
    private final HashSet<CalendarDay> dates;

    public DotPrcatcice(int color, Collection<CalendarDay> dates, Context context) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        highlightDrawable = ContextCompat.getDrawable(context, R.drawable.ic_check_black_24dp);
    }

    public DotPrcatcice(int color, HashSet<CalendarDay> dates, Context context) {
        this.color = color;
        this.dates = dates;
        highlightDrawable = ContextCompat.getDrawable(context,R.drawable.ic_check_black_24dp);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(highlightDrawable);
//        view.addSpan(new DotSpan(30, color));
    }
}