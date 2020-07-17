package com.example.yaneodoo.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.ListView.BistroListViewItem;
import com.example.yaneodoo.R;

public class ShowCustomerBistroList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_list_customer);

        // TODO : db에서 손님의 근처의 가게데이터를 가져옴

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

                Drawable iconDrawable = item.getIcon() ;
                String titleStr = item.getTitle() ;
                String locationStr=item.getLocationStr() ;
                String descStr = item.getDesc() ;

                Intent intent = new Intent(ShowCustomerBistroList.this, ShowCustomerBistroList.class);
                intent.putExtra("selectedBistro",titleStr);
                startActivity(intent);
            }
        }) ;

    }
}

