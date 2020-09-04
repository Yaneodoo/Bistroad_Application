package com.example.yaneodoo.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.MenuListViewCustomerAdapter;
import com.example.yaneodoo.ListView.MenuListViewItem;
import com.example.yaneodoo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        final MenuListViewCustomerAdapter adapter = new MenuListViewCustomerAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.menu_list_view_customer);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "떡볶이", "12345", "#떡순튀 #매운맛 조절 가능", "★4.3", " ");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.eomuk), "어묵탕", "12345", "#떡순튀 #매운맛 조절 가능 #떡순튀 #매운맛 조절 가능 #떡순튀\n" +
                "#매운맛 조절 가능 #떡순튀 #매운맛 조절 가능 #떡순튀 #매운맛 조절 가능\n#떡순튀 #매운맛 조절 가능", "★4.3", " ");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.sundae), "순대", "100000", "#짜장 #짬뽕", "★4.3", " ");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tempura), "모듬튀김", "서울시 동작구", "#짜장 #짬뽕", "★4.3", " ");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.udon), "우동", "서울시 동작구", "#짜장 #짬뽕", "★4.3", " ");

        Log.d("a", "b");
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

                String TAG = "MainActivity";
                Log.i(TAG, item.getMenuStr());

                ShowCustomerMenuList.this.finish();
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCustomerMenuList.this, ShowCustomerBistroList.class);
                ShowCustomerMenuList.this.finish();
                startActivity(intent);
            }
        });

        // TODO : mypagebtn 클릭 리스너

        // FAB 클릭 리스너
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowCustomerMenuList.this, ShowCustomerShoppingBasket.class);
                ShowCustomerMenuList.this.finish();
                startActivity(intent);
            }
        });
    }

    // 주문하기 버튼 클릭 리스너
    public void orderMenu(View v) {
        RelativeLayout parentRow = (RelativeLayout) v.getParent();
        TextView menuName = (TextView) parentRow.findViewById(R.id.menu_name_txtView);
        String mname = menuName.getText().toString();
        Intent intent = new Intent(ShowCustomerMenuList.this, ShowCustomerOrderForm.class);
        intent.putExtra("mname", mname);
        ShowCustomerMenuList.this.finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        ShowCustomerMenuList.this.finish();
        startActivity(new Intent(this, ShowCustomerBistroList.class));
    }
}