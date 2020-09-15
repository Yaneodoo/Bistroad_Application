package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.R;

public class ShowCustomerOrderForm extends AppCompatActivity {
    int menuQuantity;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_form_customer);

        intent = getIntent();
        //final User user = (User) intent.getSerializableExtra("userInfo");
        final Menu menu = (Menu) intent.getSerializableExtra("menuInfo");

        TextView menuNameTxtView = (TextView) findViewById(R.id.menu_name_txtView);
        menuNameTxtView.setText(menu.getName());
        TextView menuDescTxtView = (TextView) findViewById(R.id.menu_desc_txtView);
        menuDescTxtView.setText(menu.getDescription());
        TextView menuStarsTxtView = (TextView) findViewById(R.id.menu_stars_txtView);
        menuStarsTxtView.setText(menu.getStars());

        TextView menuPriceTxtView = (TextView) findViewById(R.id.menu_price_txtView);
        menuPriceTxtView.setText(menu.getPrice());

        Button pickupbtn = (Button) findViewById(R.id.btn_pick_up);
        pickupbtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowCustomerOrderForm.this.finish();
                startActivity(new Intent(ShowCustomerOrderForm.this, ShowCustomerMenuList.class));
            }
        });
    }

    public void Decrement(View view) {
        TableRow parentRow = (TableRow) view.getParent();

        TextView menuQuantityTxtView = (TextView) parentRow.findViewById(R.id.menu_quantity);
        String quantityString = menuQuantityTxtView.getText().toString();
        menuQuantity = Integer.parseInt(quantityString);
        menuQuantity -= 1;

        if (menuQuantity < 1) {
            menuQuantity = 1;
            Toast.makeText(ShowCustomerOrderForm.this, "Can not be less than 1",
                    Toast.LENGTH_SHORT).show();
        }
        menuQuantityTxtView.setText(String.valueOf(menuQuantity));
    }

    public void Increment(View view) {
        TableRow parentRow = (TableRow) view.getParent();

        TextView menuQuantityTxtView = (TextView) parentRow.findViewById(R.id.menu_quantity);
        String quantityString = menuQuantityTxtView.getText().toString();
        menuQuantity = Integer.parseInt(quantityString);
        menuQuantity += 1;
        menuQuantityTxtView.setText(String.valueOf(menuQuantity));
    }

    @Override
    public void onBackPressed() {
        ShowCustomerOrderForm.this.finish();
        startActivity(new Intent(this, ShowCustomerMenuList.class));
    }
}
