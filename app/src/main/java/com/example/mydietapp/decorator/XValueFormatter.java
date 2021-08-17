package com.example.mydietapp.decorator;

import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
import android.graphics.Color;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.*;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class XValueFormatter extends ValueFormatter {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format2;
    private Calendar first;

    public XValueFormatter(String d,String type) throws ParseException {
        first=Calendar.getInstance();
        first.setTime(format.parse(d));

        switch (type) {
            case "week":
            case "month":
            case "month3":
                format2=new SimpleDateFormat("MM-dd");
                break;
            case "year":
                format2=new SimpleDateFormat("yy-MM");
                break;
        }
    }

    // add(new Entry())된 것들만만 formatted하는게 아니라
    // add(new Entry())를 시작한 값부터 new Entry()의 마지막 값까지 1 증가하면서 그 사이 모든 값들도 formatted함
    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        Calendar c= Calendar.getInstance();
        c.setTime(first.getTime());
        c.add(Calendar.DATE,(int)value);
        c.add(Calendar.DATE,-1);
//        if((c.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY)) // not working
//            axis.setGridColor(Color.RED);
//        else {
//            axis.setGridColor(Color.GRAY);
//        }
        return format2.format(c.getTime());
    }
}