package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.MenuListViewOwnerAdapter;
import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.Request;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.OrderListViewAdapter;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.RestGetOrders;
import com.example.yaneodoo.REST.RestGetUser;
import com.example.yaneodoo.RetrofitService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.yaneodoo.RetrofitService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowOwnerOrderList extends AppCompatActivity {
    private Intent intent;
    private String token, id, name;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private OrderListViewAdapter adapter = new OrderListViewAdapter();
    private ListView listview;

    private List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_order_list_owner);

        final SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);
        listview = (ListView) findViewById(R.id.order_list_view_owner);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");
        name = getSharedPreferences("sFile", MODE_PRIVATE).getString("fullName", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        // ShowOwnerMenuList에서 보낸 titleStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        final Store store = (Store) intent.getSerializableExtra("bistroInfo");
        token = tk.getString("bistrotk","");

        getOrderList(token, store.getId());//가게의 주문내역 불러오기
        //TODO : 날짜 최신순

        TextView titleTxtView = (TextView) findViewById(R.id.title_txtView);
        titleTxtView.setText(name+" 점주님의\n"+store.getName()+" 주문내역입니다.");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // 주문 아이템 하나 선택해도 일단은 아무것도 안함.
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerOrderList.this, ShowOwnerBistroList.class);
                ShowOwnerOrderList.this.finish();
                startActivity(intent);
            }
        });

        ImageButton btnMyPage = (ImageButton) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerOrderList.this, MyPageOwner.class);
                startActivity(intent);
            }
        });
    }

    // 주문접수 토글 버튼 클릭 리스너
    public void progressToggle(View v) {
        LinearLayout parentRow = (LinearLayout) v.getParent();
        TextView orderState = (TextView) parentRow.findViewById(R.id.order_progress);

        LinearLayout pparentRow = (LinearLayout) parentRow.getParent();
        TextView orderDate = (TextView) pparentRow.findViewById(R.id.order_date_txtView);
        TextView orderCustomerId = (TextView) pparentRow.findViewById(R.id.order_customer_txtView);

        ToggleButton tb2 = (ToggleButton) v.findViewById(R.id.btn_progress);
        Log.d("tag", orderState.getText().toString());
        if (orderState.getText().toString() == "접수중") {
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.accepted));
            orderState.setText("접수 완료");
        } else if (orderState.getText().toString() == "접수 완료") {
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.requested));
            orderState.setText("접수중");
        }
    }

    private void checkOrder(final String token, String storeId, final SharedPreferences sp){
        service.getStoreOrder("Bearer " + token, storeId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Order body = response.body();
                    if (body != null) {
                        Order order = new Order();
                        order.setId(body.getId());
                        sp.getString("orderId", "");
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                t.printStackTrace();
                Log.d("fail", "======================================");
            }
        });
    }

    private void getOrderList(final String token, String storeId) {
        service.getStoreOrders("Bearer " + token, storeId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> body = response.body();
                    if (body != null) {
                        for (int i = 0; i < body.size(); i++) {
                            Order order = new Order();
                            order.setId(body.get(i).getId());
                            order.setProgress(body.get(i).getProgress());
                            order.setTableNum(body.get(i).getTableNum());
                            order.setDate(body.get(i).getDate());
                            order.setUserId(body.get(i).getUserId());
                            order.setRequest(body.get(i).getRequests());
                            orderList.add(order);
                            RestGetUser restGetUser = new RestGetUser(order.getUserId(),token);
                            String name = "";
                            try {
                                name = restGetUser.execute().get();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            String requests = "";
                            String amount = "";
                            String menu = "";
                            for( int j = 0 ; j < order.getRequests().size() ; j++ ){
                                amount = order.getRequests().get(j).getAmount();
                                menu = String.valueOf(order.getRequests().get(j).getMenu().getName());
                                requests += menu + " x " + amount + "\n";
                                Log.d("requests", requests);
                            }
                            requests = requests.substring(0,requests.length()-1);

                            if(order.getProgress().equals("REQUESTED"))
                                adapter.addItem(ContextCompat.getDrawable(ShowOwnerOrderList.this, R.drawable.requested), String.valueOf(order.getDate()).substring(4,10)+"\n"+String.valueOf(order.getDate()).substring(11,19), name, requests, "접수중",order.getId());
                            else
                                adapter.addItem(ContextCompat.getDrawable(ShowOwnerOrderList.this, R.drawable.accepted), String.valueOf(order.getDate()).substring(4,10)+"\n"+String.valueOf(order.getDate()).substring(11,19), name, requests, "접수 완료",order.getId());
                            Log.d("menu data", "--------------------------------------");
                        }
                        Log.d("getMenuList end", "======================================");
                        listview.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                t.printStackTrace();
                Log.d("fail", "======================================");
            }
        });
    }
}