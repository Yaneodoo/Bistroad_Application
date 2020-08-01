package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.MenuListViewAdapter;
import com.example.yaneodoo.ListView.MenuListViewItem;
import com.example.yaneodoo.R;

import java.util.ArrayList;

public class ShowCustomerMenuList extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list_customer);

        // ShowCustomerBistroList에서 보낸 titleStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        String bistroName=intent.getStringExtra("selectedBistro");
        // TODO : GET /stores/{storeId}/items 로 데이터 가져와서 listview에 아이템 추가
        // 별점 높은 순

        // Adapter 생성
        final ArrayList<MenuListViewItem> listViewItemList = new ArrayList<>();
        final MenuListViewAdapter adapter = new MenuListViewAdapter(this, android.R.layout.simple_list_item_multiple_choice, listViewItemList);

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.menu_list_view_customer);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "떡볶이", "12345", "#떡순튀 #매운맛 조절 가능", "★4.3", "");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "떡볶이", "12345", "#떡순튀 #매운맛 조절 가능 #떡순튀 #매운맛 조절 가능 #떡순튀\n" +
                "#매운맛 조절 가능 #떡순튀 #매운맛 조절 가능 #떡순튀 #매운맛 조절 가능\n#떡순튀 #매운맛 조절 가능", "★4.3", "");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mypage), "떡볶이", "100000", "#짜장 #짬뽕", "★4.3", "");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.img_upload), "떡볶이", "서울시 동작구", "#짜장 #짬뽕", "★4.3", "");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "레드 175", "서울시 동작구", "#짜장 #짬뽕", "★4.3", "");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "레드 175", "서울시 동작구", "#짜장 #짬뽕", "★4.3", "");

        //메뉴 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                MenuListViewItem item = (MenuListViewItem) parent.getItemAtPosition(position);
                String menuStr = item.getMenuStr();
                String customer = "";

                Intent intent = new Intent(ShowCustomerMenuList.this, ShowCustomerMenuInfo.class);
                intent.putExtra("selectedMenu", menuStr);
                intent.putExtra("customer", customer);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerMenuList.this, ShowCustomerBistroList.class);
                startActivity(intent);
            }
        }) ;

        // TODO : mypagebtn 클릭 리스너

    }
}