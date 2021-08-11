package com.example.mydietapp.decorator;

import android.content.ReceiverCallNotAllowedException;
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

public class XValueFormatter implements IAxisValueFormatter {

    private DecimalFormat mFormat;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format2 = new SimpleDateFormat("MM-dd");
    private long a=16128828L;
    private long multi=100000;
    private Calendar first;

    public XValueFormatter(String f) throws ParseException {
        first=Calendar.getInstance();
        first.setTime(format.parse(f));
    }
//
//    @Override
//    public String getAxisLabel(float value, AxisBase axis) {
//        Long a=(long)value*100000;
//        String s=new BigDecimal(value).toPlainString();
////        Long b=Long.parseLong(String.format("%f",value));
//        System.out.println("value:0.5 "+value+" "+a+" "+s);
////        return format.format(new Date(a));
//        return String.valueOf(value);
//    }

    // add(new Entry())된 것들만만 formatted하는게 아니라
    // add(new Entry())를 시작한 값부터 new Entry()의 마지막 값까지 1 증가하면서 그 사이 모든 값들도 formatted함
    @Override
    public String getFormattedValue(float value, AxisBase axis) { 
//        long k=(long)value*multi+first;
        Calendar c= Calendar.getInstance();
        c.setTime(first.getTime());
        c.add(Calendar.DATE,(int)value);
        c.add(Calendar.DATE,-1);

        System.out.println("value:8 "+value+" "+format.format(c.getTime()));
      return format2.format(c.getTime());
    }

}