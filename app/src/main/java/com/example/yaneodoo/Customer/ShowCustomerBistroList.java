package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.BackPressedForFinish;
import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.ListView.BistroListViewItem;
import com.example.yaneodoo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShowCustomerBistroList extends AppCompatActivity {
    private BackPressedForFinish backPressedForFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_list_customer);
        backPressedForFinish = new BackPressedForFinish(this);

        // TODO : GET /nearby-stores하여 받아온 정보 아이템으로 추가

        // Adapter 생성
        BistroListViewAdapter adapter = new BistroListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.bistro_list_view_customer);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "레드 175", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "윤스쿡", "서울시 용산구 새창로 70", "식전빵이 매우 맛있는곳\n" +
                "테스트 문장입니다. 테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mypage), "아웃백", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.img_upload), "동대문엽기떡볶이", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.edit), "계이득", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.shopping_basket), "계이득", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "계이득", "서울시 동작구", "#짜장 #짬뽕");

        //가게 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                BistroListViewItem item = (BistroListViewItem) parent.getItemAtPosition(position);

                String titleStr = item.getTitle();

                Intent intent = new Intent(ShowCustomerBistroList.this, ShowCustomerMenuList.class);
                intent.putExtra("selectedBistro", titleStr);
                ShowCustomerBistroList.this.finish();
                startActivity(intent);
            }
        }) ;

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerBistroList.this, ShowCustomerBistroList.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ShowCustomerBistroList.this.finish();
                startActivity(intent);
            }
        });

        // TODO : mypagebtn 클릭 리스너

        // FAB 클릭 리스너
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowCustomerBistroList.this, ShowCustomerShoppingBasket.class);
                ShowCustomerBistroList.this.finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // BackPressedForFinish 클래시의 onBackPressed() 함수를 호출한다.
        backPressedForFinish.onBackPressed();
    }
}

