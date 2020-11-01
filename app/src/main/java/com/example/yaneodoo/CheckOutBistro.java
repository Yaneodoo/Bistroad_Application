package com.example.yaneodoo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.yaneodoo.Customer.MyPageCustomer;

public class CheckOutBistro extends Service {
    private static final String TAG = "CheckOutBistro";
    NotificationManager Notifi_M;
    GPSThread thread;
    GPSTracker gpsTracker;
    float lat;
    float lon;
    int count;
    SharedPreferences tk;
    float preLat;
    float preLon;

    public CheckOutBistro() {
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
        Log.d(TAG, "onStartCommand() called");
        tk = getSharedPreferences("sFile", MODE_PRIVATE);
        gpsTracker = new GPSTracker(CheckOutBistro.this);

        if (intent == null) {
            return Service.START_STICKY; //서비스가 종료되어도 자동으로 다시 실행시켜줘!
        } else {
            Notifi_M = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            BistroCheckOutServiceHandler handler = new BistroCheckOutServiceHandler();
            thread = new GPSThread( handler, gpsTracker, tk);
            thread.start();
            //thread.stopForever();
            Log.d(TAG, "onStartCommand() else");
            return START_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        //Log.d(TAG, "onDestroy() called");
        BistroCheckOutServiceHandler handler = new BistroCheckOutServiceHandler();
        thread = new GPSThread( handler , gpsTracker, tk);
        thread.start();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class GPSThread extends Thread{
        Handler handler;
        boolean isRun = true;
        GPSTracker gpsTracker;
        SharedPreferences tk;
        Message msg;
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
            double distance = 0;

            preLat = tk.getFloat("storeLat",0);
            preLon = tk.getFloat("storeLon",0);

            while (isRun) {
                if (tk.getString("bId", "").length() == 0){
                    stopForever();
                    break;
                }
                gpsTracker.getLocation();
                lat = (float)gpsTracker.getLatitude();
                lon = (float)gpsTracker.getLongitude();

                distance = Math.sqrt(Math.pow(Math.abs(preLat-lat), 2) + Math.pow(Math.abs(preLon-lon), 2));

                Log.d(TAG, String.valueOf(lat)+", "+String.valueOf(lon));

                if(count > 30 && distance > 0.001) {
                    msg = new Message();
                    handler.sendEmptyMessage(0);

                    SharedPreferences.Editor editor = tk.edit();
                    editor.putString("checkPush", "0");
                    editor.commit();
                    count = 0;
                    stopForever();
                    break;
                }

                try {
                    Thread.sleep( 1000 );
                    count += 10;
                    //// 위는 테스트 아래는 실적용 ////
//                    Thread.sleep( 60000 );
//                    count += 1;
                    //1분씩 쉰다.
                } catch (Exception e) {

                }
            }
        }
    }

    public class BistroCheckOutServiceHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String sName = "맛있는 시간 되셨나요? 리뷰 부탁드립니다!";
            //Log.d("handler sname", sName);
            String NOTIFICATION_ID = "10001";
            String NOTIFICATION_NAME = "리뷰남기기";
            //채널 생성
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Bistroad");
                notificationManager.createNotificationChannel(channel);
            }

            Intent intent = new Intent(CheckOutBistro.this, MyPageCustomer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(CheckOutBistro.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri soundUri = RingtoneManager.getDefaultUri((RingtoneManager.TYPE_NOTIFICATION));
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( CheckOutBistro.this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Bistroad")
                    .setContentText(sName)
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOnlyAlertOnce(true)
                    .setChannelId(NOTIFICATION_ID)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(1,notificationBuilder.build());
        }
    }
}
