package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.R;

public class RegisterMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_registration_owner);

        // 등록 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_complete);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 입력한 정보 저장

                Intent intent = new Intent(RegisterMenu.this, ShowOwnerMenuList.class);
                startActivity(intent);
            }
        });

        // 업로드 버튼 클릭 리스너
        ImageButton upload_btn = (ImageButton) findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 갤러리 또는 카메라 열기

            }
        });
    }

}
