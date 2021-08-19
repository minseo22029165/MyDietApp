package com.example.mydietapp.ui;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.mydietapp.MainActivity;
import com.example.mydietapp.R;
import com.example.mydietapp.db.DbHelper;
import com.example.mydietapp.model.MyItem;
import com.example.mydietapp.model.MyItemAdapter;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SettingFrag extends Fragment {
    private ListView listView;
    private MyItemAdapter adapter;
    private List<MyItem> itemArray;

    private DbHelper helper;
    private SQLiteDatabase db;

    public static Stack<Fragment> fragmentStack;
//    public static FragmentManager manager;

    public void createNotification() {
        //알림(Notification)을 관리하는 관리자 객체를 운영체제(Context)로부터 소환하기
        NotificationManager notificationManager=(NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder= null;

        Intent intent=new Intent(getActivity(),MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // notification에는 직접적으로 intent 전달 못하므로 pendingIntent로 감싸서 전달함
        PendingIntent pendingIntent=PendingIntent.getActivity(getActivity(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //Oreo 버전(API26 버전)이상에서는 알림시에 NotificationChannel 이라는 개념이 필수 구성요소가 됨
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelID="ID1"; //알림채널 식별자
            String channelName="channel1"; //알림채널의 이름(별명)

            NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder=new NotificationCompat.Builder(getActivity(), channelID);
        }else{
            builder= new NotificationCompat.Builder(getActivity());
        }
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("MyDietApp")
                .setContentText("오늘 데이터를 입력하세요!")
                .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(1, builder.build());

        //알림 요청시에 사용한 번호를 알림제거 할 수 있음.
        //notificationManager.cancel(1);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_setting,container,false);

        fragmentStack = new Stack<>();

        listView = v.findViewById(R.id.list_view);
        adapter=new MyItemAdapter(getActivity(),R.layout.list_item,setListViewData());
        listView.setAdapter((ListAdapter) adapter);

//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("alarmFile",Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        SwitchCompat listSwitch=listView.findViewById(R.id.list_switch);
//        System.out.println("checked:"+listSwitch.isChecked());
//        if(sharedPreferences.getString("time",null)==null)  // 값이 없다면
//            listSwitch.setChecked(false);
//        else
//            listSwitch.setChecked(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyItem item=itemArray.get(position);
                switch(item.getText()) {
                    case "시간 알림":
                        break;
                    case "모든 데이터 삭제":
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("모든 데이터를 삭제하시겠습니까?");
                        builder.setPositiveButton("삭제",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                helper = new DbHelper(getActivity(), "myDiet.db", null, 1);
                                db = helper.getWritableDatabase();
                                helper.onCreate(db);

                                db.delete("myRecord",null,null);
                                Toast.makeText(getActivity(), "모든 데이터가 삭제되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("취소",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    case "앱 정보":
                        MainActivity.fragmentStack.push(new SettingFrag());
//                        ((MainActivity)getActivity()).replaceFragment(AddDataFrag.newInstance(date,set));
                        MainActivity.manager.beginTransaction().replace(R.id.fragment_container,new AppInfoFrag()).commit();
                        break;
                }
            }
        });
        return v;
    }
    public List<MyItem> setListViewData() {
        itemArray=new ArrayList<>();
        MyItem item1=new MyItem(R.drawable.ic_access_alarm_black_24dp,"시간 알림");
        MyItem item2=new MyItem(R.drawable.ic_delete_black_24dp,"모든 데이터 삭제");
        MyItem item3=new MyItem(R.drawable.ic_info_outline_black_24dp,"앱 정보");
        itemArray.add(item1);
        itemArray.add(item2);
        itemArray.add(item3);
        return itemArray;
    }


}
