package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.MenuListViewCustomerAdapter;
import com.example.yaneodoo.ListView.MenuListViewItem;
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

public class ShowCustomerMenuList extends AppCompatActivity {
    private Intent intent;
    final MenuListViewCustomerAdapter adapter = new MenuListViewCustomerAdapter();
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";
    private ListView listview;

    private User user;

    private List<Menu> menuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list_customer);

        listview = (ListView) findViewById(R.id.menu_list_view_customer);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        intent = getIntent();
        user = (User) intent.getSerializableExtra("userInfo");
        final Store store = (Store) intent.getSerializableExtra("bistroInfo");

        Log.d("bistroInfo2", store.getName());

        TextView bistroNameTxtView = (TextView) findViewById(R.id.bistro_name_txtView);
        bistroNameTxtView.setText(store.getName());
        TextView bistroLocationTxtView = (TextView) findViewById(R.id.bistro_location_txtView);
        bistroLocationTxtView.setText(store.getLocation().toString());
        TextView bistroDescTxtView = (TextView) findViewById(R.id.bistro_desc_txtView);
        bistroDescTxtView.setText(store.getDescription());

        getMenuList(token, store.getId());//가게의 메뉴 불러오기
        //TODO : 별점 높은 순

        //메뉴 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                MenuListViewItem item = (MenuListViewItem) parent.getItemAtPosition(position);
                Menu menu = new Menu();
                menu.setStoreId(menuList.get(position).getStoreId());
                menu.setId(menuList.get(position).getId());
                menu.setName(menuList.get(position).getName());
                menu.setDescription(menuList.get(position).getDescription());
                menu.setPrice(menuList.get(position).getPrice());
                menu.setStars(menuList.get(position).getStars());
                //menu.setPhotoUri(menuList.get(position).getPhotoUri());
                //menu.set..(menuList.get(position).getOrderedCnt());

                Log.d("menu", menu.toString());

                Intent intent = new Intent(ShowCustomerMenuList.this, ShowCustomerMenuInfo.class);
                intent.putExtra("menuInfo", menu);
                intent.putExtra("userInfo", user);

                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerMenuList.this, ShowCustomerBistroList.class);
                ShowCustomerMenuList.this.finish();
                startActivity(intent);
            }
        });

        ImageButton btnMyPage = (ImageButton) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerMenuList.this, MyPageCustomer.class);
                startActivity(intent);
            }
        });

        // FAB 클릭 리스너
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowCustomerMenuList.this, ShowCustomerShoppingBasket.class);
                intent.putExtra("userInfo", user);
                startActivity(intent);
            }
        });
    }

    // 주문하기 버튼 클릭 리스너
    public void orderMenu(View v) {
        LinearLayout parentRow = (LinearLayout) v.getParent().getParent().getParent();
        Integer position = Integer.parseInt((String) parentRow.getTag());

        Intent intent = new Intent(ShowCustomerMenuList.this, ShowCustomerOrderForm.class);
        intent.putExtra("userInfo", user);
        intent.putExtra("menuInfo", menuList.get(position));

        ShowCustomerMenuList.this.finish();
        startActivity(intent);
    }

    private void getMenuList(String token, String storeId) {
        service.getMenuList("Bearer " + token, storeId).enqueue(new Callback<List<Menu>>() {
            @Override
            public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {
                if (response.isSuccessful()) {
                    List<Menu> body = response.body();
                    if (body != null) {
                        for (int i = 0; i < body.size(); i++) {
                            Menu menu = new Menu();
                            menu.setId(body.get(i).getId());
                            menu.setName(body.get(i).getName());
                            menu.setPrice(body.get(i).getPrice().substring(0, body.get(i).getPrice().length() - 2) + "원");
                            menu.setDescription(body.get(i).getDescription());
                            menu.setStars("★" + body.get(i).getStars());
                            //menu.setPhotoUri(body.get(i).getPhotoUri());
                            menu.setStoreId(body.get(i).getStoreId());
                            menuList.add(menu);

                            adapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sundae), menu.getName(), menu.getPrice(), menu.getDescription(), menu.getStars(), " ");
                            Log.d("menu data", "--------------------------------------");
                        }
                        Log.d("getMenuList end", "======================================");
                        listview.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {
                t.printStackTrace();
                Log.d("fail", "======================================");
            }
        });
    }
}