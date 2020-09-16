package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.OrderListViewAdapter;
import com.example.yaneodoo.R;

public class ShowOwnerOrderList extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_order_list_owner);

        // ShowOwnerMenuList에서 보낸 titleStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        final String bistroName = intent.getStringExtra("bistroStr");

        // TODO : GET /stores/{storeId}/orders로 가게의 주문내역을 모두 아이템에 추가
        // 날짜 최신순

        // Adapter 생성
        OrderListViewAdapter adapter = new OrderListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.order_list_view_owner);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "horseesroh", "짜장 x 1\n짬뽕 x 2", "접수중");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "elsiff", "짜장 x 1\n짬뽕 x 2", "접수중");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "himinju", "짜장 x 1\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2", "접수중");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "vomin", "짜장 x 1\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2", "접수중");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "Yalru", "짜장 x 1\n짬뽕 x 2", "접수중");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "tjwlsgkkgslwjt", "짜장 x 1\n짬뽕 x 2", "접수중");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "2020-02-09", "tjwlsgkkgslwjt", "짜장 x 1\n짬뽕 x 2", "접수 완료");

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // 주문 아이템 하나 선택해도 일단은 아무것도 안함.
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerOrderList.this, ShowOwnerBistroList.class);
                ShowOwnerOrderList.this.finish();
                startActivity(intent);
            }
        });

        // TODO : mypagebtn 클릭 리스너
    }

    // 주문접수 토글 버튼 클릭 리스너
    public void progressToggle(View v) {
        LinearLayout parentRow = (LinearLayout) v.getParent();
        TextView orderState = (TextView) parentRow.findViewById(R.id.order_progress);

        LinearLayout pparentRow = (LinearLayout) parentRow.getParent();
        TextView orderDate = (TextView) pparentRow.findViewById(R.id.order_date_txtView);
        TextView orderCustomerId = (TextView) pparentRow.findViewById(R.id.order_customer_txtView);

        ToggleButton tb2 = (ToggleButton) v.findViewById(R.id.btn_progress);
        Log.d("tag", orderState.getText().toString());
        if (orderState.getText().toString() == "접수중") {
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.accepted));
            orderState.setText("접수 완료");
        } else if (orderState.getText().toString() == "접수 완료") {
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.requested));
            orderState.setText("접수중");
        }
    }
}