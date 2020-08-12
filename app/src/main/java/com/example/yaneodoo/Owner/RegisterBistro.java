package com.example.yaneodoo.Owner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.ListView.BistroListViewAdapter;
import com.example.yaneodoo.ListView.BistroListViewItem;
import com.example.yaneodoo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.ArrayList;

public class RegisterBistro extends AppCompatActivity implements OnMapReadyCallback {
    private Intent intent;
    private static final int REQUEST_CODE = 0;
    private ImageView upload_btn;
    private GoogleMap mMap;

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        LatLng SEOUL = new LatLng(37.56, 126.97);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_registration_owner);

        // ShowOwnerMenuList에서 보낸 titleStr을 받기위해 getIntent()로 초기화
        // 받을게 없으면 어떻게 되지??
        intent = getIntent();
        String bistroName=intent.getStringExtra("selectedBistro");
        //받으면
        // TODO : GET /stores/{storeId}로 이미지, 좌표 값 얻어서 화면에 표시

        // TODO : Google Map으로 좌표값 얻어오기
        // 수정하는 경우는 미리 입력된 좌표값 지도로 나타내기
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 이미지 업로드 버튼 클릭 리스너
        upload_btn = findViewById(R.id.bistro_imagebtn);
        upload_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // 등록 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_complete);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 지도에서 값 가져오기
                ImageView imgBtn = findViewById(R.id.bistro_imagebtn);
                // 지도
                EditText nameEditTxt=(EditText) findViewById(R.id.bistro_name_txtView);
                EditText telEditTxt=(EditText) findViewById(R.id.bistro_tel_txtView);

                Drawable photo=imgBtn.getDrawable();
                // 지도
                String name=nameEditTxt.getText().toString();
                String tel=telEditTxt.getText().toString();

                //BistroInfo bistroInfo=new BistroInfo(photo,,name,tel);
                // TODO : POST /stores로 생성한 bistro등록

                // Adapter 생성
                ArrayList<BistroListViewItem> listViewItemList = new ArrayList<>();
                BistroListViewAdapter adapter = new BistroListViewAdapter(RegisterBistro.this, android.R.layout.simple_list_item_multiple_choice,listViewItemList);

                // 리스트뷰 참조 및 Adapter달기
                ListView listview = (ListView) findViewById(R.id.bistro_list_view_owner);
                listview.setAdapter(adapter);

                // 아이템 추가 예시
                adapter.addItem(ContextCompat.getDrawable(RegisterBistro.this, R.drawable.tteokbokki),"레드 175", "서울시 동작구", "#짜장 #짬뽕") ;

                Intent intent = new Intent(RegisterBistro.this, ShowOwnerBistroList.class);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn) ;
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterBistro.this, ShowOwnerBistroList.class);
                startActivity(intent);
            }
        });

        // TODO : mypagebtn 클릭 리스너

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
                    upload_btn.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
}
