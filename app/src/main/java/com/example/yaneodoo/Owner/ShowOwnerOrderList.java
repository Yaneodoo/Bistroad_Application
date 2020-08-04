package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.OrderListViewAdapter;
import com.example.yaneodoo.R;

public class ShowOwnerOrderList extends AppCompatActivity implements OrderListViewAdapter.ListBtnClickListener {
    private Intent intent;

    @Override
    public void onListBtnClick(int position) {
        OrderListViewAdapter adapter = new OrderListViewAdapter(this);

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.order_list_view_owner);
        listview.setAdapter(adapter);

        // TODO : 해당 position의 progress를 반전
        final ToggleButton tb2 = (ToggleButton) this.findViewById(R.id.btn_progress);
        if (tb2.isChecked()) {
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.accepted));
        } else {
            tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.requested));
        }

        adapter.notifyDataSetChanged();

        // TODO : 선택한 아이템의 상태정보 update
        // String ordernum = ((OrderListViewItem)adapter.getItem(position)).getOrderNum();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_order_list_owner);

        // ShowOwnerMenuList에서 보낸 titleStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        final String bistroName=intent.getStringExtra("bistroStr");

        // TODO : GET /stores/{storeId}/orders로 가게의 주문내역을 모두 아이템에 추가
        // 날짜 최신순

        // Adapter 생성
        OrderListViewAdapter adapter = new OrderListViewAdapter(this);

        // 리스트뷰 참조 및 Adapter달기
        ListView listview = (ListView) findViewById(R.id.order_list_view_owner);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "horseesroh", "짜장 x 1\n짬뽕 x 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "2020-02-09", "minju", "짜장 x 1\n짬뽕 x 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.requested), "2020-02-09", "youbin", "짜장 x 1\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "2020-02-09", "elsif", "짜장 x 1\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "2020-02-09", "trump", "짜장 x 1\n짬뽕 x 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "2020-02-09", "rilakkuma", "짜장 x 1\n짬뽕 x 2");

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerOrderList.this, ShowOwnerBistroList.class);
                startActivity(intent);
            }
        });

        // TODO : mypagebtn 클릭 리스너
    }
}
