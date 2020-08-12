package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.ReviewListViewAdapter;
import com.example.yaneodoo.R;

public class ShowCustomerMenuInfo extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_info_customer);

        // ShowCustomerMenuList나 ShowOwnerMenuList에서 보낸 menuStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        final String menuName=intent.getStringExtra("selectedMenu");
        // TODO : GET /stores/{storeId}/items/{itemId}/reviews 로 데이터 가져와서 listview에 아이템 추가

        // Adapter 생성
        ReviewListViewAdapter adapter = new ReviewListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.review_list_view_customer);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "horseesroh", "떡볶이", "★4.3", "맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.sundae), "2020.07.09", "elsiff", "떡볶이", "★4.3", "맛있었다!맛있었다!맛있었다!맛있었다!" +
                "\n맛있었다!\n맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.eomuk), "2020.07.09", "minju", "떡볶이떡볶이떡볶이떡", "★4.3", "맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "vomin", "떡볶이", "★4.3", "맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "yalru", "떡볶이", "★4.3", "맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "hello", "떡볶이", "★4.3", "맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "idkmyname", "떡볶이", "★4.3", "맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "officialll", "떡볶이", "★4.3", "맛있었다!");



        Button btn_order = (Button) findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView menuName = (TextView) findViewById(R.id.menu_name_txtView);
                String mname = menuName.getText().toString();
                Intent intent = new Intent(ShowCustomerMenuInfo.this, ShowCustomerOrderForm.class);
                intent.putExtra("mname", mname);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerMenuInfo.this, ShowCustomerBistroList.class);
                startActivity(intent);
            }
        }) ;

        // TODO : mypagebtn 클릭 리스너
    }
}