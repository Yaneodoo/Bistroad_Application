package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.MenuListViewItem;
import com.example.yaneodoo.ListView.MenuListViewOwnerAdapter;
import com.example.yaneodoo.R;

import java.util.ArrayList;

public class ShowOwnerMenuList extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list_owner);

        // ShowOwnerBistroList에서 보낸 titleStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        final String bistroName=intent.getStringExtra("selectedBistro");
        // TODO : GET /stores/{storeId}/items 로 데이터 가져와서 listview에 아이템 추가
        // 별점 높은 순

        // Adapter 생성
        final ArrayList<MenuListViewItem> listViewItemList = new ArrayList<>();
        final MenuListViewOwnerAdapter adapter = new MenuListViewOwnerAdapter(this, android.R.layout.simple_list_item_multiple_choice, listViewItemList);

        // 리스트뷰 참조, 멀티 선택(체크박스) 설정, Adapter달기
        final ListView listview = (ListView) findViewById(R.id.menu_list_view_owner);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "떡볶이", "12345", "#떡순튀 #매운맛 조절 가능", "★4.3", " ");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.eomuk), "어묵탕", "12345", "#떡순튀 #매운맛 조절 가능 #떡순튀 #매운맛 조절 가능 #떡순튀\n" +
                "#매운맛 조절 가능 #떡순튀 #매운맛 조절 가능 #떡순튀 #매운맛 조절 가능\n#떡순튀 #매운맛 조절 가능", "★4.3", " ");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.sundae), "순대", "100000", "#짜장 #짬뽕", "★4.3", " ");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tempura), "모듬튀김", "서울시 동작구", "#짜장 #짬뽕", "★4.3", " ");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.udon), "우동", "서울시 동작구", "#짜장 #짬뽕", "★4.3", " ");

        // 주문내역 버튼 클릭 리스너
        Button btn_orderlist = (Button) findViewById(R.id.btn_orderlist);
        btn_orderlist.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, ShowOwnerOrderList.class);
                intent.putExtra("bistroStr", bistroName);
                startActivity(intent);
            }
        });

        //메뉴 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                MenuListViewItem item = (MenuListViewItem) parent.getItemAtPosition(position);
                String titleStr = item.getMenuStr();

                Intent intent = new Intent(ShowOwnerMenuList.this, ShowOwnerMenuInfo.class);
                intent.putExtra("selectedMenu", titleStr);
                startActivity(intent);
            }
        });

        // 수정 버튼 클릭 리스너
        Button editbtn = (Button) findViewById(R.id.btn_edit) ;
        editbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, RegisterBistro.class);
                intent.putExtra("bistroStr",bistroName);
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

        // TODO : 삭제 버튼 클릭 리스너
        Button delbtn = (Button) findViewById(R.id.btn_delete) ;
        delbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count, checked;
                count = adapter.getCount();

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득
                    checked = listview.getCheckedItemPosition();
                    if (checked > -1 && checked < count) {
                        // 아이템 삭제
                        listViewItemList.remove(checked);
                        // listview 선택 초기화
                        listview.clearChoices();
                        // listview 갱신
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuList.this, ShowOwnerBistroList.class);
                startActivity(intent);
            }
        }) ;

        // TODO : mypagebtn 클릭 리스너
    }
}