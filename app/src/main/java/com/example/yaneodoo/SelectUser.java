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
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.Owner.ShowOwnerBistroList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectUser extends AppCompatActivity {
    // FOR ACTIVITY SWITCH. ACCORDING TO USER.
    static final int SMS_RECEIVE_PERMISSON = 1;
    private Retrofit mRetrofit;
    private RetrofitService mRetrofitAPI;
    private String baseUrl = "https://api.bistroad.kr/v1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_select);
        checkSelfPermission();
        setRetrofitInit();

        //Check Log
        getUserList();
        getUser("admin");
        postUser(new User("jinha", "awefvdnrhges", "관리자", "010-4916-6570", "ROLE_ADMIN"));

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

    private void getUser(String uid) {
        mRetrofitAPI.getUser(uid).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User body = response.body();
                    if (body != null) {
                        Log.d("data.getid()", body.getId());
                        Log.d("data.getusername()", body.getUsername());
                        Log.d("data.getfullName()", body.getFullName());
                        Log.d("data.getphone()", body.getPhone());
                        Log.d("data.getrole()", body.getRole());
                        Log.d("getUser end", "======================================");
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getUserList() {
        mRetrofitAPI.getUserList().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> body = response.body();
                    if (body != null) {
                        for (int i = 0; i < body.size(); i++) {
                            Log.d("data" + i + "getid()", body.get(i).getId());
                            Log.d("data" + i + "getusername()", body.get(i).getUsername());
                            Log.d("data" + i + "getfullName()", body.get(i).getFullName());
                            Log.d("data" + i + "getphone()", body.get(i).getPhone());
                            Log.d("data" + i + "getrole()", body.get(i).getRole());
                            Log.d("user data", "--------------------------------------");
                        }
                        Log.d("getUserList end", "======================================");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void postUser(User user) {
        mRetrofitAPI.postUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User body = response.body();
                    if (body != null) {
                        Log.d("data.getid()", body.getId());
                        Log.d("data.getusername()", body.getUsername());
                        Log.d("data.getfullName()", body.getFullName());
                        Log.d("data.getphone()", body.getPhone());
                        Log.d("data.getrole()", body.getRole());
                        Log.d("postUser end", "======================================");
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
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

    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;

    @Override
    public void onBackPressed() {
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this, "이용해 주셔서 감사합니다.", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
