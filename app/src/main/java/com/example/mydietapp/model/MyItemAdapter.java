package com.example.mydietapp.model;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import com.example.mydietapp.R;
import com.example.mydietapp.ui.AlertReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MyItemAdapter extends BaseAdapter {
    private List<MyItem> data;
    private int layout;
    private LayoutInflater inflater;
    private Context context;

    private TimePicker timePicker;

    public MyItemAdapter(Context context, int layout, List<MyItem> data) {
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout=layout;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(layout, parent, false);
        ImageView image=convertView.findViewById(R.id.list_image);
        TextView text=convertView.findViewById(R.id.list_text);
        SwitchCompat switchCompat=convertView.findViewById(R.id.list_switch);

        MyItem item=data.get(position);
        image.setImageResource(item.getIcon());
        text.setText(item.getText());

        SwitchCompat switchView=convertView.findViewById(R.id.list_switch);
        switchView.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("alarmFile", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();

                Intent intent = new Intent(context, AlertReceiver.class);
                PendingIntent pIntent = PendingIntent.getBroadcast(context, 1,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                if(isChecked) { // 체크 x->체크 o
                    Calendar c=Calendar.getInstance();

                    AlertDialog.Builder builder1= new AlertDialog.Builder(context);
                    builder1.setTitle("시간 선택");
                    builder1.setView(R.layout.dialog_timepicker);

                    builder1.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switchView.setChecked(false);
                        }
                    });
                    builder1.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            c.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                            c.set(Calendar.MINUTE,timePicker.getMinute());
                            c.set(Calendar.SECOND,0);

                            SimpleDateFormat format=new SimpleDateFormat("hh:mm");
                            editor.putString("time",format.format(c.getTime()));
                            editor.apply();

                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pIntent);
                        }
                    });
                    AlertDialog dialog=builder1.create();
                    dialog.setCanceledOnTouchOutside(true); //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
                    dialog.show();

                    timePicker=dialog.findViewById(R.id.timePicker);

                } else { // 체크 o->체크 x
                    // 알람 삭제
                    alarmManager.cancel(pIntent);
                    // SharedPreferences 값 null로 update
                    sharedPreferences.getString("time",null);
                }
            }
        });
        if(position!=0)
            switchCompat.setVisibility(View.GONE);
        return convertView;
    }
}
