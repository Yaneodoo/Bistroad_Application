package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Customer.ShowCustomerBistroList;
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
        TextView order_progress=(TextView) findViewById(R.id.order_progress);
        ImageButton progressbtn=(ImageButton) findViewById(R.id.btn_progress);
        if(order_progress.getText()=="접수 완료"){
            progressbtn.setImageResource(R.drawable.requested);
            order_progress.setText("접수중");
        }
        else{
            progressbtn.setImageResource(R.drawable.accepted);
            order_progress.setText("접수 완료");
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
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020-02-09", "지나찡", "짜장 x 1\n짬뽕 x 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "2020-02-09", "지나찡", "짜장 x 1\n짬뽕 x 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mypage), "2020-02-09", "지나찡", "짜장 x 1\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.img_upload), "2020-02-09", "지나찡", "짜장 x 1\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2\n짬뽕 x 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020-02-09", "지나찡", "짜장 x 1\n짬뽕 x 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020-02-09", "지나찡지나찡지나찡지나찡지나찡", "짜장 x 1\n짬뽕 x 2\n짬뽕 x 2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "2020-02-09", "지나찡", "짜장 x 1\n짬뽕 x 2");

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // 주문 아이템 하나 선택해도 일단은 아무것도 안함.
            }
        }) ;

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerOrderList.this, ShowCustomerBistroList.class);
                startActivity(intent);
            }
        }) ;

        // TODO : mypagebtn 클릭 리스너
    }
}
