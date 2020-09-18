package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Review;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.ReviewListViewAdapter;
import com.example.yaneodoo.R;
import com.example.yaneodoo.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowCustomerMenuInfo extends AppCompatActivity {
    private Intent intent;
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private String writerName = "";

    private ListView listview;

    private List<Review> reviewList = new ArrayList<>();
    private ReviewListViewAdapter adapter = new ReviewListViewAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_info_customer);

        listview = (ListView) findViewById(R.id.review_list_view_customer);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        intent = getIntent();
        final Menu menu = (Menu) intent.getSerializableExtra("menuInfo");
        final User user = (User) intent.getSerializableExtra("userInfo");

        Call<List<Review>> callgetReviewList = service.getReviewList("Bearer " + token, menu.getStoreId(), menu.getId());
        //Call<List<Review>> callgetReviewList = service.getReviewList("Bearer " + token);
        Log.d("MMMMMMMM", menu.getStoreId().toString() + "         " + menu.getId().toString());
        new callgetReviewList().execute(callgetReviewList);

        TextView menuNameTxtView = (TextView) findViewById(R.id.menu_name_txtView);
        menuNameTxtView.setText(menu.getName());
        TextView menuPriceTxtView = (TextView) findViewById(R.id.menu_price_txtView);
        menuPriceTxtView.setText(menu.getPrice());
        TextView menuDescTxtView = (TextView) findViewById(R.id.menu_desc_txtView);
        menuDescTxtView.setText(menu.getDescription());
        TextView menuStarsTxtView = (TextView) findViewById(R.id.menu_stars_txtView);
        menuStarsTxtView.setText(menu.getStars());

        Button btn_order = (Button) findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShowCustomerMenuInfo.this, ShowCustomerOrderForm.class);
                intent.putExtra("userInfo", user);
                intent.putExtra("menuInfo", menu);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerMenuInfo.this, ShowCustomerBistroList.class);
                ShowCustomerMenuInfo.this.finish();
                startActivity(intent);
            }
        });

        ImageButton btnMyPage = (ImageButton) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerMenuInfo.this, MyPageCustomer.class);
                startActivity(intent);
            }
        });

        // FAB 클릭 리스너
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowCustomerMenuInfo.this, ShowCustomerShoppingBasket.class);
                intent.putExtra("userInfo", user);
                startActivity(intent);
            }
        });
    }

    private class callgetReviewList extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<List<Review>> call = params[0];
                Response<List<Review>> response = call.execute();
                List<Review> body = response.body();
                if (body != null) {
                    for (int i = 0; i < body.size(); i++) {
                        Review review = new Review();
                        review.setContents(body.get(i).getContents());
                        review.setId(body.get(i).getId());
                        review.setOrderId(body.get(i).getOrderId());
                        review.setStars(body.get(i).getStars());
                        review.setWriterId(body.get(i).getWriterId());
                        reviewList.add(review);

                        Log.d("REVIEW", review.toString());
                    }
                    Log.d("review data", "--------------------------------------");
                } else {
                    Log.d("REVIEW", "EMPTY");
                }

                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            for (Review review : reviewList) {
                Call<User> callgetUser = service.getUser("Bearer " + token, review.getWriterId());
                new callgetUser().execute(callgetUser);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                adapter.addItem("2020.09.19", writerName, "★" + review.getStars(), review.getContents());
            }
        }
    }

    private class callgetUser extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<User> call = params[0];
                Response<User> response = call.execute();
                User body = response.body();
                writerName = body.getFullName();

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