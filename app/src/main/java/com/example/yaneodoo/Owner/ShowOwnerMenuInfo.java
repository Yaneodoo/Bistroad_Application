package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Review;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.ReviewListViewAdapter;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetImage;
import com.example.yaneodoo.RetrofitService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowOwnerMenuInfo extends AppCompatActivity {
    private Intent intent;
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private String writerName = "";

    private List<Review> reviewList = new ArrayList<>();
    private ListView listview;
    private ReviewListViewAdapter adapter = new ReviewListViewAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_info_owner);

        listview = (ListView) findViewById(R.id.review_list_view_owner);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        intent = getIntent();
        final Menu menu = (Menu) intent.getSerializableExtra("menuInfo");
        final User owner = (User) intent.getSerializableExtra("ownerInfo");
        final Store store = (Store) intent.getSerializableExtra("bistroInfo");

        Call<List<Review>> callgetReviewList = service.getReviewList("Bearer " + token, menu.getStoreId(), menu.getId());
        new callgetReviewList().execute(callgetReviewList);

        GetImage getUserImage = new GetImage();
        if(owner.getPhoto()!=null){
            Bitmap bitmap = null;
            try {
                bitmap = getUserImage.execute(owner.getPhoto().getThumbnailUrl()).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ImageButton btnMyPage = (ImageButton)findViewById(R.id.mypagebtn);
            btnMyPage.setImageBitmap(bitmap);
        }

        TextView menuNameTxtView = (TextView) findViewById(R.id.menu_name_txtView);
        menuNameTxtView.setText(menu.getName());
        TextView menuPriceTxtView = (TextView) findViewById(R.id.menu_price_txtView);
        menuPriceTxtView.setText(menu.getPrice());
        TextView menuDescTxtView = (TextView) findViewById(R.id.menu_desc_txtView);
        menuDescTxtView.setText(menu.getDescription());
        TextView menuStarsTxtView = (TextView) findViewById(R.id.menu_stars_txtView);
        menuStarsTxtView.setText(menu.getStars());
        TextView menuAmountTxtView = (TextView) findViewById(R.id.menu_orderedCnt_txtView);
        menuAmountTxtView.setText("주문 횟수 : "+menu.getOrderCount().toString());

        GetImage getMenuImage = new GetImage();
        Bitmap sbitmap = null;
        if(menu.getPhoto()!=null){
            try {
                sbitmap = getMenuImage.execute(menu.getPhoto().getSourceUrl()).get();
                ImageView menuRepresentImage=(ImageView) findViewById(R.id.menu_image);
                menuRepresentImage.setImageBitmap(sbitmap);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Button editbutton = (Button) findViewById(R.id.btn_edit);

        // 수정 버튼 클릭 리스너
        editbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShowOwnerMenuInfo.this, RegisterMenu.class);
                intent.putExtra("menuInfo", menu);
                intent.putExtra("ownerInfo", owner);
                intent.putExtra("bistroInfo", store);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuInfo.this, ShowOwnerBistroList.class);
                ShowOwnerMenuInfo.this.finish();
                startActivity(intent);
            }
        });

        ImageButton btnMyPage = (ImageButton) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuInfo.this, MyPageOwner.class);
                intent.putExtra("ownerInfo", owner);
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
}