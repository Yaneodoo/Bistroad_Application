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

import com.example.yaneodoo.BackPressedForFinish;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.ListView.BistroListViewItem;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetUserImage;
import com.example.yaneodoo.RetrofitService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowOwnerBistroList extends AppCompatActivity {
    boolean onChoice = false;

    private BackPressedForFinish backPressedForFinish;

    private User owner = new User();
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private BistroListViewAdapter adapter = new BistroListViewAdapter();
    private ListView listview;

    private List<Store> storeList = new ArrayList<>();

    private SparseBooleanArray checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_list_owner);
        backPressedForFinish = new BackPressedForFinish(this);

        listview = (ListView) findViewById(R.id.bistro_list_view_owner);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");
        String ownerId = getSharedPreferences("sFile", MODE_PRIVATE).getString("id", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        Call<User> callgetUserMe = service.getUserMe("Bearer " + token);
        new getUserMe().execute(callgetUserMe);

        final Call<List<Store>> callgetStoreList = service.getStoreList("Bearer " + token, ownerId);
        new getStoreList().execute(callgetStoreList);


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
                    store.setOwnerId(storeList.get(position).getOwnerId());
                    store.setId(storeList.get(position).getId());
                    store.setName(storeList.get(position).getName());
                    store.setLocation(storeList.get(position).getLocation());
                    store.setDescription(storeList.get(position).getDescription());
                    store.setPhone(storeList.get(position).getPhone());
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
                Intent intent = new Intent(ShowOwnerBistroList.this, ShowOwnerBistroList.class);
                startActivity(intent);
            }
        });

        ImageButton btnMyPage = (ImageButton) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerBistroList.this, MyPageOwner.class);
                intent.putExtra("ownerInfo", owner);
                startActivity(intent);
            }
        });
    }

    private class getUserMe extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<User> call = params[0];
                Response<User> response = call.execute();
                User body = response.body();
                Log.d("USER", body.toString());

                owner.setId(body.getId());
                owner.setUsername(body.getUsername());
                owner.setRole(body.getRole());
                owner.setPhone(body.getPhone());
                owner.setFullName(body.getFullName());
                owner.setPhoto(body.getPhoto());

                TextView textView = (TextView) findViewById(R.id.owner_name_textView);
                textView.setText(owner.getFullName() + " 점주님");
                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
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
        }
    }

    private class getStoreList extends AsyncTask<Call, Void, String> {
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

    private class calldeleteStore extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Void> call = params[0];
                Response<Void> response = call.execute();
                Void body = response.body();

                if (body == null){
                    int statusCode  = response.code();
                    Log.d("CODE",Integer.toString(statusCode));
                }
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

    void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제 확인");
        builder.setMessage("선택한 항목(들)을 삭제합니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    //다중 삭제 처리 동작
                        int count = adapter.getCount();
                        Log.d("DELETE", Integer.toString(count));
                        for (int i = count - 1; i >= 0; i--) {
                            if (checkedItems.get(i)) { //i position의 상태가 Checked이면
                                Call<Void> calldeleteStore = service.deleteStore("Bearer " + token, storeList.get(i).getId());
                                Log.d("DELETE",storeList.get(i).getId());
                                try {
                                    new calldeleteStore().execute(calldeleteStore).get();
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

    @Override
    public void onBackPressed() {
        backPressedForFinish.onBackPressed(this);
    }
}