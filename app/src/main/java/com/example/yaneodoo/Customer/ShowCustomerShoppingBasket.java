package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.ListView.ShoppingBasketListViewAdapter;
import com.example.yaneodoo.R;

public class ShowCustomerShoppingBasket extends AppCompatActivity {

    private ShoppingBasketListViewAdapter adapter = new ShoppingBasketListViewAdapter();
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingbasket_customer);

        listview = (ListView) findViewById(R.id.shoppingbasket_list_view_customer);

        // 리스트뷰 참조 및 Adapter달기
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem("떡볶이", "5000원");
        adapter.addItem("오뎅", "3000원");

        // 최종 주문 버튼 클릭 리스너
        Button shoppingbasketOrderBtn = findViewById(R.id.shoppingbasket_order_btn);
        shoppingbasketOrderBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : 팝업으로 띄울건지 등의 논의 후 구현
                //PopupMenu pop = new PopupMenu(getApplicationContext(), view);
                Toast.makeText(getApplicationContext(), "주문이 완료되었습니다!", Toast.LENGTH_LONG).show();
                ShowCustomerShoppingBasket.this.finish();
                Intent intent = new Intent(ShowCustomerShoppingBasket.this, ShowCustomerBistroList.class);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerShoppingBasket.this, ShowCustomerBistroList.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ShowCustomerShoppingBasket.this.finish();
                startActivity(intent);
            }
        });

        // TODO : mypagebtn 클릭 리스너
    }

    // 더담으러가기 텍스트뷰 클릭 리스너
    public void backToMenuList(View v) {
        ShowCustomerShoppingBasket.this.finish();
        Intent intent = new Intent(ShowCustomerShoppingBasket.this, ShowCustomerMenuList.class);
        startActivity(intent);
    }

    // 아이템 삭제 버튼 클릭 리스너
    public void deleteItem(View v) {
        int position = listview.getPositionForView(v);
        adapter.deleteItem(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        ShowCustomerShoppingBasket.this.finish();
        startActivity(new Intent(this, ShowCustomerMenuList.class));
    }
}
