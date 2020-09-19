package com.example.yaneodoo.Customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
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

public class ShowCustomerOrderForm extends AppCompatActivity {
    int menuQuantity;
    private Intent intent;
    ArrayList<Menu> selectedMenu = new ArrayList<>();
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_form_customer);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        intent = getIntent();
        final User user = (User) intent.getSerializableExtra("userInfo");
        final Menu menu = (Menu) intent.getSerializableExtra("menuInfo");

        TextView menuNameTxtView = (TextView) findViewById(R.id.menu_name_txtView);
        menuNameTxtView.setText(menu.getName());
        TextView menuDescTxtView = (TextView) findViewById(R.id.menu_desc_txtView);
        menuDescTxtView.setText(menu.getDescription());
        TextView menuStarsTxtView = (TextView) findViewById(R.id.menu_stars_txtView);
        menuStarsTxtView.setText(menu.getStars());

        TextView menuPriceTxtView = (TextView) findViewById(R.id.menu_price_txtView);
        menuPriceTxtView.setText(menu.getPrice());

        final Store store = getStore(token, menu.getStoreId());

        final Context context = this;

        Button pickupbtn = (Button) findViewById(R.id.btn_pick_up);
        pickupbtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Menu menu : ReadShoppingBasketData()) {
                    selectedMenu.add(menu);
                }
                TextView menuQuantityTxtView = (TextView) findViewById(R.id.menu_quantity);
                menu.setQuantity(Integer.parseInt(menuQuantityTxtView.getText().toString()));
                selectedMenu.add(menu);

                SaveShoppingBasketData(selectedMenu);

                Intent intent = new Intent(ShowCustomerOrderForm.this, ShowCustomerMenuList.class);
                intent.putExtra("userInfo", user);
                intent.putExtra("bistroInfo", store);
                ShowCustomerOrderForm.this.finish();
                startActivity(intent);
            }
        });
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

    public void Decrement(View view) {
        TableRow parentRow = (TableRow) view.getParent();

        TextView menuQuantityTxtView = (TextView) parentRow.findViewById(R.id.menu_quantity);
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
                        Log.d("bistroInfo1", store.toString());
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
}
