package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.MenuListViewAdapter;
import com.example.yaneodoo.ListView.MenuListViewItem;
import com.example.yaneodoo.R;
import com.example.yaneodoo.Common.ShowMenuInfo;

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
                String menuStr = item.getMenuStr();

                Intent intent = new Intent(ShowCustomerMenuList.this, ShowMenuInfo.class);
                intent.putExtra("selectedMenu", menuStr);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        Button btnHome = (Button) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerMenuList.this, ShowCustomerBistroList.class);
                startActivity(intent);
            }
        }) ;

        // TODO : mypagebtn 클릭 리스너

    }
}