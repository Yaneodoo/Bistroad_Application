package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.R;
import com.example.yaneodoo.RetrofitService;

import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterMenu extends AppCompatActivity {
    private Intent intent;
    private String storeId;
    private String token, ownerName;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private static final int REQUEST_CODE = 0;
    private ImageView uploadbtn;

    private Store store = new Store();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_registration_owner);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        // ShowOwnerMenuList에서 보낸 titleStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        final Store store = (Store) intent.getSerializableExtra("bistroInfo");
        final Menu menu = (Menu) intent.getSerializableExtra("menuInfo");
        final User owner = (User) intent.getSerializableExtra("ownerInfo");

        TextView ownerNameTxtView = (TextView) findViewById(R.id.owner_name_textView);
        ownerNameTxtView.setText(owner.getFullName() + " 점주님");

        if (store != null) {
            TextView bistroNameTxtView = (TextView) findViewById(R.id.bistro_name_txtView);
            bistroNameTxtView.setText(store.getName());
            TextView bistroLocationTxtView = (TextView) findViewById(R.id.bistro_location_txtView);
            bistroLocationTxtView.setText("매장 위치 [lat: " + store.getLocation().getLat() + " lng: " + store.getLocation().getLng() + "]");
        } else {
            Call<Store> callgetStore = service.getStore("Bearer " + token, menu.getStoreId());
            new getStore().execute(callgetStore);
        }

        if (menu != null) {
            EditText menuNameTxtView = (EditText) findViewById(R.id.menu_name_txtView);
            menuNameTxtView.setText(menu.getName());
            EditText menuPriceTxtView = (EditText) findViewById(R.id.menu_price_txtView);
            menuPriceTxtView.setText(menu.getPrice());
            EditText meuDescTxtView = (EditText) findViewById(R.id.menu_desc_txtView);
            meuDescTxtView.setText(menu.getDescription());
        }

        // 등록 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_complete);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 이미 있는 품목이면 수정, 없는 품목이면 등록
                // PUT /stores/{storeId}/items/{itemId}
                // POST /stores/{storeId}/items

                Intent intent = new Intent(RegisterMenu.this, ShowOwnerMenuList.class);
                RegisterMenu.this.finish();
                startActivity(intent);
            }
        });

        // 이미지 업로드 버튼 클릭 리스너
        uploadbtn = findViewById(R.id.upload_btn);
        uploadbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterMenu.this, ShowOwnerBistroList.class);
                RegisterMenu.this.finish();
                startActivity(intent);
            }
        });

        ImageButton btnMyPage = (ImageButton) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterMenu.this, MyPageOwner.class);
                RegisterMenu.this.finish();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    uploadbtn.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
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
                //store.setPhotoUri(body.get(i).getPhotoUri());
                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView bistroNameTxtView = (TextView) findViewById(R.id.bistro_name_txtView);
            bistroNameTxtView.setText(store.getName());
            TextView bistroLocationTxtView = (TextView) findViewById(R.id.bistro_location_txtView);
            bistroLocationTxtView.setText("매장 위치 [lat: " + store.getLocation().getLat() + " lng: " + store.getLocation().getLng() + "]");
        }
    }
}
