package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Login;
import com.example.yaneodoo.R;
import com.example.yaneodoo.RetrofitService;

import retrofit2.Retrofit;

public class MyPageOwner extends AppCompatActivity {
    private String token, id, name, orderInfo;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_owner);
        final SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");
        name = getSharedPreferences("sFile", MODE_PRIVATE).getString("fullName", "");
        final TextView nameText = (TextView) findViewById(R.id.owner_name_textView);
        nameText.setText(name + " 점주님");

        // 손님 버튼 클릭 리스너
        Button btnCustomer = (Button) findViewById(R.id.mypage_logout_button);
        btnCustomer.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = tk.edit();
                editor.putString("bId", ""); //
                editor.commit();
                Intent intent = new Intent(MyPageOwner.this, Login.class);
                startActivity(intent);
                MyPageOwner.this.finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        this.finish();
    }
}
