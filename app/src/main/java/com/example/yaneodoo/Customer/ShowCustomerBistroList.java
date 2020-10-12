package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.BackPressedForFinish;
import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.ListView.BistroListViewItem;
import com.example.yaneodoo.Owner.ShowOwnerBistroList;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetUserImage;
import com.example.yaneodoo.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    private User user = new User();

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

        Call<User> callgetUserMe = service.getUserMe("Bearer " + token);
        new getUserMe().execute(callgetUserMe);

        Call<List<Store>> getNearbyStoreList = service.getNearbyStoreList("Bearer " + token);
        new getNearbyStoreList().execute(getNearbyStoreList);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.bistro_list_view_customer);
        listview.setAdapter(adapter);

        //가게 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                BistroListViewItem item = (BistroListViewItem) parent.getItemAtPosition(position);
                Store store = new Store();

                store.setOwnerId(storeList.get(position).getOwnerId());
                store.setPhone(storeList.get(position).getPhone());
                store.setId(storeList.get(position).getId());
                store.setName(storeList.get(position).getName());
                store.setLocation(storeList.get(position).getLocation());
                store.setDescription(storeList.get(position).getDescription());
                //store.setPhotoUri(storeList.get(position).getPhotoUri());

                Intent intent = new Intent(ShowCustomerBistroList.this, ShowCustomerMenuList.class);
                intent.putExtra("userInfo", user);
                intent.putExtra("bistroInfo", store);
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
                startActivity(intent);
            }
        });

        ImageButton btnMyPage = (ImageButton)findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerBistroList.this, MyPageCustomer.class);
                intent.putExtra("userInfo", user);
                startActivity(intent);
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
                    Intent intent = new Intent(ShowCustomerBistroList.this, ShowCustomerShoppingBasket.class);
                    intent.putExtra("userInfo", user);
                    startActivity(intent);
                }
            }
        });
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

    private class getUserMe extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<User> call = params[0];
                Response<User> response = call.execute();
                User body = response.body();
                Log.d("USER", body.toString());

                user.setId(body.getId());
                user.setUsername(body.getUsername());
                user.setRole(body.getRole());
                user.setPhone(body.getPhone());
                user.setFullName(body.getFullName());
                user.setPhoto(body.getPhoto());
                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            GetUserImage getUserImage = new GetUserImage();
            try {
                if(user.getPhoto()!=null) {
                    Bitmap bitmap = getUserImage.execute(user.getPhoto().getThumbnailUrl()).get();
                    ImageButton btnMyPage = (ImageButton) findViewById(R.id.mypagebtn);
                    btnMyPage.setImageBitmap(bitmap);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class getNearbyStoreList extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<List<Store>> call = params[0];
                Response<List<Store>> response = call.execute();
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
                        store.setAddress(body.get(i).getAddress());
                        //store.setPhotoUri(body.get(i).getPhotoUri());
                        storeList.add(store);

                        Log.d("STORE", store.toString());
                        adapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tteokbokki),
                                store.getName(), store.getAddress(), store.getDescription());
                        Log.d("store data", "--------------------------------------");
                    }
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

    @Override
    public void onBackPressed() {
        backPressedForFinish.onBackPressed(this);
    }
}

