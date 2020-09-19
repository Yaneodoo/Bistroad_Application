package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.OrderListViewAdapter;
import com.example.yaneodoo.Login;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.RestGetOrders;

import java.util.concurrent.ExecutionException;

public class MyPageCustomer extends AppCompatActivity {
    private String token, id, role, orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_customer);
        final SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);

        final TextView nameText = (TextView)findViewById(R.id.cutomer_name_textView);
        nameText.setText(tk.getString("fullName",""));

        token = tk.getString("bistrotk","");
        id = tk.getString("id","");
        role = tk.getString("role", "");

        RestGetOrders restGetOrders = new RestGetOrders(id, token, role);
        try {
            orderInfo = restGetOrders.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Log.d("orderInfo", orderInfo);

        // Adapter 생성
        OrderListViewAdapter orderListViewAdapter = new OrderListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.order_list_view_customer);
        listview.setAdapter(orderListViewAdapter);

        // 아이템 추가 예시
        orderListViewAdapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "horseesroh", "짜장 x 1\n짬뽕 x 2", "접수중","");
        orderListViewAdapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "elsiff", "짜장 x 1\n짬뽕 x 2", "접수중","");
        orderListViewAdapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "himinju", "짜장 x 1\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2", "접수중","");
        orderListViewAdapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "vomin", "짜장 x 1\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2", "접수중","");
        orderListViewAdapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "Yalru", "짜장 x 1\n짬뽕 x 2", "접수중","");
        orderListViewAdapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "tjwlsgkkgslwjt", "짜장 x 1\n짬뽕 x 2", "접수중","");
        orderListViewAdapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "2020-02-09", "tjwlsgkkgslwjt", "짜장 x 1\n짬뽕 x 2", "접수 완료","");

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // 주문 아이템 하나 선택해도 일단은 아무것도 안함.
            }
        });

        // 손님 버튼 클릭 리스너
        Button btnCustomer = (Button) findViewById(R.id.mypage_logout_button);
        btnCustomer.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = tk.edit();
                editor.putString("bId", ""); //
                editor.commit();
                Intent intent = new Intent(MyPageCustomer.this, Login.class);
                startActivity(intent);
                MyPageCustomer.this.finish();
            }
        });



    }
    @Override
    public void onBackPressed() {
        this.finish();
    }
}
