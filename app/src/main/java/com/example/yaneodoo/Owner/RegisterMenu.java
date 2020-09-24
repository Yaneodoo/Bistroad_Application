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
import java.util.concurrent.ExecutionException;

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

        TextView bistroNameTxtView = (TextView) findViewById(R.id.bistro_name_txtView);
        bistroNameTxtView.setText(store.getName());
        TextView bistroLocationTxtView = (TextView) findViewById(R.id.bistro_location_txtView);
        bistroLocationTxtView.setText("매장 위치 [lat: " + store.getLocation().getLat() + " lng: " + store.getLocation().getLng() + "]");

        if (menu != null) {
            EditText menuNameTxtView = (EditText) findViewById(R.id.menu_name_txtView);
            menuNameTxtView.setText(menu.getName());
            EditText menuPriceTxtView = (EditText) findViewById(R.id.menu_price_txtView);
            menuPriceTxtView.setText(menu.getPrice().substring(0,menu.getPrice().length()-1));
            EditText meuDescTxtView = (EditText) findViewById(R.id.menu_desc_txtView);
            meuDescTxtView.setText(menu.getDescription());
        }

        // 등록 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_complete);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameEditTxt = (EditText) findViewById(R.id.menu_name_txtView);
                EditText priceEditTxt = (EditText) findViewById(R.id.menu_price_txtView);
                EditText descEditTxt = (EditText) findViewById(R.id.menu_desc_txtView);

                if(nameEditTxt.getText().toString().equals("") || priceEditTxt.getText().toString().equals("") || descEditTxt.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "항목을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
                else {
                    //Drawable uploadedImg = imgBtn.getDrawable();
                    String name = nameEditTxt.getText().toString();
                    String price = priceEditTxt.getText().toString();
                    String desc = descEditTxt.getText().toString();

                    Menu nMenu=new Menu();
                    nMenu.setDescription(desc);
                    nMenu.setName(name);
                    nMenu.setPrice(price);

                    if (menu == null) { //새로운 메뉴 등록
                        Call<Menu> callpostMenu = service.postMenu("Bearer " + token, nMenu, store.getId());
                        try {
                            new postMenu().execute(callpostMenu).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{ //기존 메뉴 수정
                        Call<Menu> callpatchMenu = service.patchMenu("Bearer " + token, nMenu, store.getId(), menu.getId());
                        try {
                            new patchMenu().execute(callpatchMenu).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                Intent intent = new Intent(RegisterMenu.this, ShowOwnerMenuList.class);
                intent.putExtra("ownerInfo", owner);
                intent.putExtra("bistroInfo", store);
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

    private class postMenu extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Menu> call = params[0];
                Response<Menu> response = call.execute();
                Menu body = response.body();
                Log.d("MENU", body.toString());

                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

    private class patchMenu extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Menu> call = params[0];
                Response<Menu> response = call.execute();
                Menu body = response.body();
                Log.d("MENU", body.toString());

                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
}
