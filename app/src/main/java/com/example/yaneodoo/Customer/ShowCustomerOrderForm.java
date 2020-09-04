package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.R;

public class ShowCustomerOrderForm extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_form_customer);

        Button pickupbtn = (Button) findViewById(R.id.btn_pick_up);
        pickupbtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowCustomerOrderForm.this.finish();
                startActivity(new Intent(ShowCustomerOrderForm.this, ShowCustomerMenuList.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        ShowCustomerOrderForm.this.finish();
        startActivity(new Intent(this, ShowCustomerMenuList.class));
    }
}
