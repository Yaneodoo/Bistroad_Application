package com.example.yaneodoo.Owner;

import android.content.Intent;
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

public class ShowOwnerOrderList extends AppCompatActivity {
    private Intent intent;
    private String token, id, role, orderInfo;
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

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        // ShowOwnerMenuList에서 보낸 titleStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        id = intent.getStringExtra("bistroStr");
        token = tk.getString("bistrotk","");
        role = tk.getString("role", "");

        RestGetOrders restGetOrders = new RestGetOrders(id, token, role);
        try {
            orderInfo = restGetOrders.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("orderInfo", orderInfo);

        getOrderList(token, id);//가게의 메뉴 불러오기

        // TODO : GET /stores/{storeId}/orders로 가게의 주문내역을 모두 아이템에 추가
        // 날짜 최신순

        // 아이템 추가 예시
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "horseesroh", "짜장 x 1\n짬뽕 x 2", "접수중","");
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "elsiff", "짜장 x 1\n짬뽕 x 2", "접수중","");
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "himinju", "짜장 x 1\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2", "접수중","");
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "vomin", "짜장 x 1\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2", "접수중","");
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "Yalru", "짜장 x 1\n짬뽕 x 2", "접수중","");
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "tjwlsgkkgslwjt", "짜장 x 1\n짬뽕 x 2", "접수중","");
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "2020-02-09", "tjwlsgkkgslwjt", "짜장 x 1\n짬뽕 x 2", "접수 완료","");

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
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
                            order.setRequests(body.get(i).getRequests());
                            orderList.add(order);
                            RestGetUser restGetUser = new RestGetUser(order.getUserId(),token);
                            String name ="";
                            try {
                                name = restGetUser.execute().get();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            if(order.getProgress().equals("REQUESTED"))
                                adapter.addItem(ContextCompat.getDrawable(ShowOwnerOrderList.this, R.drawable.requested), String.valueOf(order.getDate()).substring(4,10)+"\n"+String.valueOf(order.getDate()).substring(11,19), name, "짜장 x 1\n짬뽕 x 2", "접수 대기",order.getId());
                            else
                                adapter.addItem(ContextCompat.getDrawable(ShowOwnerOrderList.this, R.drawable.accepted), String.valueOf(order.getDate()).substring(4,10)+"\n"+String.valueOf(order.getDate()).substring(11,19), name, "짜장 x 1\n짬뽕 x 2", "접수 완료",order.getId());
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