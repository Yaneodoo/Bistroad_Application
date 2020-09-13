package com.example.yaneodoo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
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
import com.example.yaneodoo.REST.RestGetNearestStore;
import com.example.yaneodoo.REST.RestGetUserInfo;

import java.util.concurrent.ExecutionException;

public class GetCurrentGPSService extends Service {
    private static final String TAG = "GetCurrentGPSService";
    NotificationManager Notifi_M;
    GPSThread thread;
    GPSTracker gpsTracker;
    float lat;
    float lon;
    int count;
    SharedPreferences tk;

    public GetCurrentGPSService() {
    }

    @Override
    public void onCreate() {
        // 서비스는 한번 실행되면 계속 실행된 상태로 있는다.
        // 따라서 서비스 특성상 intent를 받아서 처리하기에 적합하지않다.
        // intent에 대한 처리는 onStartCommand()에서 처리해준다.
        //Log.d(TAG, "onCreate() called");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d(TAG, "onStartCommand() called");
        tk = getSharedPreferences("sFile", MODE_PRIVATE);
        gpsTracker = new GPSTracker(GetCurrentGPSService.this);
        if (intent == null) {
            return Service.START_STICKY; //서비스가 종료되어도 자동으로 다시 실행시켜줘!
        } else {
            Notifi_M = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            myServiceHandler handler = new myServiceHandler();
            thread = new GPSThread( handler, gpsTracker, tk);
            thread.start();
            //thread.stopForever();
            //Log.d(TAG, "onStartCommand() else");
            return START_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        //Log.d(TAG, "onDestroy() called");
        myServiceHandler handler = new myServiceHandler();
        thread = new GPSThread( handler , gpsTracker, tk);
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
        GPSTracker gpsTracker;
        SharedPreferences tk;
        Message msg;
        String sName;
        public GPSThread(Handler handler, GPSTracker gpsTracker, SharedPreferences tk) {
            this.handler = handler;
            this.gpsTracker = gpsTracker;
            this.tk = tk;
        }
        public void stopForever() {
            synchronized (this) {
                this.isRun = false;
            }
        }
        public void run() {
            //반복적으로 수행할 작업을 한다.
            count = 0;
            while (isRun) {
                if (tk.getString("bId", "").length() == 0){
                    stopForever();
                    break;
                }
                gpsTracker.getLocation();
                lat = (float)gpsTracker.getLatitude();
                lon = (float)gpsTracker.getLongitude();

                //Log.d(TAG, String.valueOf(lat)+", "+String.valueOf(lon));

                String token = tk.getString("bistrotk", "");
                float preLat = tk.getFloat("lat", 0);
                float preLon = tk.getFloat("lon", 0);

                if(lat == preLat && lon == preLon) {
                    count ++;
                    //쓰레드에 있는 핸들러에게 메세지를 보냄
                    RestGetNearestStore restGetNearestStore = new RestGetNearestStore(lat, lon);
                    try {
                        sName = restGetNearestStore.execute().get();
                        //Log.d("Store", sName);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(sName != "noStore" && count > 2) {
                        msg = new Message();
                        msg.obj = sName;
                        handler.sendMessage(msg);
                        count = 0;
                    }
                }
                else
                    Log.d(TAG, "Location has been changed.");

                SharedPreferences.Editor editor = tk.edit();
                editor.putFloat("lat", lat);
                editor.putFloat("lon", lon);
                editor.commit();

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
            String sName = String.valueOf(msg.obj)+"에 입장하셨나요?";
            //Log.d("handler sname", sName);
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
                    .setContentText(sName)
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOnlyAlertOnce(true)
                    .setChannelId(NOTIFICATION_ID);
            notificationManager.notify(1,notificationBuilder.build());
        }
    }
}
