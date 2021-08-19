package com.example.mydietapp.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.example.mydietapp.MainActivity;
import com.example.mydietapp.R;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) { // 폰 껐다 켰을때 사라진 설정값 부여
//
//        }
        //알림(Notification)을 관리하는 관리자 객체를 운영체제(Context)로부터 소환하기
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder= null;

        Intent intent2=new Intent(context, MainActivity.class);
        intent2.setAction(Intent.ACTION_MAIN);
        intent2.addCategory(Intent.CATEGORY_LAUNCHER);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // notification에는 직접적으로 intent 전달 못하므로 pendingIntent로 감싸서 전달함
        PendingIntent pendingIntent=PendingIntent.getActivity(context,1,intent2,PendingIntent.FLAG_UPDATE_CURRENT);

        //Oreo 버전(API26 버전)이상에서는 알림시에 NotificationChannel 이라는 개념이 필수 구성요소가 됨
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelID="ID1"; //알림채널 식별자
            String channelName="channel1"; //알림채널의 이름(별명)

            NotificationChannel channel= new NotificationChannel(channelID,channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder=new NotificationCompat.Builder(context, channelID);
        }else{
            builder= new NotificationCompat.Builder(context);
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
}
