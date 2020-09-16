package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.MenuListViewItem;
import com.example.yaneodoo.ListView.MenuListViewOwnerAdapter;
import com.example.yaneodoo.R;
import com.example.yaneodoo.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowOwnerMenuList extends AppCompatActivity {
    private Intent intent;

    private String storeId;
    private String token, ownerName;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private MenuListViewOwnerAdapter adapter = new MenuListViewOwnerAdapter();
    private ListView listview;

    private List<Menu> menuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list_owner);

        listview = (ListView) findViewById(R.id.menu_list_view_owner);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        intent = getIntent();
        final Store store = (Store) intent.getSerializableExtra("bistroInfo");
        final User owner = (User) intent.getSerializableExtra("ownerInfo");

        TextView ownerNameTxtView = (TextView) findViewById(R.id.owner_name_textView);
        ownerNameTxtView.setText(owner.getFullName() + " 점주님");

        TextView bistroNameTxtView = (TextView) findViewById(R.id.bistro_name_txtView);
        bistroNameTxtView.setText(store.getName());
        TextView bistroLocationTxtView = (TextView) findViewById(R.id.bistro_location_txtView);
        bistroLocationTxtView.setText(store.getLocation().toString());
        TextView bistroDescTxtView = (TextView) findViewById(R.id.bistro_desc_txtView);
        bistroDescTxtView.setText(store.getDescription());

        getMenuList(token, store.getId());//가게의 메뉴 불러오기

        // 주문내역 버튼 클릭 리스너
        Button btn_orderlist = (Button) findViewById(R.id.btn_orderlist);
        btn_orderlist.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, ShowOwnerOrderList.class);
                intent.putExtra("bistroStr", storeId);
                startActivity(intent);
            }
        });

        //메뉴 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                MenuListViewItem item = (MenuListViewItem) parent.getItemAtPosition(position);
                Menu menu = new Menu();
                menu.setId(menuList.get(position).getId());
                menu.setName(menuList.get(position).getName());
                menu.setDescription(menuList.get(position).getDescription());
                menu.setPrice(menuList.get(position).getPrice());
                menu.setStars(menuList.get(position).getStars());
                //menu.setPhotoUri(menuList.get(position).getPhotoUri());
                //menu.set..(menuList.get(position).getOrderedCnt());

                Log.d("menu", menu.toString());
                Intent intent = new Intent(ShowOwnerMenuList.this, ShowOwnerMenuInfo.class);
                intent.putExtra("menuInfo", menu);
                startActivity(intent);
            }
        });

        // 수정 버튼 클릭 리스너
        Button editbtn = (Button) findViewById(R.id.btn_edit) ;
        editbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, RegisterBistro.class);
                intent.putExtra("bistroStr", storeId);
                startActivity(intent);
            }
        });

        // 추가 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_add);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, RegisterMenu.class);
                startActivity(intent);
            }
        });

        // 삭제 추가 레이아웃 초기화
        final Button delbtn = (Button) findViewById(R.id.btn_delete);
        delbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        delbtn.setTextSize(14);
        delbtn.setText("삭제");
        Button abtn = (Button) findViewById(R.id.btn_add);
        abtn.setTextSize(14);
        abtn.setText("추가");

        delbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delbtn.getText().toString() == "삭제") {
                    delbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    delbtn.setTextSize(14);
                    delbtn.setText("확인");
                    Button addbtn = (Button) findViewById(R.id.btn_add);
                    addbtn.setTextSize(14);
                    addbtn.setText("");
                } else {
                    delbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                    delbtn.setTextSize(14);
                    delbtn.setText("삭제");
                    Button addbtn = (Button) findViewById(R.id.btn_add);
                    addbtn.setTextSize(14);
                    addbtn.setText("추가");
                }
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, ShowOwnerBistroList.class);
                ShowOwnerMenuList.this.finish();
                startActivity(intent);
            }
        });

        ImageButton btnMyPage = (ImageButton) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, MyPageOwner.class);
                startActivity(intent);
            }
        });
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