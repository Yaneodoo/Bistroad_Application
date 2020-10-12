package com.example.yaneodoo.Owner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.MenuListViewOwnerAdapter;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetUserImage;
import com.example.yaneodoo.RetrofitService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowOwnerMenuList extends AppCompatActivity {
    boolean onChoice = false;
    private Intent intent;

    private String token;
    private Store store;

    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private MenuListViewOwnerAdapter adapter = new MenuListViewOwnerAdapter();
    private ListView listview;

    private List<Menu> menuList = new ArrayList<>();

    private SparseBooleanArray checkedItems;

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
        store = (Store) intent.getSerializableExtra("bistroInfo");
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

        GetUserImage getUserImage = new GetUserImage();
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

        // 주문내역 버튼 클릭 리스너
        Button btn_orderlist = (Button) findViewById(R.id.btn_orderlist);
        btn_orderlist.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, ShowOwnerOrderList.class);
                intent.putExtra("bistroInfo", store);
                intent.putExtra("ownerInfo", owner);
                startActivity(intent);
            }
        });

        //메뉴 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (onChoice) {
                    v.setBackgroundColor(R.id.dark);
                } else {
                    // get item
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
                    Intent intent = new Intent(ShowOwnerMenuList.this, ShowOwnerMenuInfo.class);
                    intent.putExtra("bistroInfo", store);
                    intent.putExtra("menuInfo", menu);
                    intent.putExtra("ownerInfo", owner);
                    startActivity(intent);
                }
            }
        });

        // 수정 버튼 클릭 리스너
        Button editbtn = (Button) findViewById(R.id.btn_edit);
        editbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, RegisterBistro.class);
                intent.putExtra("bistroInfo", store);
                intent.putExtra("ownerInfo", owner);
                startActivity(intent);
            }
        });

        // 추가 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_add);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, RegisterMenu.class);
                intent.putExtra("bistroInfo", store);
                intent.putExtra("ownerInfo", owner);
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
                    onChoice = true;
                    delbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    delbtn.setTextSize(14);
                    delbtn.setText("확인");
                    Button addbtn = (Button) findViewById(R.id.btn_add);
                    addbtn.setTextSize(14);
                    addbtn.setText("");
                } else {
/*                    delbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                    delbtn.setTextSize(14);
                    delbtn.setText("삭제");
                    Button addbtn = (Button) findViewById(R.id.btn_add);
                    addbtn.setTextSize(14);
                    addbtn.setText("추가");*/
                    checkedItems = listview.getCheckedItemPositions();   //item별 checked 상태 0 or 1
                    if (checkedItems.size() > 0) showAlertDialog(); //삭제 확인 AlertDialog
                    else {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
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
                intent.putExtra("ownerInfo", owner);
                startActivity(intent);
            }
        });
    }

    void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제 확인");
        builder.setMessage("선택한 항목(들)을 삭제합니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //다중 삭제 처리 동작
                        int count = adapter.getCount();
                        for (int i = count - 1; i >= 0; i--) {
                            if (checkedItems.get(i)) { //i position의 상태가 Checked이면
                                Call<Void> calldeleteMenu = service.deleteMenu("Bearer " + token, store.getId(), menuList.get(i).getId());
                                try {
                                    new deleteMenu().execute(calldeleteMenu).get();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        Toast.makeText(getApplicationContext(), "삭제 완료.", Toast.LENGTH_LONG).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "삭제 취소.", Toast.LENGTH_LONG).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
        builder.show();
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

    private class deleteMenu extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Void> call = params[0];
                Response<Void> response = call.execute();
                Void body = response.body();

                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            adapter.notifyDataSetChanged();
        }
    }
}