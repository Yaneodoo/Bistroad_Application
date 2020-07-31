package com.example.yaneodoo.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Customer.ShowCustomerBistroList;
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
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mypage), "2020.07.09", "민주짱", "떡볶이떡볶이떡볶이떡","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.edit), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020.07.09", "민주짱", "떡볶이","★4.3","맛있었다!");


        RelativeLayout topLL = (RelativeLayout) findViewById(R.id.user_ori_dynamic_area);
        TextView tv = new TextView(this);
        tv.setText(menuName);
        tv.setTextSize(15);
        tv.setId(R.id.menu_name_txtView);
        topLL.addView(tv);

        RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //buttonLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        // TODO : customer일 때 주문하기버튼 생성
        Button btn_order=new Button(this);
        buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        btn_order.setLayoutParams(buttonLayoutParams);
        btn_order.setText("주문하기");
        btn_order.setTextSize(10);
        topLL.addView(btn_order);

        // TODO : 주문 버튼 클릭 리스너
        btn_order.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 주문 페이지로
            }
        });

        // TODO : owner일 때 edit버튼 생성
        ImageButton btn_edit=new ImageButton(this);
        buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        btn_edit.setLayoutParams(buttonLayoutParams);
        btn_edit.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit));
        btn_edit.getLayoutParams().height = 15;
        btn_edit.setScaleType(ImageButton.ScaleType.FIT_CENTER);
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
                String user = intent.getStringExtra("customer");
                if (user.compareTo("customer") == 0) {
                    Intent intent = new Intent(ShowMenuInfo.this, ShowCustomerBistroList.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ShowMenuInfo.this, ShowOwnerBistroList.class);
                    startActivity(intent);
                }
            }
        }) ;

        // TODO : mypagebtn 클릭 리스너
    }
}