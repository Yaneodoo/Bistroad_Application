package com.example.yaneodoo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Customer.ShowCustomerBistroList;
import com.example.yaneodoo.Owner.ShowOwnerBistroList;
import com.example.yaneodoo.REST.GetImage;
import com.example.yaneodoo.REST.RestGetUserInfo;

import java.util.concurrent.ExecutionException;

public class LoginConfirmed extends AppCompatActivity {
    private String token;
    private String profileUrl;
    private String name;
    private String role;
    private int rc;
    private String  url = "https://api.bistroad.kr/v1/users/me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_confirmed);

        SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);
        token = tk.getString("bistrotk","");
        profileUrl = tk.getString("profileUrl","");
        Log.d("TOKEN", token);
        final TextView loginText = (TextView)findViewById(R.id.login_profile_text);
        RestGetUserInfo restGetUserInfo = new RestGetUserInfo(url, token, tk);
        try {
            String rs = restGetUserInfo.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GetImage getStoreImage = new GetImage();
        try {
            Bitmap sbitmap = getStoreImage.execute(profileUrl).get();
            de.hdodenhof.circleimageview.CircleImageView bistroRepresentImage=(de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.login_confirmed_profile_image);
            bistroRepresentImage.setImageBitmap(sbitmap);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        name = tk.getString("fullName","");
        role = tk.getString("role","");
        Log.d("name", name);
        Log.d("role", role);

        loginText.setText(name);
        Handler hd = new Handler();

        if(role.equals("ROLE_STORE_OWNER"))
            hd.postDelayed(new confirmedHandlerOwner(), 2000); // 1초 후에 hd handler 실행  3000ms = 3초
        else if(role.equals("ROLE_USER")) {
            Intent intent = new Intent(getApplicationContext(), GetCurrentGPSService.class);
            startService(intent);
            hd.postDelayed(new confirmedHandlerCustomer(), 2000); // 1초 후에 hd handler 실행  3000ms = 3초
        }

    }

    private class confirmedHandlerCustomer implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), ShowCustomerBistroList.class)); //로딩이 끝난 후, ChoiceFunction 이동
            LoginConfirmed.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }

    private class confirmedHandlerOwner implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), ShowOwnerBistroList.class)); //로딩이 끝난 후, ChoiceFunction 이동
            LoginConfirmed.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }

    @Override
    public void onBackPressed() {
        //플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }
}