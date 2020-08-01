package com.example.yaneodoo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Customer.ShowCustomerBistroList;
import com.example.yaneodoo.Owner.ShowOwnerBistroList;

public class SelectUser extends AppCompatActivity {
    // FOR ACTIVITY SWITCH. ACCORDING TO USER.
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_select);

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
}
