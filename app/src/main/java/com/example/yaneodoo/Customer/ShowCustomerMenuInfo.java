package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.ImageDialog;
import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Review;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.ReviewListViewAdapter;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetImage;
import com.example.yaneodoo.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.ExecutionException;

public class ShowCustomerMenuInfo extends AppCompatActivity {
    private Intent intent;
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private Bitmap sbitmap=null;

    private ListView listview;

    private List<Review> reviewList = new ArrayList<>();
    final ReviewListViewAdapter adapter = new ReviewListViewAdapter();

    private User user = new User();
    private Store store=new Store();

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
        user = (User) intent.getSerializableExtra("userInfo");
        store = (Store) intent.getSerializableExtra("bistroInfo");

        Call<List<Review>> callgetReviewList = service.getReviewList("Bearer " + token, menu.getStoreId(), menu.getId());
        new callgetReviewList().execute(callgetReviewList);

        Bitmap bitmap = null;
        GetImage getUserImage = new GetImage();
        try {
            if(user.getPhoto()!=null) {
                bitmap = getUserImage.execute(user.getPhoto().getThumbnailUrl()).get();
                de.hdodenhof.circleimageview.CircleImageView btnMyPage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.mypagebtn);
                btnMyPage.setImageBitmap(bitmap);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView menuNameTxtView = (TextView) findViewById(R.id.menu_name_txtView);
        menuNameTxtView.setText(menu.getName());
        TextView menuPriceTxtView = (TextView) findViewById(R.id.menu_price_txtView);
        menuPriceTxtView.setText(menu.getPrice().toString()+"원");
        TextView menuDescTxtView = (TextView) findViewById(R.id.menu_desc_txtView);
        menuDescTxtView.setText(menu.getDescription());
        TextView menuStarsTxtView = (TextView) findViewById(R.id.menu_stars_txtView);
        if(menu.getStars().equals("NaN"))
            menuStarsTxtView.setText("0.0");
        else
            menuStarsTxtView.setText(String.format("%.1f", Double.valueOf(menu.getStars())));

        GetImage getMenuImage = new GetImage();
        if(menu.getPhoto()!=null){
            try {
                sbitmap = getMenuImage.execute(menu.getPhoto().getThumbnailUrl()).get();
                ImageView menuRepresentImage=(ImageView) findViewById(R.id.menu_image);
                menuRepresentImage.setImageBitmap(sbitmap);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Button btn_order = (Button) findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShowCustomerMenuInfo.this, ShowCustomerOrderForm.class);
                intent.putExtra("userInfo", user);
                intent.putExtra("menuInfo", menu);
                intent.putExtra("bistroInfo",store);
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

        de.hdodenhof.circleimageview.CircleImageView btnMyPage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerMenuInfo.this, MyPageCustomer.class);
                intent.putExtra("userInfo", user);
                startActivity(intent);
            }
        });

        //원본 이미지 팝업 클릭 리스너
        ImageView menuImgView = (ImageView) findViewById(R.id.menu_image);
        menuImgView.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sbitmap!=null){
                    Intent intent = new Intent(ShowCustomerMenuInfo.this, ImageDialog.class);
                    intent.putExtra("menuInfo", menu);
                    startActivity(intent);
                }
            }
        });

        //리뷰 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Review review = (Review) parent.getItemAtPosition(position);

                if(review.getPhoto()!=null){
                    Intent intent = new Intent(ShowCustomerMenuInfo.this, ImageDialog.class);
                    intent.putExtra("reviewInfo", review);
                    startActivity(intent);
                }
            }
        });

        // FAB 클릭 리스너
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ReadShoppingBasketData().size() == 0) {
                    Toast.makeText(getApplicationContext(), "담은 메뉴가 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ShowCustomerMenuInfo.this, ShowCustomerShoppingBasket.class);
                    intent.putExtra("userInfo", user);
                    startActivity(intent);
                }
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
                        review.setTimestamp(body.get(i).getTimestamp());
                        review.setContents(body.get(i).getContents());
                        review.setId(body.get(i).getId());
                        review.setOrderId(body.get(i).getOrderId());
                        review.setStars(body.get(i).getStars());
                        review.setUser(body.get(i).getUser());
                        review.setWriter(body.get(i).getWriter());
                        review.setPhoto(body.get(i).getPhoto());
                        reviewList.add(review);

                        Log.d("REVIEW", review.getUser().toString());

                        adapter.addItem(review);
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
            listview.setAdapter(adapter);
        }
    }

    private ArrayList<Menu> ReadShoppingBasketData() {
        Gson gson = new Gson();
        String json = getSharedPreferences("sFile", MODE_PRIVATE).getString("SelectedMenu", "EMPTY");
        if (json != "EMPTY") {
            Type type = new TypeToken<ArrayList<Menu>>() {
            }.getType();
            ArrayList<Menu> arrayList = gson.fromJson(json, type);
            return arrayList;
        } else return new ArrayList<Menu>();
    }
}