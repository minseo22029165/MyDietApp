package com.example.mydietapp.decorator;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XValueFormatter extends ValueFormatter {

    private DecimalFormat mFormat;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public XValueFormatter() {
//        mFormat = new DecimalFormat("###,###,###"); // use one decimal
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
//        return String.valueOf(value);
//        System.out.println("value: "+value+" "+format.format(new Date(((long)value)*10000)));
        System.out.println("value:0.5 "+value);
//        return format.format(new Date(((long)value)*1000));
//        return format.format(new Date((long) value));
        return "3";
    }

    @Override
    public String getFormattedValue(float value) {
        return "2";
    }
}