package com.example.yaneodoo;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.REST.RestGetAuth;
import com.example.yaneodoo.REST.RestGetUserInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {
    private String userInfo;
    private String loginInfo;
    private int rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        checkSelfPermission();

        final SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        final String NOTIFICATION_ID = "10001";
        String NOTIFICATION_NAME = "리뷰남기기";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
        final Intent intent = new Intent(Login.this.getApplicationContext(), LoginConfirmed.class);

        //채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_NAME, IMPORTANCE);
            notificationManager.createNotificationChannel(channel);
        }

        // 회원가입 버튼 클릭 리스너
        Button btnCustomer = (Button) findViewById(R.id.login_signup_button);
        btnCustomer.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼 클릭 리스너
        Button btnOwner = (Button) findViewById(R.id.login_button);
        final EditText id = (EditText)findViewById(R.id.login_id_textinput);
        final EditText password = (EditText)findViewById(R.id.login_password_textinput);
        btnOwner.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    RestGetAuth restGetAuth = new RestGetAuth(id.getText().toString(), password.getText().toString(), tk);
                    try {
                        rc = restGetAuth.execute().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("Login rc", String.valueOf(rc));
                    if(rc == 200){
                        Intent intent = new Intent(Login.this, LoginConfirmed.class);
                        startActivity(intent);
                    }
                    else if(rc == 404){
                        Toast noIdToast = Toast.makeText(getApplicationContext(), "계정이 존재하지 않습니다.", Toast.LENGTH_LONG);
                        noIdToast.show();
                    } else if (rc == 401) {
                        Toast diffPwToast = Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_LONG);
                        diffPwToast.show();
                    } else {
                        Log.e("POST", "Failed.");
                    }
                }
                catch (Exception e) {
                    // Error calling the rest api
                    Log.e("REST_API", "POST method failed: " + e.getMessage());
                    e.printStackTrace();
                }

            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    Log.d("Login", "권한 허용 : " + permissions[i]);
            }
        }
    }
}
