package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.ListView.BistroListViewItem;
import com.example.yaneodoo.R;

public class ShowOwnerBistroList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_list_owner);

        // TODO : db에서 점주의 매장데이터를 가져옴

        // Adapter 생성
        BistroListViewAdapter adapter = new BistroListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.bistro_list_view_owner);
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
                BistroListViewItem item = (BistroListViewItem) parent.getItemAtPosition(position);
                String titleStr = item.getTitle();

                Intent intent = new Intent(ShowOwnerBistroList.this, ShowOwnerMenuList.class);
                intent.putExtra("selectedBistro", titleStr);
                startActivity(intent);
            }
        });

        // TODO : 추가 버튼 클릭
        Button addbtn = (Button) findViewById(R.id.btn_add) ;
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerBistroList.this, RegisterBistro.class);
                startActivity(intent);
            }
        });

        // TODO : 삭제 버튼 클릭
        Button delbtn = (Button) findViewById(R.id.btn_delete) ;
        delbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event
                // onchoice 이용?
            }
        });
    }
}