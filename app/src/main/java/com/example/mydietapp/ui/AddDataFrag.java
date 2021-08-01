package com.example.mydietapp.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mydietapp.R;
import com.example.mydietapp.custom.NumberDecimalInputFilter;
import com.example.mydietapp.db.DbHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AddDataFrag extends Fragment {
    private static CalendarDay day;
    private static HashSet<CalendarDay> dataSet=new HashSet<>();

    private DbHelper helper;
    private SQLiteDatabase db;

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static AddDataFrag newInstance(CalendarDay date, Set<CalendarDay> set) {
        day=date;
        dataSet.addAll(set);
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
        Button datePicker=v.findViewById(R.id.datePickerButton);
        ImageButton previous=v.findViewById(R.id.previousDateButton);
        ImageButton next=v.findViewById(R.id.nextDateButton);

        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd");
        String time1 = format.format(day.getCalendar().getTime());
        datePicker.setText(time1);

        Calendar c=day.getCalendar();
        EditText e=v.findViewById(R.id.weightEditText);
        RatingBar food=v.findViewById(R.id.foodRating);
        RatingBar exercise=v.findViewById(R.id.exerciseRating);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE, -1);
                datePicker.setText(format.format(c.getTime()));

                System.out.println("ok:" + c.get(Calendar.YEAR) + " " + (c.get(Calendar.MONTH) + 1) + " " + c.get(Calendar.DATE));

                StringBuffer sb = new StringBuffer();
                sb.append("select * from myRecord where record_date like ?");
                String[] params = {c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + "%"};
                Cursor cursor = db.rawQuery(sb.toString(), params);
                String weiValue = "", exValue = "", foValue = "";

                while (cursor.moveToNext()) {
                    weiValue = cursor.getString(2);
                    exValue = cursor.getString(3);
                    foValue = cursor.getString(4);
                }
                if(!weiValue.equals("")) {
                    e.setText(weiValue);
                    food.setRating(Float.parseFloat(foValue));
                    exercise.setRating(Float.parseFloat(exValue));
                } else {
                        e.setText(null);
                        food.setRating((float)3);
                        exercise.setRating((float)3);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE,1);
                datePicker.setText(format.format(c.getTime()));
            }
        });


        return v;
    }
}
