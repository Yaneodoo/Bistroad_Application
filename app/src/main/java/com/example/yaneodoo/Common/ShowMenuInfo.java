package com.example.yaneodoo.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Customer.ShowCustomerMenuList;
import com.example.yaneodoo.ListView.MenuListViewAdapter;
import com.example.yaneodoo.ListView.MenuListViewItem;
import com.example.yaneodoo.Owner.RegisterBistro;
import com.example.yaneodoo.Owner.RegisterMenu;
import com.example.yaneodoo.R;

import java.security.acl.Owner;

public class ShowMenuInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_info);

        // TODO : owner면 동적으로 edit버튼 생성하고 받은 입력 처리
        RelativeLayout topLL = (RelativeLayout)findViewById(R.id.dynamicArea);
        TextView tv = new TextView(this);
        tv.setText("떡볶이");
        tv.setTextSize(26);
        tv.setId(R.id.menu_name_txtView);
        topLL.addView(tv);

        RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        Button editbtn = new Button(this);
        editbtn.setText("START");
        buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        editbtn.setLayoutParams(buttonLayoutParams);
        topLL.addView(editbtn);

        // edit 버튼 클릭 리스너
        editbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShowMenuInfo.this, RegisterMenu.class);
                // TODO : 알고 있는 Menu의 정보 전달
                //intent.putExtra("selectedMenu", titleStr);
                startActivity(intent);
            }
        });
    }
}