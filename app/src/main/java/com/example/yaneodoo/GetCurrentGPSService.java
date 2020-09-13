package com.example.yaneodoo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.yaneodoo.Customer.ShowCustomerBistroList;

public class GetCurrentGPSService extends Service implements LocationListener {
    private static final String TAG = "GetCurrentGPSService";
    NotificationManager Notifi_M;
    GPSThread thread;

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public GetCurrentGPSService(Context mContext) {
    }

    @Override
    public void onCreate() {
        // 서비스는 한번 실행되면 계속 실행된 상태로 있는다.
        // 따라서 서비스 특성상 intent를 받아서 처리하기에 적합하지않다.
        // intent에 대한 처리는 onStartCommand()에서 처리해준다.
        Log.d(TAG, "onCreate() called");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called");

        if (intent == null) {
            return Service.START_STICKY; //서비스가 종료되어도 자동으로 다시 실행시켜줘!
        } else {
            Notifi_M = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            myServiceHandler handler = new myServiceHandler();
            thread = new GPSThread( handler );
            thread.start();
            //thread.stopForever();
            Log.d(TAG, "onStartCommand() else");
            return START_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        myServiceHandler handler = new myServiceHandler();
        thread = new GPSThread( handler );
        thread.start();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public class GPSThread extends Thread{
        Handler handler;
        boolean isRun = true;
        public GPSThread(Handler handler) {
            this.handler = handler;
        }
        public void stopForever() {
            synchronized (this) {
                this.isRun = false;
            }
        }
        public void run() {
            //반복적으로 수행할 작업을 한다.
            while (isRun) {
                Log.d(TAG, "ThreadRun() called");
                handler.sendEmptyMessage( 0 );
                //쓰레드에 있는 핸들러에게 메세지를 보냄
                try {
                    Thread.sleep( 10000 );
                    //10초씩 쉰다.
                } catch (Exception e) {

                }
            }
        }
    }

    public class myServiceHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String NOTIFICATION_ID = "10001";
            String NOTIFICATION_NAME = "리뷰남기기";
            //채널 생성
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Bistroad");
                notificationManager.createNotificationChannel(channel);
            }

            Intent intent = new Intent(GetCurrentGPSService.this, ShowCustomerBistroList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(GetCurrentGPSService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri soundUri = RingtoneManager.getDefaultUri((RingtoneManager.TYPE_NOTIFICATION));
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( GetCurrentGPSService.this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Bistroad")
                    .setContentText("식당에 들어가셨나요?")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("식당에 들어가셨나요?"))
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOnlyAlertOnce(true)
                    .setChannelId(NOTIFICATION_ID);
            Log.d(TAG, "handleMessage() called");
            notificationManager.notify(1,notificationBuilder.build());
        }
    }
}
