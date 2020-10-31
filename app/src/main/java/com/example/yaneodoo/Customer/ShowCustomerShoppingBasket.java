package com.example.yaneodoo.Customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.Request;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.ShoppingBasketListViewAdapter;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetImage;
import com.example.yaneodoo.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowCustomerShoppingBasket extends AppCompatActivity {
    private Integer curTableNum;
    final Context context = this;
    int menuQuantity;
    ArrayList<Menu> selectedMenu = new ArrayList<>();
    private Intent intent;
    private User user;
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";
    private Store store = new Store();
    private Integer totalAmount = 0;
    private String storeId = "";

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

        for (Menu menu : ReadShoppingBasketData()) {
            selectedMenu.add(menu);
            adapter.addItem(menu.getName(), menu.getPrice().toString(), menu.getQuantity());
            storeId = menu.getStoreId();
        }

        updateTotalAmount();

        Call<Store> callgetStore = service.getStore("Bearer " + token, storeId);
        new getStore().execute(callgetStore);

        listview = (ListView) findViewById(R.id.shoppingbasket_list_view_customer);

        // 리스트뷰 참조 및 Adapter달기
        listview.setAdapter(adapter);

        // 최종 주문 버튼 클릭 리스너
        Button shoppingbasketOrderBtn = findViewById(R.id.shoppingbasket_order_btn);
        shoppingbasketOrderBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.custom_popup_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("제출",
                                new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    public void onClick(DialogInterface dialog, int id) {
                                        curTableNum=Integer.parseInt(userInput.getText().toString());

                                        SaveShoppingBasketData(new ArrayList<Menu>()); //장바구니 비우기

                                        List<Request> requestList=new ArrayList<>();

                                        for (Menu menu : selectedMenu) {
                                            requestList.add(new Request(menu, menu.getQuantity(), menu.getPrice()));
                                            Log.d("SELECTED MENU",menu.getId()+","+menu.getQuantity());
                                        }
                                        Log.d("SELECTED MENU LIST","-------------------------");

                                        LocalDateTime date = LocalDateTime.now();
                                        Log.d("DATE",date.toString());

                                        Order order=new Order( requestList,"REQUESTED", store.getId(), curTableNum, date.toString()+"+09:00", user.getId());
//                                        order.setStoreId(store.getId());
                                        Log.d("CREATE ORDER",order.toString());

                                        Call<Order> callpostOrder = service.postOrder("Bearer " + token, order);
                                        try {
                                            new postOrder().execute(callpostOrder).get();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(getApplicationContext(), "주문이 완료되었습니다!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ShowCustomerShoppingBasket.this, ShowCustomerBistroList.class);

                                        ShowCustomerShoppingBasket.this.finish();
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerShoppingBasket.this, ShowCustomerBistroList.class);
                ShowCustomerShoppingBasket.this.finish();
                startActivity(intent);
            }
        });

        de.hdodenhof.circleimageview.CircleImageView btnMyPage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerShoppingBasket.this, MyPageCustomer.class);
                intent.putExtra("userInfo", user);
                ShowCustomerShoppingBasket.this.finish();
                startActivity(intent);
            }
        });
    }

    // 더담으러가기 텍스트뷰 클릭 리스너
    public void backToMenuList(View v) {
        SaveShoppingBasketData(selectedMenu);
        Intent intent = new Intent(ShowCustomerShoppingBasket.this, ShowCustomerMenuList.class);
        intent.putExtra("userInfo", user);
        intent.putExtra("bistroInfo", store);
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

        updateTotalAmount();
    }

    public void Decrement(View view) {
        TableRow parentRow = (TableRow) view.getParent();

        TextView menuQuantityTxtView = (TextView) parentRow.findViewById(R.id.menu_quantity);
        String quantityString = menuQuantityTxtView.getText().toString();
        menuQuantity = Integer.parseInt(quantityString);
        menuQuantity -= 1;

        if (menuQuantity < 1) menuQuantity = 1;
        menuQuantityTxtView.setText(String.valueOf(menuQuantity));

        int position = listview.getPositionForView(view);
        Menu curMenu = selectedMenu.get(position);
        curMenu.setQuantity(menuQuantity);
        selectedMenu.set(position,curMenu); //들어가는 값 변경

        updateTotalAmount();
    }

    public void Increment(View view) {
        TableRow parentRow = (TableRow) view.getParent();

        TextView menuQuantityTxtView = (TextView) parentRow.findViewById(R.id.menu_quantity);
        String quantityString = menuQuantityTxtView.getText().toString();
        menuQuantity = Integer.parseInt(quantityString);
        menuQuantity += 1;
        menuQuantityTxtView.setText(String.valueOf(menuQuantity));

        int position = listview.getPositionForView(view);
        Menu curMenu = selectedMenu.get(position);
        curMenu.setQuantity(menuQuantity);
        selectedMenu.set(position,curMenu); //들어가는 값 변경

        updateTotalAmount();
    }

    private void updateTotalAmount() {
        if(selectedMenu.size()==0){
            Toast.makeText(getApplicationContext(), "장바구니가 텅 비었습니다!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ShowCustomerShoppingBasket.this, ShowCustomerBistroList.class);
            ShowCustomerShoppingBasket.this.finish();
            startActivity(intent);
        }
        else{
            for (Menu menu : selectedMenu) {
                Log.d("SELECTED MENU",menu.getId()+","+menu.getQuantity());
            }
            Log.d("SELECTED MENU LIST","-------------------------");

            totalAmount=0;
            for (Menu menu : selectedMenu) {
                totalAmount += Integer.valueOf(menu.getPrice().toString()) * menu.getQuantity();
            }
            TextView shoppingTotalAmountTxtView = (TextView) findViewById(R.id.shoppingbasket_total_amount_txtView);
            shoppingTotalAmountTxtView.setText(totalAmount + "원");
        }
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

    private class postOrder extends AsyncTask<Call, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Order> call = params[0];
                Response<Order> response = call.execute();
                Order body = response.body();
                Log.d("POSTORDER", "POSTORDER");
                Log.d("POSTORDER", body.toString());

                if (body != null) {
                    int statusCode  = response.code();
                    Log.d("POSTORDER CODE",Integer.toString(statusCode));

                }
                else {
                    int statusCode  = response.code();
                    Log.d("POSTORDER CODE",Integer.toString(statusCode));
                }

                return null;
            } catch (IOException e) {
                Log.d("POSTORDER", "FAIL");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

    private class getStore extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Store> call = params[0];
                Response<Store> response = call.execute();
                Store body = response.body();
                Log.d("STORE", body.toString());

                store.setName(body.getName());
                store.setLocation(body.getLocation());
                store.setDescription(body.getDescription());
                store.setId(body.getId());
                store.setOwnerId(body.getOwnerId());
                store.setPhone(body.getPhone());
                store.setAddress(body.getAddress());
                store.setPhoto(body.getPhoto());
                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            GetImage getStoreImage = new GetImage();
            try {
                if(store.getPhoto()!=null) {
                    Bitmap bitmap = getStoreImage.execute(store.getPhoto().getThumbnailUrl()).get();
                    ImageView bistroRepresentImgView = (ImageView) findViewById(R.id.bistro_represent_image);
                    bistroRepresentImgView.setImageBitmap(bitmap);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            TextView bistroNameTxtView = (TextView) findViewById(R.id.bistro_name_txtView);
            bistroNameTxtView.setText(store.getName());
            TextView bistroLocationTxtView = (TextView) findViewById(R.id.bistro_location_txtView);
            bistroLocationTxtView.setText(store.getAddress());
            TextView bistroDescTxtView = (TextView) findViewById(R.id.bistro_desc_txtView);
            bistroDescTxtView.setText(store.getDescription());
        }
    }
}
