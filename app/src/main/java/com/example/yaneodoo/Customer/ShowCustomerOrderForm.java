package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.R;

public class ShowCustomerOrderForm extends AppCompatActivity {
    // FOR ACTIVITY SWITCH. SHOW USER'S ORDER FORM.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_form_customer);
    }

    @Override
    public void onBackPressed() {
        ShowCustomerOrderForm.this.finish();
        startActivity(new Intent(this, ShowCustomerMenuList.class));
    }
}
