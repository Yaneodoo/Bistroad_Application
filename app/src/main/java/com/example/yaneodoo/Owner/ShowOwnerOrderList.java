package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.OrderListViewAdapter;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.RestGetUser;
import com.example.yaneodoo.RetrofitService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ShowOwnerOrderList extends AppCompatActivity {
    private Intent intent;
    private String token, name;
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

        intent = getIntent();
        final User owner = (User) intent.getSerializableExtra("ownerInfo");
        final Store store = (Store) intent.getSerializableExtra("bistroInfo");
        token = tk.getString("bistrotk","");

        getOrderList(token, store.getId(), "date,desc");//가게의 주문내역 불러오기
        Log.d("GETORDERLIST", store.getId());

        TextView titleTxtView = (TextView) findViewById(R.id.title_txtView);
        titleTxtView.setText(owner.getFullName()+" 점주님의\n"+store.getName()+" 주문내역입니다.");

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
                intent.putExtra("ownerInfo", owner);
                startActivity(intent);
            }
        });
    }

    // 주문접수 토글 버튼 클릭 리스너
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void progressToggle(View v) {
        LinearLayout parentRow = (LinearLayout) v.getParent();
        TextView orderState = (TextView) parentRow.findViewById(R.id.order_progress);
        int position = listview.getPositionForView(v);

        ToggleButton tb2 = (ToggleButton) v.findViewById(R.id.btn_progress);
        Log.d("tag", orderState.getText().toString());
        if (orderState.getText().toString() == "접수중") {
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.accepted));
            orderState.setText("접수 완료");
            orderList.get(position).setProgress("ACCEPTED");
        } else if (orderState.getText().toString() == "접수 완료") {
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.requested));
            orderState.setText("접수중");
            orderList.get(position).setProgress("REQUESTED");
        }

        Call<Order> callPatchOrder = service.patchOrder("Bearer " + token, orderList.get(position), orderList.get(position).getId());
        try {
            new patchOrder().execute(callPatchOrder).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getOrderList(final String token, String storeId, String sort) {
        service.getStoreOrders("Bearer " + token, storeId, sort).enqueue(new Callback<List<Order>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> body = response.body();
                    if (body != null) {
                        Log.d("requests", body.toString());
                        for (int i = 0; i < body.size(); i++) {
                            Order order = new Order();
                            order.setId(body.get(i).getId());
                            order.setProgress(body.get(i).getProgress());
                            order.setTableNum(body.get(i).getTableNum());
                            order.setTimestamp(body.get(i).getTimestamp());
                            order.setUserId(body.get(i).getUserId());
                            order.setRequest(body.get(i).getRequests());
                            order.setHasReview(body.get(i).getHasReview());
                            order.setStore(body.get(i).getStore());
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
                                amount = order.getRequests().get(j).getAmount().toString();
                                menu = String.valueOf(order.getRequests().get(j).getMenu().getName());
                                requests += menu + " x " + amount + "\n";
                            }
                            requests = requests.substring(0,requests.length()-1);
                            Log.d("requests complete", requests);

                            if(order.getProgress().equals("REQUESTED"))
                                adapter.addItem(ContextCompat.getDrawable(ShowOwnerOrderList.this, R.drawable.requested),
                                        order.getTimestamp(), name, requests, "접수중",order.getId(),order.getTableNum());
                            else
                                adapter.addItem(ContextCompat.getDrawable(ShowOwnerOrderList.this, R.drawable.accepted),
                                        order.getTimestamp(), name, requests, "접수 완료",order.getId(),order.getTableNum());
                            Log.d("menu data", "--------------------------------------");
                        }
                        Log.d("getMenuList end", "======================================");
                        listview.setAdapter(adapter);
                    }
                    int statusCode  = response.code();
                    Log.d("GET ORDER CODE",Integer.toString(statusCode));
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                t.printStackTrace();
                Log.d("fail", "======================================");
            }
        });
    }

    private class patchOrder extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Order> call = params[0];
                Response<Order> response = call.execute();
                Order order = response.body();

                if (order == null){
                    int statusCode  = response.code();
                    Log.d("CODE",Integer.toString(statusCode));
                }
                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
}