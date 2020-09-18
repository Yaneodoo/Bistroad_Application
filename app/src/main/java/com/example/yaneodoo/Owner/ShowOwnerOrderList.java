package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.AsyncTask;
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

import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.Request;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.OrderListViewAdapter;
import com.example.yaneodoo.R;
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
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private ListView listview;
    private OrderListViewAdapter adapter = new OrderListViewAdapter();

    private List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_order_list_owner);

        listview = (ListView) findViewById(R.id.order_list_view_owner);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        intent = getIntent();
        final Store store = (Store) intent.getSerializableExtra("bistroInfo");
        final User owner = (User) intent.getSerializableExtra("ownerInfo");

        TextView titleTxtView = (TextView) findViewById(R.id.title_txtView);
        titleTxtView.setText(owner.getFullName() + " 점주님의\n" + store.getName() + " 주문내역입니다.");

        Call<List<Order>> callgetOrderList = service.getOrderList("Bearer " + token, store.getId());
        new callgetOrderList().execute(callgetOrderList);

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

    private class callgetOrderList extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<List<Order>> call = params[0];
                Response<List<Order>> response = call.execute();
                List<Order> body = response.body();
                if (body != null) {
                    for (int i = 0; i < body.size(); i++) {
                        Order order = new Order();
                        order.setDate(body.get(i).getDate());
                        order.setId(body.get(i).getId());
                        order.setProgress(body.get(i).getProgress());
                        order.setRequestList(body.get(i).getRequestList());
                        order.setStoreId(body.get(i).getStoreId());
                        order.setUserId(body.get(i).getUserId());

                        Log.d("ORDER", order.toString());
                        //orderList.add(order);
                        String o = "";
                        for (Request request : order.getRequestList()) {
                            o += request.getMenu() + " x " + request.getQuantity() + "\n";
                        }
                        if (order.getProgress() == "접수중") {
                            adapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.requested), "2020-09-19", order.getId(), o, order.getProgress());
                        } else { //접수됨
                            adapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.accepted), "2020-09-19", order.getId(), o, order.getProgress());
                        }
                    }
                    Log.d("orderList data", "--------------------------------------");
                }

                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            listview.setAdapter(adapter);
        }
    }
}