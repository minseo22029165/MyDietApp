package com.example.mydietapp.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mydietapp.R;
import com.example.mydietapp.custom.NumberDecimalInputFilter;
import com.example.mydietapp.db.DbHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddDataFrag extends Fragment {
    private static CalendarDay day;
    private static HashSet<CalendarDay> dataSet=new HashSet<>();
    private static List<CalendarDay> list;

    private SimpleDateFormat format;
    private DbHelper helper;
    private SQLiteDatabase db;
    private Calendar c;

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
        list=new ArrayList<>(set);
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
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_data_top_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.delete);
//        CalendarDay cc=new CalendarDay(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
        Date dd= null;
        try {
            dd = format.parse(format.format(c.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!list.contains(new CalendarDay(dd))) {
            menuItem.setVisible(false);
        } else{
            menuItem.setVisible(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("현재 데이터를 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                list.remove(new CalendarDay(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE)));
                                Date dd=null;
                                try {
                                    dd = format.parse(format.format(c.getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                deleteDbData(c);
                                list.remove(new CalendarDay(dd));
                                getDbData(c);
                            }
                        })
                        .setNegativeButton("취소", null);
                builder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_adddata,container,false);
        setHasOptionsMenu(true);
        helper = new DbHelper(getActivity(), "myDiet.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        format = new SimpleDateFormat ( "yyyy-MM-dd");

        EditText edit=v.findViewById(R.id.weightEditText);
        edit.setFilters(new InputFilter[] {new NumberDecimalInputFilter(0,200,1)});
        edit.setFilters(new InputFilter[] {new NumberDecimalInputFilter(0,200,1)});
        Button datePicker=v.findViewById(R.id.datePickerButton);
        ImageButton previous=v.findViewById(R.id.previousDateButton);
        ImageButton next=v.findViewById(R.id.nextDateButton);

        String time1 = format.format(day.getCalendar().getTime());
        datePicker.setText(time1);

        c=day.getCalendar(); // 현재 선택된 날
        System.out.println("current:"+c);
        weight=v.findViewById(R.id.weightEditText);
        food=v.findViewById(R.id.foodRating);
        exercise=v.findViewById(R.id.exerciseRating);
        registButton=v.findViewById(R.id.registButton);
        previousWeightText=v.findViewById(R.id.previousWeightText);

        for(CalendarDay d:list) {
            System.out.println("ls:" + d.getDate());
        }

        getDbData(c);
        try {
            previousData(c);
        } catch (ParseException e) {
            System.out.println("exception 발생");
        }
        getActivity().invalidateOptionsMenu();

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
                        try {
                            previousData(c);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        getActivity().invalidateOptionsMenu();
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
                try {
                    previousData(c);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                getActivity().invalidateOptionsMenu();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE,1);
                datePicker.setText(format.format(c.getTime()));

                getDbData(c);
                try {
                    previousData(c);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                getActivity().invalidateOptionsMenu();
            }
        });
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weight.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "몸무게 값이 비워져있습니다.", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    Date dd=null;
                    try {
                        dd=format.parse(format.format(c.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                    CalendarDay cd=new CalendarDay(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                    CalendarDay cd=new CalendarDay(dd);
                    if(list.contains(cd)) {
                        builder.setMessage("현재 데이터를 덮어씌우시겠습니까?");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ContentValues values = new ContentValues();
                                values.put("weight",Float.parseFloat(weight.getText().toString()));
                                values.put("exercise",exercise.getRating());
                                values.put("food",food.getRating());
//                                db.update("myRecord",values,"record_date=?", new String[]{cd.getYear()+"-"+(cd.getMonth()+1)+"-"+cd.getDay()});
                                db.update("myRecord",values,"record_date=?", new String[]{format.format(cd.getDate())});
                            }
                        });

                    } else {
                        builder.setMessage("현재 데이터를 새로 등록하시겠습니까?");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ContentValues values = new ContentValues();
                                values.put("record_date",format.format(cd.getDate()));
                                values.put("weight",Float.parseFloat(weight.getText().toString()));
                                values.put("exercise",exercise.getRating());
                                values.put("food",food.getRating());
                                db.insert("myRecord",null,values);

                                list.add(cd);
                                Collections.sort(list, new Comparator<CalendarDay>() {
                                    @Override
                                    public int compare(CalendarDay t1, CalendarDay t2) { // 오름차순
                                        Calendar left=t1.getCalendar();
                                        Calendar right=t2.getCalendar();
                                        return left.compareTo(right);
                                    }
                                });
                                getActivity().invalidateOptionsMenu();
                            }
                        });
                    }
                    builder.setNegativeButton("취소", null);
                    builder.create().show();
                    getActivity().invalidateOptionsMenu();
                }
            }
        });


        return v;
    }
    public void previousData(Calendar c) throws ParseException {
        int r=getRange(list,c);
        if(r==-1) {
            previousWeightText.setText("");
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("select * from myRecord where record_date like ?");
//            String[] params = {list.get(r).getCalendar().get(Calendar.YEAR) + "-" + (list.get(r).getCalendar().get(Calendar.MONTH)+1) + "-" + list.get(r).getCalendar().get(Calendar.DATE)};
            String[] params = {format.format(list.get(r).getDate())};
            Cursor cursor = db.rawQuery(sb.toString(), params);

            String weiValue = "", exValue = "", foValue = "";

            while (cursor.moveToNext()) {
                weiValue = cursor.getString(2);
                exValue = cursor.getString(3);
                foValue = cursor.getString(4);
            }
            previousWeightText.setText("이전 몸무게는 "+weiValue+"kg,\n식사는 "+foValue+", 운동은 "+exValue+"입니다.");
        }
    }
    public int getRange(List<CalendarDay> list, Calendar date) throws ParseException {
        if(list.isEmpty())
            return -1;
        Date dd=format.parse(format.format(date.getTime()));
        Calendar cc=Calendar.getInstance();
        cc.setTime(dd);
        for(int i=0;i<list.size()-1;i++) { // size()==0 or 1 제외
            Calendar v1=list.get(i).getCalendar(); //2021-03-07
            if(v1.equals(cc)) {
//                return i == 0 ? -1 : list.indexOf(new CalendarDay(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE))) - 1;
                return i == 0 ? -1 : list.indexOf(new CalendarDay(dd)) - 1;
            }
//            if(date.after(list.get(i).getCalendar()) && date.before(list.get(i+1).getCalendar()))
            if(cc.after(list.get(i).getCalendar()) && cc.before(list.get(i+1).getCalendar()))
                return i;
        }
//        if(list.get(list.size()-1).getCalendar().equals(date))
        if(list.get(list.size()-1).getCalendar().equals(cc))
            return list.size()-2;
        return cc.before(list.get(0).getCalendar())?-1:list.size()-1; // 1인데 양끝 범위 여기서 처리됨

    }
    public void deleteDbData(Calendar c) {
//        db.delete("myRecord","record_date=?",new String[]{c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE)});
        db.delete("myRecord","record_date=?",new String[]{format.format(c.getTime())});
    }
    public void getDbData(Calendar c) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from myRecord where record_date like ?");
        String[] params = {format.format(c.getTime())};
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
