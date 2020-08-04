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

import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.ListView.BistroListViewItem;
import com.example.yaneodoo.R;

import java.util.ArrayList;

public class ShowOwnerBistroList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_list_owner);

        // TODO : owNerId로 GET /stores하여 얻은 정보 아이템으로 추가

        // Adapter 생성
        final ArrayList<BistroListViewItem> listViewItemList = new ArrayList<>();
        final BistroListViewAdapter adapter = new BistroListViewAdapter(this, android.R.layout.simple_list_item_multiple_choice, listViewItemList);

        // 리스트뷰 참조, 멀티 선택(체크박스) 설정, Adapter달기
        final ListView listview = (ListView) findViewById(R.id.bistro_list_view_owner);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "레드 175", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "윤스쿡", "서울시 용산구 새창로 70", "식전빵이 매우 맛있는곳\n" +
                "테스트 문장입니다. 테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mypage), "아웃백", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.img_upload), "동대문엽기떡볶이", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.edit), "계이득", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.edit), "계이득", "서울시 동작구", "#짜장 #짬뽕");

        //가게 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                BistroListViewItem item = (BistroListViewItem) parent.getItemAtPosition(position);
                String titleStr = item.getTitle();

                Intent intent = new Intent(ShowOwnerBistroList.this, ShowOwnerMenuList.class);
                intent.putExtra("selectedBistro", titleStr);
                startActivity(intent);
            }
        });

        // 추가 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_add) ;
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerBistroList.this, RegisterBistro.class);
                startActivity(intent);

            }
        });

        // 삭제 버튼 클릭 리스너
        final Button delbtn = (Button) findViewById(R.id.btn_delete) ;
        delbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delbtn.getText()=="삭제"){
                    // TODO : onchoice활성화
                    // add버튼 삭제
                    /*
                    LinearLayout topLL = (LinearLayout) findViewById(R.id.bistro_delete_dynamic_area);
                    Button addbtn = (Button) findViewById(R.id.btn_add);
                    topLL.removeView(addbtn);

                    delbtn.setText("확인");
                    */
                }
                else{
                    // TODO : (삭제)확인 버튼 클릭 리스너
                    // DELETE /stores/{storeId}로 선택한 매장들 삭제
                    // add버튼 생성
                    /*
                    Button btn_order=new Button(ShowOwnerBistroList.this);
                    btn_order.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f));
                    btn_order.setText("추가");
                    btn_order.setTextSize(10);
                    btn_order.setId(R.id.btn_add);
                    LinearLayout topLL = (LinearLayout) findViewById(R.id.bistro_delete_dynamic_area);
                    topLL.addView(btn_order);
                    delbtn.setText("삭제");
                    delbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f));
                    */
                }
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowOwnerBistroList.this, ShowOwnerBistroList.class);
                startActivity(intent);
            }
        }) ;

        // TODO : mypagebtn 클릭 리스너
    }
}