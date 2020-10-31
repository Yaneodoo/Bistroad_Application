package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.ImageDialog;
import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.Review;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.ReviewListViewAdapter;
import com.example.yaneodoo.Owner.MyPageOwner;
import com.example.yaneodoo.Owner.RegisterMenu;
import com.example.yaneodoo.Owner.ShowOwnerBistroList;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetImage;
import com.example.yaneodoo.RetrofitService;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyPageCheckReview extends AppCompatActivity {
    private Intent intent;
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private String writerName = "";

    private List<Review> reviewList = new ArrayList<>();
    private ListView listview;
    private ReviewListViewAdapter adapter = new ReviewListViewAdapter();

    private Bitmap sbitmap=null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_review);

        listview = (ListView) findViewById(R.id.review_list_view_owner);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        intent = getIntent();
        final User owner = (User) intent.getSerializableExtra("userInfo");
        final Order order = (Order) intent.getSerializableExtra("orderInfo");

        TextView bistroName = (TextView) findViewById(R.id.bistro_name_txtView);
        bistroName.setText(order.getStore().getName());

        Call<List<Review>> callgetReviewList = service.getOrderReviewList("Bearer " + token, order.getId());
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
            de.hdodenhof.circleimageview.CircleImageView btnMyPage = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.mypagebtn);
            btnMyPage.setImageBitmap(bitmap);
        }

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageCheckReview.this, ShowOwnerBistroList.class);
                MyPageCheckReview.this.finish();
                startActivity(intent);
            }
        });

        de.hdodenhof.circleimageview.CircleImageView btnMyPage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageCheckReview.this, MyPageOwner.class);
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