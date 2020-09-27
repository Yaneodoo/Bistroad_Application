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
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Customer.ShowCustomerBistroList;
import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Owner.ShowOwnerOrderList;
import com.example.yaneodoo.REST.RestGetUser;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetRealtimeOrderService extends Service {
    private static final String TAG = "GetRealtimeOrderService";
    NotificationManager Notifi_M;
    OrderThread thread;
    int count;
    SharedPreferences tk;

    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private String oldOrderId = "";
    private String newOrderId = "";
    private String storeId = "";
    private String [] sort = new String[2];

    public GetRealtimeOrderService() {
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
        oldOrderId = tk.getString("orderId", "noOrder");
        sort[0] = "date";
        sort[1] = "asc";
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);
        if (intent == null) {
            return Service.START_STICKY; //서비스가 종료되어도 자동으로 다시 실행시켜줘!
        } else {
            Notifi_M = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            myServiceHandler handler = new myServiceHandler();
            thread = new OrderThread( handler, tk);
            thread.start();
            //thread.stopForever();
            Log.d(TAG, "onStartCommand() else");
            return START_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        //Log.d(TAG, "onDestroy() called");
        myServiceHandler handler = new myServiceHandler();
        thread = new OrderThread( handler, tk);
        thread.start();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void checkOrder(final String token, String storeId, final SharedPreferences sp){
        service.getStoreOrder("Bearer " + token, storeId, "date,desc", "1").enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> body = response.body();
                    if (body != null) {
                        Order order = new Order();
                        order.setId(body.get(0).getId());
                        Log.d("orderId",order.getId());

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("orderId", order.getId()); //
                        editor.commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "check order failed");
            }
        });
    }

    public class OrderThread extends Thread{
        Handler handler;
        boolean isRun = true;
        SharedPreferences tk;
        String oName;
        public OrderThread(Handler handler, SharedPreferences tk) {
            this.handler = handler;
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
            oName = "noOrder";
            while (isRun) {
                try {
                    Thread.sleep( 5000 );
                    //10초씩 쉰다.
                } catch (Exception e) {
                }
                if (tk.getString("storeId", "").length() == 0){
                    stopForever();
                    break;
                }

                //Log.d(TAG, String.valueOf(lat)+", "+String.valueOf(lon));

                String token = tk.getString("bistrotk", "");
                String sId = tk.getString("storeId", "noStore");

                checkOrder(token, sId, tk);

                newOrderId = tk.getString("orderId", "noOrder");
                storeId = tk.getString("storeId", "");
                Log.d("New Order", newOrderId);
                Log.d("Old Order", oldOrderId);
                if(!newOrderId.equals(oldOrderId)){
                    handler.sendEmptyMessage(0);
                    oldOrderId = newOrderId;
                }
            }
        }
    }

    public class myServiceHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String oName = "새로운 주문이 도착했습니다.";
            //Log.d("handler sname", sName);
            String NOTIFICATION_ID = "10002";
            String NOTIFICATION_NAME = "주문 가져오기";
            //채널 생성
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Bistroad");
                notificationManager.createNotificationChannel(channel);
            }

            Intent intent = new Intent(GetRealtimeOrderService.this, ShowCustomerBistroList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(GetRealtimeOrderService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri soundUri = RingtoneManager.getDefaultUri((RingtoneManager.TYPE_NOTIFICATION));
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( GetRealtimeOrderService.this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Bistroad")
                    .setContentText(oName)
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
