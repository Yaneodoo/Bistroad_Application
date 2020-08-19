package com.example.yaneodoo.Owner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.ListView.BistroListViewItem;
import com.example.yaneodoo.R;

public class ShowOwnerBistroList extends AppCompatActivity {
    boolean onChoice = false;
    boolean removeItems = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_list_owner);

        // TODO : ownerId로 GET /stores하여 얻은 정보 아이템으로 추가

        // Adapter 생성
        final BistroListViewAdapter adapter = new BistroListViewAdapter();

        // 리스트뷰 참조, 멀티 선택(체크박스) 설정, Adapter달기
        final ListView listview = (ListView) findViewById(R.id.bistro_list_view_owner);
        listview.setAdapter(adapter);

        // 아이템 추가 예시
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tteokbokki), "레드 175", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "윤스쿡", "서울시 용산구 새창로 70", "식전빵이 매우 맛있는곳\n" +
                "테스트 문장입니다. 테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.테스트 문장입니다.");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mypage), "아웃백", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.img_upload), "동대문엽기떡볶이", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.edit), "계이득", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.shopping_basket), "계이득", "서울시 동작구", "#짜장 #짬뽕");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.accepted), "계이득", "서울시 동작구", "#짜장 #짬뽕");

        //가게 선택 리스너
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (onChoice) {
                    //
                } else {
                    // get item
                    BistroListViewItem item = (BistroListViewItem) parent.getItemAtPosition(position);
                    String titleStr = item.getTitle();

                    Intent intent = new Intent(ShowOwnerBistroList.this, ShowOwnerMenuList.class);
                    intent.putExtra("selectedBistro", titleStr);
                    startActivity(intent);
                }
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

        // 삭제 추가 레이아웃 초기화
        final Button delbtn = (Button) findViewById(R.id.btn_delete);
        delbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        delbtn.setTextSize(14);
        delbtn.setText("삭제");
        Button abtn = (Button) findViewById(R.id.btn_add);
        abtn.setTextSize(14);
        abtn.setText("추가");

        delbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delbtn.getText().toString() == "삭제") {
                    onChoice = true;
                    delbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    delbtn.setTextSize(14);
                    delbtn.setText("확인");
                    Button addbtn = (Button) findViewById(R.id.btn_add);
                    addbtn.setTextSize(14);
                    addbtn.setText("");

                    //Check whether it works
                    CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
                } else {
                    onChoice = false;
                    delbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                    delbtn.setTextSize(14);
                    delbtn.setText("삭제");
                    Button addbtn = (Button) findViewById(R.id.btn_add);
                    addbtn.setTextSize(14);
                    addbtn.setText("추가");

                    //삭제 확인 AlertDialog
                    showAlertDialog();

                    if (removeItems) {
                        //다중 삭제 처리 동작
                        SparseBooleanArray checkedItems = listview.getCheckedItemPositions();   //삭제할 매장 배열
                        int count = adapter.getCount();
                        for (int i = count - 1; i >= 0; i--) {
                            if (checkedItems.get(i)) { //i position의 상태가 Checked이면
                                // TODO : DELETE /stores/{storeId}로 해당 매장 삭제
                                BistroListViewItem bistro = (BistroListViewItem) adapter.getItem(i);
                                //bistro.getTitle(); //Id로
                            }
                        }
                    }
                    // 모든 선택 상태 초기화.
                    listview.clearChoices();
                    // TODO : 다시 GET /stores
                    adapter.notifyDataSetChanged();
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
        });

        // TODO : mypagebtn 클릭 리스너
    }

    void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제 확인");
        builder.setMessage("선택한 항목(들)을 삭제합니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeItems = true;
                        Toast.makeText(getApplicationContext(), "삭제 완료.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "삭제 취소.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}