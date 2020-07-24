package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Customer.ShowCustomerBistroList;
import com.example.yaneodoo.R;

public class RegisterMenu extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_registration_owner);

        // ShowOwnerMenuList에서 보낸 titleStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        String bistroName=intent.getStringExtra("selectedBistro");

        // TODO : GET /stores/{storeId}/items/{itemId}로 가져온 정보를 화면에 표시
        EditText mname_txtView =(EditText) findViewById(R.id.menu_name_txtView);
        EditText mprice_txtView =(EditText) findViewById(R.id.menu_price_txtView);
        EditText mdesc_txtView =(EditText) findViewById(R.id.menu_desc_txtView);
        //mname_txtView.setText();
        //mprice_txtView.setText();
        //mdesc_txtView.setText();

        // 등록 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_complete);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 이미 있는 품목이면 수정, 없는 품목이면 등록
                // PUT /stores/{storeId}/items/{itemId}
                // POST /stores/{storeId}/items

                Intent intent = new Intent(RegisterMenu.this, ShowOwnerMenuList.class);
                startActivity(intent);
            }
        });

        // 업로드 버튼 클릭 리스너
        ImageButton upload_btn = (ImageButton) findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 갤러리 또는 카메라 열어서 이미지 업로드
            }
        });

        // 홈 버튼 클릭 리스너
        Button btnHome = (Button) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterMenu.this, ShowOwnerBistroList.class);
                startActivity(intent);
            }
        }) ;

        // TODO : mypagebtn 클릭 리스너
    }

}
