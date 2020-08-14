package com.example.yaneodoo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Customer.ShowCustomerBistroList;
import com.example.yaneodoo.Owner.ShowOwnerBistroList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectUser extends AppCompatActivity {
    // FOR ACTIVITY SWITCH. ACCORDING TO USER.
    private Intent intent;
    static final int SMS_RECEIVE_PERMISSON = 1;
    private Retrofit mRetrofit;
    private RetrofitService mRetrofitAPI;
    private String baseUrl = "https://api.bistroad.kr/v1";
    private Callback<String> mRetrofitCallback = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            String result = response.body();
            Log.d("TAG", result);
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            t.printStackTrace();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_select);
        checkSelfPermission();
        setRetrofitInit();
        //not complete. so comment out.
        //callBistroList();

        // 손님 버튼 클릭 리스너
        Button btnCustomer = (Button) findViewById(R.id.customerbtn);
        btnCustomer.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectUser.this, ShowCustomerBistroList.class);
                startActivity(intent);
            }
        });

        // 점주 버튼 클릭 리스너
        Button btnOwner = (Button) findViewById(R.id.ownerbtn);
        btnOwner.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectUser.this, ShowOwnerBistroList.class);
                startActivity(intent);
            }
        });
    }

    //retrofit 초기화
    private void setRetrofitInit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .build();
        RetrofitService service = mRetrofit.create(RetrofitService.class);
    }

    private void callBistroList() {
        Call<String> mCallBistroList = mRetrofitAPI.getBistroList();
        mCallBistroList.enqueue(mRetrofitCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    Log.d("SelectUser", "권한 허용 : " + permissions[i]);
            }
        }
    }

    public void checkSelfPermission() {
        String temp = "";

        //카메라 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.CAMERA + " ";
        }

        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //권한 요청
        if (TextUtils.isEmpty(temp) == false)
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
        else
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
    }

}
