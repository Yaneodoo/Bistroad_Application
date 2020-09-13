package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.BackPressedForFinish;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.ListView.BistroListViewItem;
import com.example.yaneodoo.R;
import com.example.yaneodoo.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowCustomerBistroList extends AppCompatActivity {
    private String ownerId, ownerName;
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private BistroListViewAdapter adapter = new BistroListViewAdapter();
    private ListView listview;

    private List<Store> storeList = new ArrayList<>();

    private BackPressedForFinish backPressedForFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_list_customer);
        backPressedForFinish = new BackPressedForFinish(this);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        final User user = getUserMe(token);
        getNearbyStoreList(token);//소유한 가게 불러오기

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.bistro_list_view_customer);

        //가게 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                BistroListViewItem item = (BistroListViewItem) parent.getItemAtPosition(position);
                Store store = new Store();
                store.setId(storeList.get(position).getId());
                store.setName(storeList.get(position).getName());
                store.setLocation(storeList.get(position).getLocation());
                store.setDescription(storeList.get(position).getDescription());
                //store.setPhotoUri(storeList.get(position).getPhotoUri());

                Intent intent = new Intent(ShowCustomerBistroList.this, ShowCustomerMenuList.class);
                intent.putExtra("userInfo", user);
                intent.putExtra("bistroInfo", store);
                //ShowCustomerBistroList.this.finish();
                startActivity(intent);
            }
        }) ;

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerBistroList.this, ShowCustomerBistroList.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ShowCustomerBistroList.this.finish();
                startActivity(intent);
            }
        });

        // TODO : mypagebtn 클릭 리스너

        // FAB 클릭 리스너
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowCustomerBistroList.this, ShowCustomerShoppingBasket.class);
                ShowCustomerBistroList.this.finish();
                startActivity(intent);
            }
        });
    }

    private User getUserMe(String token) {
        final User user = new User();
        service.getUserMe("Bearer " + token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User body = response.body();
                    if (body != null) {
                        user.setId(body.getId());
                        user.setUsername(body.getUsername());
                        user.setRole(body.getRole());
                        user.setPhone(body.getPhone());
                        user.setFullName(body.getFullName());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                Log.d("t", "fail");
            }
        });

        return user;
    }

    private void getNearbyStoreList(String token) {
        service.getNearbyStoreList("Bearer " + token).enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (response.isSuccessful()) {
                    List<Store> body = response.body();
                    if (body != null) {
                        for (int i = 0; i < body.size(); i++) {
                            Store store = new Store();
                            store.setName(body.get(i).getName());
                            store.setLocation(body.get(i).getLocation());
                            store.setDescription(body.get(i).getDescription());
                            store.setId(body.get(i).getId());
                            store.setOwnerId(body.get(i).getOwnerId());
                            store.setPhone(body.get(i).getPhone());
                            //store.setPhotoUri(body.get(i).getPhotoUri());
                            storeList.add(store);

                            adapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tteokbokki), store.getName(), "lat: " + store.getLocation().getLat() + "lng: " + store.getLocation().getLng(), store.getDescription());

                            Log.d("store data", "--------------------------------------");
                        }
                        Log.d("getStoreList end", "======================================");
                        listview.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressedForFinish.onBackPressed(this);
    }
}

