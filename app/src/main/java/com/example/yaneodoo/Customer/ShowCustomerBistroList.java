package com.example.yaneodoo.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.ListView.BistroListViewItem;
import com.example.yaneodoo.R;

public class ShowCustomerBistroList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_list_customer);

        // TODO : GET /nearby-stores하여 받아온 정보 아이템으로 추가

        // Adapter 생성
        BistroListViewAdapter adapter = new BistroListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.bistro_list_view_customer);
        listview.setAdapter(adapter);

        // 아이템 추가 예시1
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "레드 175", "서울시 동작구", "#짜장 #짬뽕");
        // 아이템 추가 예시2
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mypage), "얄리얄리얄랴셩", "사랑시 고백구", "#맛집 #또먹");

        //가게 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                BistroListViewItem item = (BistroListViewItem) parent.getItemAtPosition(position) ;

                String titleStr = item.getTitle() ;

                Intent intent = new Intent(ShowCustomerBistroList.this, ShowCustomerMenuList.class);
                intent.putExtra("selectedBistro",titleStr);
                startActivity(intent);
            }
        }) ;

        // 홈 버튼 클릭 리스너
        Button btnHome = (Button) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerBistroList.this, ShowCustomerBistroList.class);
                startActivity(intent);
            }
        }) ;

        // TODO : mypagebtn 클릭 리스너
    }
}

