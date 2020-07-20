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

public class RegisterBistro extends Activity implements OnMapReadyCallback{

    private GoogleMap mMap;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_registration_owner);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        @Override
        public void onMapReady(final GoogleMap googleMap) {
            mMap = googleMap;

            LatLng SEOUL = new LatLng(37.56, 126.97);

            // 마커 예시 코드
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(SEOUL);
            markerOptions.title("서울");
            markerOptions.snippet("한국의 수도");
            mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }

        // 등록 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_complete);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageButton imgBtn=(ImageButton) findViewById(R.id.menu_image);
                // 지도
                EditText nameEditTxt=(EditText) findViewById(R.id.bistro_name_txtView);
                EditText telEditTxt=(EditText) findViewById(R.id.bistro_tel_txtView);

                // TODO : bistro 생성
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
