package com.example.yaneodoo.Owner;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.yaneodoo.BackPressedForFinish;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.Customer.MyPageCustomer;
import com.example.yaneodoo.Customer.ShowCustomerBistroList;
import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.ListView.BistroListViewItem;
import com.example.yaneodoo.R;
import com.example.yaneodoo.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowOwnerBistroList extends AppCompatActivity {
    boolean onChoice = false;
    boolean remove = false;

    private BackPressedForFinish backPressedForFinish;

    private String ownerId, ownerName;
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private BistroListViewAdapter adapter = new BistroListViewAdapter();
    private ListView listview;

    private List<Store> storeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_list_owner);
        backPressedForFinish = new BackPressedForFinish(this);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");
        ownerName = getSharedPreferences("sFile", MODE_PRIVATE).getString("fullName", "");

        TextView ownerNameTxtView = (TextView) findViewById(R.id.owner_name_textView);
        ownerNameTxtView.setText(ownerName);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        final User owner = getUserMe(token);

        getStoreList(token, ownerId);//소유한 가게 불러오기

        // 리스트뷰 참조, 멀티 선택(체크박스) 설정, Adapter달기
        listview = (ListView) findViewById(R.id.bistro_list_view_owner);

        //가게 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (onChoice) {
                    v.setBackgroundColor(R.id.dark);
                } else {
                    // get item
                    BistroListViewItem item = (BistroListViewItem) parent.getItemAtPosition(position);
                    Store store = new Store();
                    store.setId(storeList.get(position).getId());
                    store.setName(storeList.get(position).getName());
                    store.setLocation(storeList.get(position).getLocation());
                    store.setDescription(storeList.get(position).getDescription());
                    //store.setPhotoUri(storeList.get(position).getPhotoUri());

                    Intent intent = new Intent(ShowOwnerBistroList.this, ShowOwnerMenuList.class);
                    intent.putExtra("ownerInfo", owner);
                    intent.putExtra("bistroInfo", store);
                    startActivity(intent);
                }
            }
        });

        // 추가 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_add);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerBistroList.this, RegisterBistro.class);
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
                    SparseBooleanArray checkedItems = listview.getCheckedItemPositions();   //item별 checked 상태 0 or 1

                    Log.d("selected", checkedItems.toString());
                    if (checkedItems.size() > 0) showAlertDialog(); //삭제 확인 AlertDialog
                    else {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }

                    //다중 삭제 처리 동작
                    if (remove) {
                        int count = adapter.getCount();
                        for (int i = count - 1; i >= 0; i--) {
                            if (checkedItems.get(i)) { //i position의 상태가 Checked이면
                                // TODO : DELETE /stores/{storeId}로 해당 매장 삭제
                                BistroListViewItem bistro = (BistroListViewItem) adapter.getItem(i);
                                //bistro.getTitle(); //Id로
                            }
                        }
                    }
                }
            }
        });

        ImageButton btnMyPage = (ImageButton)findViewById(R.id.mypagebtn) ;
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerBistroList.this, MyPageOwner.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerBistroList.this, ShowOwnerBistroList.class);
                startActivity(intent);
            }
        });

        // TODO : mypagebtn 클릭 리스너
    }

    private User getUserMe(String token) {
        final User owner = new User();
        service.getUserMe("Bearer " + token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User body = response.body();
                    if (body != null) {
                        owner.setId(body.getId());
                        owner.setUsername(body.getUsername());
                        owner.setRole(body.getRole());
                        owner.setPhone(body.getPhone());
                        owner.setFullName(body.getFullName());

                        Log.d("b", owner.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                Log.d("t", "fail");
            }
        });

        return owner;
    }

    private void getStoreList(String token, String ownerId) {
        service.getStoreList("Bearer " + token, ownerId).enqueue(new Callback<List<Store>>() {
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

    void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제 확인");
        builder.setMessage("선택한 항목(들)을 삭제합니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        remove = true;
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

    @Override
    public void onBackPressed() {
        backPressedForFinish.onBackPressed(this);
    }
}