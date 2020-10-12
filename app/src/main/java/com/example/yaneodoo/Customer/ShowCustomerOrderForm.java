package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetUserImage;
import com.example.yaneodoo.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
    private Store store=new Store();

    private User user;

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
        user = (User) intent.getSerializableExtra("userInfo");
        store = (Store) intent.getSerializableExtra("bistroInfo");
        final Menu menu = (Menu) intent.getSerializableExtra("menuInfo");
        final String menuQuantity = (String) intent.getStringExtra("menuQuantity");

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

        selectedMenu = ReadShoppingBasketData();

        TextView menuNameTxtView = (TextView) findViewById(R.id.menu_name_txtView);
        menuNameTxtView.setText(menu.getName());
        TextView menuDescTxtView = (TextView) findViewById(R.id.menu_desc_txtView);
        menuDescTxtView.setText(menu.getDescription());
        TextView menuStarsTxtView = (TextView) findViewById(R.id.menu_stars_txtView);
        menuStarsTxtView.setText(menu.getStars());

        TextView menuPriceTxtView = (TextView) findViewById(R.id.menu_price_txtView);
        menuPriceTxtView.setText(menu.getPrice());
        TextView menuQuantityTxtView = (TextView) findViewById(R.id.menu_quantity);
        if(menuQuantity!=null) menuQuantityTxtView.setText(menuQuantity);
        else menuQuantityTxtView.setText("1");

        Button btnPickupBtn = (Button) findViewById(R.id.btn_pick_up);
        btnPickupBtn.setText(menuQuantityTxtView.getText().toString() + "개 담기");

        Button pickupbtn = (Button) findViewById(R.id.btn_pick_up);
        pickupbtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView menuQuantityTxtView = (TextView) findViewById(R.id.menu_quantity);
                menu.setQuantity(Integer.parseInt(menuQuantityTxtView.getText().toString()));

                boolean exist=false;
                for(int i=0;i<selectedMenu.size();i++){
                    if(selectedMenu.get(i).getId().equals(menu.getId())){ //이미 담아있는 메뉴
                        selectedMenu.set(i,menu);
                        exist=true;
                    }
                }
                if(!exist) selectedMenu.add(menu); //새로 담는 메뉴

                SaveShoppingBasketData(selectedMenu);

                Intent intent = new Intent(ShowCustomerOrderForm.this, ShowCustomerMenuList.class);
                intent.putExtra("userInfo", user);
                intent.putExtra("bistroInfo", store);
                ShowCustomerOrderForm.this.finish();
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        ImageButton btnMyPage = (ImageButton) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerOrderForm.this, MyPageCustomer.class);
                intent.putExtra("userInfo", user);
                ShowCustomerOrderForm.this.finish();
                startActivity(intent);
            }
        });
    }

    public void Decrement(View view) {
        TableRow parentRow = (TableRow) view.getParent();

        TextView menuQuantityTxtView = (TextView) parentRow.findViewById(R.id.menu_quantity);
        String quantityString = menuQuantityTxtView.getText().toString();
        menuQuantity = Integer.parseInt(quantityString);
        menuQuantity -= 1;

        if (menuQuantity < 1) menuQuantity = 1;
        menuQuantityTxtView.setText(String.valueOf(menuQuantity));

        Button btnPickupBtn = (Button) findViewById(R.id.btn_pick_up);
        btnPickupBtn.setText(String.valueOf(menuQuantity) + "개 담기");
    }

    public void Increment(View view) {
        TableRow parentRow = (TableRow) view.getParent();

        TextView menuQuantityTxtView = (TextView) parentRow.findViewById(R.id.menu_quantity);
        String quantityString = menuQuantityTxtView.getText().toString();
        menuQuantity = Integer.parseInt(quantityString);
        menuQuantity += 1;
        menuQuantityTxtView.setText(String.valueOf(menuQuantity));

        Button btnPickupBtn = (Button) findViewById(R.id.btn_pick_up);
        btnPickupBtn.setText(String.valueOf(menuQuantity) + "개 담기");
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

    private void SaveShoppingBasketData(ArrayList<Menu> selectedMenu) {
        SharedPreferences.Editor editor = getSharedPreferences("sFile", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(selectedMenu);
        editor.putString("SelectedMenu", json);
        editor.commit();
    }
}
