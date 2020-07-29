package com.example.yaneodoo.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.ReviewListViewAdapter;
import com.example.yaneodoo.Owner.RegisterMenu;
import com.example.yaneodoo.Owner.ShowOwnerBistroList;
import com.example.yaneodoo.R;

public class ShowMenuInfo extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_info);

        // ShowCustomerMenuList나 ShowOwnerMenuList에서 보낸 menuStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        final String menuName=intent.getStringExtra("selectedMenu");
        // TODO : GET /stores/{storeId}/items/{itemId}/reviews 로 데이터 가져와서 listview에 아이템 추가


        // Adapter 생성
        ReviewListViewAdapter adapter = new ReviewListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.review_list_view);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!맛있었다!맛있었다!맛있었다!" +
                "\n맛있었다!\n맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mypage), "2020.07.09", "민주짱", "떡볶이떡볶이떡볶이떡볶이떡볶이떡볶이","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.edit), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");

        // TODO : 동적으로 owner일 때만 메뉴 txtView옆에 edit버튼 생성(실행하여 확인 필요)
        RelativeLayout topLL = (RelativeLayout)findViewById(R.id.dynamicArea);
        TextView tv = new TextView(this);
        tv.setText(menuName);
        tv.setTextSize(26);
        tv.setId(R.id.menu_name_txtView);
        topLL.addView(tv);

        RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        Button btn_edit = new Button(this);
        btn_edit.setText("edit");
        buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        btn_edit.setLayoutParams(buttonLayoutParams);
        topLL.addView(btn_edit);

        // 수정 버튼 클릭 리스너
        btn_edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShowMenuInfo.this, RegisterMenu.class);
                intent.putExtra("selectedMenu",menuName);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 손님이면 ShowCustomerBistroList로 이동
                Intent intent = new Intent(ShowMenuInfo.this, ShowOwnerBistroList.class);
                startActivity(intent);
            }
        }) ;

        // TODO : mypagebtn 클릭 리스너
    }
}