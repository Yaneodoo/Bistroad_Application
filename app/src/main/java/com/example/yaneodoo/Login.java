package com.example.yaneodoo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    // FOR ACTIVITY SWITCH. ACCORDING TO USER.
    private Intent intent;
    private String userInfo;
    private String loginInfo;
    private int rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        final String NOTIFICATION_ID = "10001";
        String NOTIFICATION_NAME = "리뷰남기기";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
        final Intent intent = new Intent(Login.this.getApplicationContext(),LoginConfirmed.class);

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
}
