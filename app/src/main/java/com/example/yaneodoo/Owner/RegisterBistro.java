package com.example.yaneodoo.Owner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.R;

public class RegisterBistro extends Activity {
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_registration_owner);

        // 등록 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_complete);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO : 입력한 정보 DB에 추가

                ImageButton imgBtn=(ImageButton) findViewById(R.id.menu_image);
                // 지도
                EditText nameEditTxt=(EditText) findViewById(R.id.bistro_name_txtView);
                EditText telEditTxt=(EditText) findViewById(R.id.bistro_tel_txtView);
                // TODO : 갤러리로부터 가게 대표 이미지, 지도로부터 매장 위치 가져와서 bistro 생성
                Drawable photo=imgBtn.getDrawable();
                // 지도
                String name=nameEditTxt.getText().toString();
                String tel=telEditTxt.getText().toString();

                //BistroInfo bistroInfo=new BistroInfo(photo,,name,tel);


                // Adapter 생성
                BistroListViewAdapter adapter = new BistroListViewAdapter() ;
                // 리스트뷰 참조 및 Adapter달기
                ListView listview = (ListView) findViewById(R.id.bistro_list_view_owner);
                listview.setAdapter(adapter);

                // 첫 번째 아이템 추가.
                adapter.addItem(ContextCompat.getDrawable(RegisterBistro.this, R.drawable.tteokbokki),"레드 175", "서울시 동작구", "#짜장 #짬뽕") ;

                Intent intent = new Intent(RegisterBistro.this, ShowOwnerBistroList.class);
                startActivity(intent);
            }
        });
    }
}
