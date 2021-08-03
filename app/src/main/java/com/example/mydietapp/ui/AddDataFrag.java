package com.example.mydietapp.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mydietapp.MainActivity;
import com.example.mydietapp.R;
import com.example.mydietapp.custom.NumberDecimalInputFilter;
import com.example.mydietapp.db.DbHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.*;

public class AddDataFrag extends Fragment {
    private static CalendarDay day;
    private static HashSet<CalendarDay> dataSet=new HashSet<>();
    private static List<CalendarDay> list;

    private DbHelper helper;
    private SQLiteDatabase db;

    private EditText weight;
    private RatingBar food;
    private RatingBar exercise;
    private Button registButton;
    private DatePicker dp;
    private TextView previousWeightText;

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static AddDataFrag newInstance(CalendarDay date, Set<CalendarDay> set) {
        day=date;
        dataSet.addAll(set);
        list=new ArrayList<>(dataSet);
        Collections.sort(list, new Comparator<CalendarDay>() {
            @Override
            public int compare(CalendarDay t1, CalendarDay t2) { // 오름차순
                Calendar left=t1.getCalendar();
                Calendar right=t2.getCalendar();
                return left.compareTo(right);
            }
        });
        return new AddDataFrag();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_adddata,container,false);

        helper = new DbHelper(getActivity(), "myDiet.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        EditText edit=v.findViewById(R.id.weightEditText);
        edit.setFilters(new InputFilter[] {new NumberDecimalInputFilter(0,200,1)});
        edit.setFilters(new InputFilter[] {new NumberDecimalInputFilter(0,200,1)});
        Button datePicker=v.findViewById(R.id.datePickerButton);
        ImageButton previous=v.findViewById(R.id.previousDateButton);
        ImageButton next=v.findViewById(R.id.nextDateButton);

        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd");
        String time1 = format.format(day.getCalendar().getTime());
        datePicker.setText(time1);

        Calendar c=day.getCalendar(); // 현재 선택된 날

        weight=v.findViewById(R.id.weightEditText);
        food=v.findViewById(R.id.foodRating);
        exercise=v.findViewById(R.id.exerciseRating);
        registButton=v.findViewById(R.id.registButton);
        previousWeightText=v.findViewById(R.id.previousWeightText);

        getDbData(c);
        previousData(c);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("날짜 선택");
                builder.setView(R.layout.dialog_datepicker);


                builder.setNegativeButton("취소",null);
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        c.set(dp.getYear(),dp.getMonth(),dp.getDayOfMonth());
                        dp.updateDate(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                        datePicker.setText(format.format(c.getTime()));
                        getDbData(c);
                    }
                });

                AlertDialog dialog=builder.create();
                dialog.setCanceledOnTouchOutside(true); //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
                //Dialog 보이기
                dialog.show();

                dp=dialog.findViewById(R.id.datePicker);
                dp.init(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE),null);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE, -1);
                datePicker.setText(format.format(c.getTime()));

                System.out.println("ok:" + c.get(Calendar.YEAR) + " " + (c.get(Calendar.MONTH) + 1) + " " + c.get(Calendar.DATE));

                getDbData(c);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE,1);
                datePicker.setText(format.format(c.getTime()));

                getDbData(c);
            }
        });
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weight.getText().toString().equals("")) {
                    System.out.println("weight 비어있음");
                } else { // 등록할껀지 수정할껀지 if로 나눠서 진행하자!!
//                    for(CalendarDay s:dataSet) {
//                        System.out.println("cd1:"+s.getYear()+" "+s.getMonth()+" "+s.getDay());
//                    }
                    CalendarDay cd=new CalendarDay(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
//                    System.out.println("cd:"+cd.getYear()+" "+cd.getMonth()+" "+cd.getDay());
                    if(dataSet.contains(cd)) {
                        System.out.println("log:수정");
                        ContentValues values = new ContentValues();
                        values.put("weight",Float.parseFloat(weight.getText().toString()));
                        values.put("exercise",exercise.getRating());
                        values.put("food",food.getRating());
                        db.update("myRecord",values,"record_date=?", new String[]{cd.getYear()+"-"+(cd.getMonth()+1)+"-"+cd.getDay()});
                    } else {
                        System.out.println("log:새 등록");
                        ContentValues values = new ContentValues();
                        values.put("record_date",cd.getYear()+"-"+(cd.getMonth()+1)+"-"+cd.getDay());
                        values.put("weight",Float.parseFloat(weight.getText().toString()));
                        values.put("exercise",exercise.getRating());
                        values.put("food",food.getRating());
                        db.insert("myRecord",null,values);
                    }
                }
            }
        });


        return v;
    }
    public void previousData(Calendar c) {
        int r=getRange(list,c);
        if(r==-1) {
            previousWeightText.setText("");
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("select * from myRecord where record_date like ?");
            String[] params = {c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE)};
            Cursor cursor = db.rawQuery(sb.toString(), params);

            String weiValue = "", exValue = "", foValue = "";

            while (cursor.moveToNext()) {
                weiValue = cursor.getString(2);
                exValue = cursor.getString(3);
                foValue = cursor.getString(4);
            }
            previousWeightText.setText("");
        }
    }
    public int getRange(List<CalendarDay> list, Calendar date) {
        if(list.isEmpty())
            return -1;
        for(int i=0;i<list.size()-1;i++) { // size()==0 or 1 제외
            Calendar v1=list.get(i).getCalendar();
            if(v1.equals(date))
                return i == 0 ? -1 : list.indexOf(new CalendarDay(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE))) - 1;
            if(date.after(list.get(i).getCalendar()) && date.before(list.get(i+1).getCalendar()))
                return i;
        }
        if(list.get(list.size()-1).getCalendar().equals(date))
            return list.size()-2;
        return date.before(list.get(0).getCalendar())?-1:list.size()-1; // 1인데 양끝 범위 여기서 처리됨
    }

    public void getDbData(Calendar c) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from myRecord where record_date like ?");
        String[] params = {c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE)};
        Cursor cursor = db.rawQuery(sb.toString(), params);

        String weiValue = "", exValue = "", foValue = "";

        while (cursor.moveToNext()) {
            weiValue = cursor.getString(2);
            exValue = cursor.getString(3);
            foValue = cursor.getString(4);
        }
        if(!weiValue.equals("")) {
            weight.setText(weiValue);
            food.setRating(Float.parseFloat(foValue));
            exercise.setRating(Float.parseFloat(exValue));
        } else {
            weight.setText(null);
            food.setRating((float)3);
            exercise.setRating((float)3);
        }
    }
}
