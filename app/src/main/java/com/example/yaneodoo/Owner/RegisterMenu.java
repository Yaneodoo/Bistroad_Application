package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.R;

import java.io.InputStream;

public class RegisterMenu extends AppCompatActivity {
    private Intent intent;
    private static final int REQUEST_CODE = 0;
    private ImageView uploadbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_registration_owner);

        // ShowOwnerMenuList에서 보낸 titleStr을 받기위해 getIntent()로 초기화
        intent = getIntent();
        String bistroName = intent.getStringExtra("selectedBistro");

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
                RegisterMenu.this.finish();
                startActivity(intent);
            }
        });

        // 이미지 업로드 버튼 클릭 리스너
        uploadbtn = findViewById(R.id.upload_btn);
        uploadbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterMenu.this, ShowOwnerBistroList.class);
                RegisterMenu.this.finish();
                startActivity(intent);
            }
        });

        ImageButton btnMyPage = (ImageButton) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterMenu.this, MyPageOwner.class);
                RegisterMenu.this.finish();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    uploadbtn.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

}
