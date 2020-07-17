package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.MenuListViewAdapter;
import com.example.yaneodoo.ListView.MenuListViewItem;
import com.example.yaneodoo.R;
import com.example.yaneodoo.Common.ShowMenuInfo;

public class ShowOwnerMenuList extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list_owner);

        // TODO : db에서 선택된 매장의 메뉴데이터를 가져옴
        // ShowBistroList 보낸 titleStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        String bistroName=intent.getStringExtra("selectedBistro");


        // Adapter 생성
        MenuListViewAdapter adapter = new MenuListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.menu_list_view_owner);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "레드 175", "서울시 동작구", "#짜장 #짬뽕","4.3","33");

        //메뉴 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                MenuListViewItem item = (MenuListViewItem) parent.getItemAtPosition(position);
                String titleStr = item.getMenuStr();

                Intent intent = new Intent(ShowOwnerMenuList.this, ShowMenuInfo.class);
                intent.putExtra("selectedMenu", titleStr);
                startActivity(intent);
            }
        });

        // 수정 버튼 클릭 리스너
        ImageButton editbtn = (ImageButton) findViewById(R.id.btn_edit) ;
        editbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, RegisterMenu.class);

                // TODO : 기존 메뉴 정보 intent로 채워서 전달
                //MenuInfo menuInfo=new MenuInfo(?);
                //intent.putExtra("menuInfo",menuInfo);
                startActivity(intent);
            }
        });

        // 추가 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_add) ;
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, RegisterMenu.class);
                startActivity(intent);
            }
        });

        // 삭제 버튼 클릭 리스너
        Button delbtn = (Button) findViewById(R.id.btn_delete) ;
        delbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : onchoice 이용?
            }
        });
    }
}