package com.example.yaneodoo.Customer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.R;

public class ShowCustomerShoppingBasket extends AppCompatActivity {
    // FOR ACTIVITY SWITCH. SHOW USER'S SHOPPING BASKET.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingbasket_customer);
    }
}
