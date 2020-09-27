package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.ListView.OrderListViewAdapter;
import com.example.yaneodoo.ListView.OrderListViewItem;
import com.example.yaneodoo.Login;
import com.example.yaneodoo.R;
import com.example.yaneodoo.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyPageOrderMenu extends AppCompatActivity {
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
        setContentView(R.layout.mypage_customer);
        final SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);
        listview = (ListView) findViewById(R.id.order_list_view_customer);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");
        name = getSharedPreferences("sFile", MODE_PRIVATE).getString("fullName", "");
        id = getSharedPreferences("sFile", MODE_PRIVATE).getString("id", "");
        final TextView nameText = (TextView)findViewById(R.id.cutomer_name_textView);
        nameText.setText(name+" 고객님");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);
        Log.d("MyPageToken",token);
        //TODO : 날짜 최신순
        getOrderList(token, id);//자신의 주문내역 불러오기

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                OrderListViewItem order = (OrderListViewItem) parent.getItemAtPosition(position);
                Order listOrder = new Order();
                listOrder.setStoreId(orderList.get(position).getStoreId());
                listOrder.setId(orderList.get(position).getId());
                listOrder.setRequest(orderList.get(position).getRequests());
                listOrder.setUserId(orderList.get(position).getUserId());

                Log.d("listOrder", listOrder.toString());
                Intent intent = new Intent(MyPageOrderMenu.this, MyPageLeaveReview.class);
                intent.putExtra("menuInfo", listOrder);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageOrderMenu.this, ShowCustomerBistroList.class);
                MyPageOrderMenu.this.finish();
                startActivity(intent);
            }
        });

        Button btnCustomer = (Button) findViewById(R.id.mypage_logout_button);
        btnCustomer.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = tk.edit();
                editor.putString("bId", ""); //
                editor.commit();
                ActivityCompat.finishAffinity(MyPageOrderMenu.this);
                Intent intent = new Intent(MyPageOrderMenu.this, Login.class);
                startActivity(intent);
                MyPageOrderMenu.this.finish();
            }
        });
    }

    private void getOrderList(final String token, String userId) {
        service.getUserOrders("Bearer " + token, userId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Log.d("onResponse", "onResponse");
                if (response.isSuccessful()) {
                    Log.d("response issuccessful", "isSuccessful");
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
                                adapter.addItem(ContextCompat.getDrawable(MyPageOrderMenu.this, R.drawable.requested), String.valueOf(order.getDate()).substring(4,10)+"\n"+String.valueOf(order.getDate()).substring(11,19), name, requests, "접수중",order.getId());
                            else
                                adapter.addItem(ContextCompat.getDrawable(MyPageOrderMenu.this, R.drawable.accepted), String.valueOf(order.getDate()).substring(4,10)+"\n"+String.valueOf(order.getDate()).substring(11,19), name, requests, "접수 완료",order.getId());
                            Log.d("menu data", "--------------------------------------");
                        }
                        Log.d("getmyOrderList end", "======================================");
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

    // 주문접수 토글 버튼 클릭 리스너
    public void progressToggle(View v) {
        //do nothing
    }
}