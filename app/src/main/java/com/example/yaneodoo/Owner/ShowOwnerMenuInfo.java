package com.example.yaneodoo.Owner;

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

public class ShowOwnerMenuInfo extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_info_owner);

        // ShowCustomerMenuList나 ShowOwnerMenuList에서 보낸 menuStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        final String menuName=intent.getStringExtra("selectedMenu");
        // TODO : GET /stores/{storeId}/items/{itemId}/reviews 로 데이터 가져와서 listview에 아이템 추가

        // Adapter 생성
        ReviewListViewAdapter adapter = new ReviewListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.review_list_view);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이", "★4.3", "맛있었다!맛있었다!맛있었다!맛있었다!" +
                "\n맛있었다!\n맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mypage), "2020.07.09", "민주짱", "떡볶이떡볶이떡볶이떡","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.edit), "2020.07.09", "민주짱", "떡볶이", "★4.3", "맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이", "★4.3", "맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이", "★4.3", "맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이", "★4.3", "맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이", "★4.3", "맛있었다!");

        TextView menu_name_txtview = (TextView) findViewById(R.id.menu_name_txtView);
        menu_name_txtview.setText(menuName);

        // TODO : owner일 때 edit버튼 생성
        Button editbutton = (Button) findViewById(R.id.btn_edit);

        Button orderlistbtn = (Button) findViewById(R.id.btn_orderlist);
        orderlistbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShowOwnerMenuInfo.this, ShowOwnerOrderList.class);
                startActivity(intent);
            }
        });

        // 수정 버튼 클릭 리스너
        editbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShowOwnerMenuInfo.this, RegisterMenu.class);
                intent.putExtra("selectedMenu", menuName);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = intent.getStringExtra("customer");
                    Intent intent = new Intent(ShowOwnerMenuInfo.this, ShowOwnerBistroList.class);
                    startActivity(intent);
            }
        }) ;

        // TODO : mypagebtn 클릭 리스너
    }
}