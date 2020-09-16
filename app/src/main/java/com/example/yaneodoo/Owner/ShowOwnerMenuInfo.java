package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Info.Menu;
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
        final Menu menu = (Menu) intent.getSerializableExtra("menuInfo");

        TextView menuNameTxtView = (TextView) findViewById(R.id.menu_name_txtView);
        menuNameTxtView.setText(menu.getName());
        TextView menuPriceTxtView = (TextView) findViewById(R.id.menu_price_txtView);
        menuPriceTxtView.setText(menu.getPrice());
        TextView menuDescTxtView = (TextView) findViewById(R.id.menu_desc_txtView);
        menuDescTxtView.setText(menu.getDescription());
        TextView menuStarsTxtView = (TextView) findViewById(R.id.menu_stars_txtView);
        menuStarsTxtView.setText(menu.getStars());
        //TextView menuNameTxtView = (TextView) findViewById(R.id.menu_orderedCnt_txtView);
        //menuNameTxtView.setText(menu.getName());

        // TODO : GET /stores/{storeId}/items/{itemId}/reviews 로 데이터 가져와서 listview에 아이템 추가

        // Adapter 생성
        ReviewListViewAdapter adapter = new ReviewListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.review_list_view_owner);
        listview.setAdapter(adapter);

        Button editbutton = (Button) findViewById(R.id.btn_edit);

        // 수정 버튼 클릭 리스너
        editbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShowOwnerMenuInfo.this, RegisterMenu.class);
                intent.putExtra("selectedMenu", menu);
                ShowOwnerMenuInfo.this.finish();
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerMenuInfo.this, ShowOwnerBistroList.class);
                ShowOwnerMenuInfo.this.finish();
                startActivity(intent);
            }
        });

        // TODO : mypagebtn 클릭 리스너
    }

    @Override
    public void onBackPressed() {
        ShowOwnerMenuInfo.this.finish();
        startActivity(new Intent(this, ShowOwnerMenuList.class));
    }
}