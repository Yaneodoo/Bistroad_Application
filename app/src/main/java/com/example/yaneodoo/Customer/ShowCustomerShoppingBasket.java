package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.ShoppingBasketListViewAdapter;
import com.example.yaneodoo.R;
import com.example.yaneodoo.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowCustomerShoppingBasket extends AppCompatActivity {
    int menuQuantity;
    ArrayList<Menu> selectedMenu = new ArrayList<>();
    private Intent intent;
    private User user;
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";
    private Store store = new Store();

    private ShoppingBasketListViewAdapter adapter = new ShoppingBasketListViewAdapter();
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingbasket_customer);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        intent = getIntent();
        user = (User) intent.getSerializableExtra("userInfo");

        String storeId = "";
        for (Menu menu : ReadShoppingBasketData()) {
            selectedMenu.add(menu);
            adapter.addItem(menu.getName(), menu.getPrice());
            storeId = menu.getStoreId();
        }

        //TODO : 장바구니가 비었을시에는 장바구니 활성화되지 않아 아래 구문들이 실행되지 않을 것임
        Log.d("STOREID", storeId);
        final Store store = getStore(token, storeId);

        listview = (ListView) findViewById(R.id.shoppingbasket_list_view_customer);

        // 리스트뷰 참조 및 Adapter달기
        listview.setAdapter(adapter);

        /*
        TextView bistroNameTxtView = (TextView) findViewById(R.id.bistro_name_txtView);
        bistroNameTxtView.setText(store.getName());
        TextView bistroLocationTxtView = (TextView) findViewById(R.id.bistro_location_txtView);
        //bistroLocationTxtView.setText("lat : "+store.getLocation().getLat().toString()+"lng : "+store.getLocation().getLng().toString());
        TextView bistroDescTxtView = (TextView) findViewById(R.id.bistro_desc_txtView);
        bistroDescTxtView.setText(store.getDescription());
        */

        // 최종 주문 버튼 클릭 리스너
        Button shoppingbasketOrderBtn = findViewById(R.id.shoppingbasket_order_btn);
        shoppingbasketOrderBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : 팝업으로 띄울건지 등의 논의 후 구현
                //PopupMenu pop = new PopupMenu(getApplicationContext(), view);
                Toast.makeText(getApplicationContext(), "주문이 완료되었습니다!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ShowCustomerShoppingBasket.this, ShowCustomerBistroList.class);
                ShowCustomerShoppingBasket.this.finish();
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerShoppingBasket.this, ShowCustomerBistroList.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ShowCustomerShoppingBasket.this.finish();
                startActivity(intent);
            }
        });

        // TODO : mypagebtn 클릭 리스너
    }

    // 더담으러가기 텍스트뷰 클릭 리스너
    public void backToMenuList(View v) {
        Intent intent = new Intent(ShowCustomerShoppingBasket.this, ShowCustomerMenuList.class);
        intent.putExtra("userInfo", user);
        intent.putExtra("bistroInfo", store);
        //TODO : 장바구니에 있는 메뉴의 bistro로 이동
        ShowCustomerShoppingBasket.this.finish();
        startActivity(intent);
    }

    // 아이템 삭제 버튼 클릭 리스너
    public void deleteItem(View v) {
        int position = listview.getPositionForView(v);

        selectedMenu.remove(position);
        SaveShoppingBasketData(selectedMenu);
        adapter.deleteItem(position);
        adapter.notifyDataSetChanged();
    }

    public void Decrement(View view) {
        TableRow parentRow = (TableRow) view.getParent();

        TextView menuQuantityTxtView = (TextView) parentRow.findViewById(R.id.menu_quantity_txtView);
        String quantityString = menuQuantityTxtView.getText().toString();
        menuQuantity = Integer.parseInt(quantityString);
        menuQuantity -= 1;

        if (menuQuantity < 1) menuQuantity = 1;
        menuQuantityTxtView.setText(String.valueOf(menuQuantity));
    }

    public void Increment(View view) {
        TableRow parentRow = (TableRow) view.getParent();

        TextView menuQuantityTxtView = (TextView) parentRow.findViewById(R.id.menu_quantity);
        String quantityString = menuQuantityTxtView.getText().toString();
        menuQuantity = Integer.parseInt(quantityString);
        menuQuantity += 1;
        menuQuantityTxtView.setText(String.valueOf(menuQuantity));
    }

    private Store getStore(String token, String storeId) {
        final Store store = new Store();
        service.getStore("Bearer " + token, storeId).enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                if (response.isSuccessful()) {
                    Store body = response.body();
                    if (body != null) {
                        store.setName(body.getName());
                        store.setLocation(body.getLocation());
                        store.setDescription(body.getDescription());
                        store.setId(body.getId());
                        store.setOwnerId(body.getOwnerId());
                        store.setPhone(body.getPhone());
                        //store.setPhotoUri(body.get(i).getPhotoUri());
                    }
                }
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return store;
    }

    private void SaveShoppingBasketData(ArrayList<Menu> selectedMenu) {
        SharedPreferences.Editor editor = getSharedPreferences("sFile", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(selectedMenu);
        editor.putString("SelectedMenu", json);
        editor.commit();
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
