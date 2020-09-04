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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {
    private String userInfo;
    private String loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        checkSelfPermission();

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
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Open the connection
                            URL url = new URL("https://api.bistroad.kr/v1/auth/token");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            JSONObject jsonInfo = new JSONObject();
                            jsonInfo.accumulate("username", id.getText().toString());
                            jsonInfo.accumulate("password", password.getText().toString());

                            userInfo = jsonInfo.toString();
                            conn.setRequestProperty("Accept", "application/json");
                            conn.setRequestProperty("Content-type", "application/json");

                            conn.setRequestMethod("POST");
                            conn.setDefaultUseCaches(false);
                            conn.setDoInput(true);
                            conn.setDoOutput(true);

                            OutputStream os = conn.getOutputStream();

                            os.write(userInfo.getBytes("euc-kr"));

                            os.flush();
                            if(conn.getResponseCode() == 200){
                                InputStream is = conn.getInputStream();
                                loginInfo = convertStreamToString(is);
                                JSONObject jsonLogin = new JSONObject(loginInfo);
                                String token = jsonLogin.getString("access_token");//
                                SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);
                                SharedPreferences.Editor editor = tk.edit();
                                editor.putString("bistrotk", token); //
                                editor.commit();
                                Log.e("TOKEN", token);

                                Intent intent = new Intent(Login.this, LoginConfirmed.class);
                                startActivity(intent);
                            }
                            else{
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

            private String convertStreamToString(InputStream is)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();

                String line = null;

                try
                {
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    try
                    {
                        is.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return sb.toString();
            }
//            @Override
//            public void onClick(View view) {
//
//                userId = id.getText().toString();
//                userPassword = password.getText().toString();
//
//                PendingIntent pendnoti = PendingIntent.getActivity(Login.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(Login.this,NOTIFICATION_ID)
//                        .setContentTitle("맛있는 시간 되셨나요?") //타이틀 TEXT
//                        .setContentText("바쁘시겠지만 리뷰 부탁드려요!") //세부내용 TEXT
//                        .setSmallIcon (R.drawable.logo)
//                        .setContentIntent(pendnoti);
//
//                notificationManager.notify(0, builder.build());
//            }
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
