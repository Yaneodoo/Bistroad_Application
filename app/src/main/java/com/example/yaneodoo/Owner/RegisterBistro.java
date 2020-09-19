package com.example.yaneodoo.Owner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Info.Location;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.PhImageCapture;
import com.example.yaneodoo.R;
import com.example.yaneodoo.RetrofitService;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.InputStream;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterBistro extends AppCompatActivity implements OnMapReadyCallback {
    private Intent intent;
    private GoogleMap mMap;
    private static final int REQUEST_TAKE_ALBUM = 1111;
    private ImageView upload_btn;
    private PhImageCapture mCamera;

    private String ownerId;
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bistro_registration_owner);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        intent = getIntent();
        final Store store = (Store) intent.getSerializableExtra("bistroInfo");
        final User owner = (User) intent.getSerializableExtra("ownerInfo");

        TextView ownerNameTxtView = (TextView) findViewById(R.id.owner_name_textView);
        ownerNameTxtView.setText(owner.getFullName() + " 점주님");

        if (store != null) {
            TextView bistroExistTxtView = (TextView) findViewById(R.id.bistro_exist_txtView);
            bistroExistTxtView.setText("매장 수정");

            TextView bistroLocationTxtView = (TextView) findViewById(R.id.bistro_location_txtView);
            bistroLocationTxtView.setText("매장 위치 [lat: " + store.getLocation().getLat() + " lng: " + store.getLocation().getLng() + "]");
            EditText bistroNameTxtView = (EditText) findViewById(R.id.bistro_name_txtView);
            bistroNameTxtView.setText(store.getName());
            EditText bistroTelTxtView = (EditText) findViewById(R.id.bistro_tel_txtView);
            bistroTelTxtView.setText(store.getPhone());
            EditText bistroDescTxtView = (EditText) findViewById(R.id.bistro_desc_txtView);
            bistroDescTxtView.setText(store.getDescription());
        }

        // 이미지 업로드 버튼 클릭 리스너
        upload_btn = findViewById(R.id.bistro_imagebtn);
        upload_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), view);
                getMenuInflater().inflate(R.menu.main_menu, pop.getMenu());

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.camera:
                                final int imageWidth = 150;
                                final int imageHeight = 150;
                                mCamera = new PhImageCapture(imageWidth, imageHeight);
                                mCamera.onStart(RegisterBistro.this);
                                break;
                            case R.id.gallery:
                                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                startActivityForResult(intent, REQUEST_TAKE_ALBUM);
                                break;
                        }
                        return true;
                    }
                });
                pop.show();
                checkPermission();
            }
        });

        // 등록 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_complete);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imgBtn = findViewById(R.id.bistro_imagebtn);
                EditText nameEditTxt = (EditText) findViewById(R.id.bistro_name_txtView);
                EditText telEditTxt = (EditText) findViewById(R.id.bistro_tel_txtView);
                EditText descEditTxt = (EditText) findViewById(R.id.bistro_desc_txtView);

                Drawable uploadedImg = imgBtn.getDrawable();
                String name = nameEditTxt.getText().toString();
                String tel = telEditTxt.getText().toString();
                String desc = descEditTxt.getText().toString();

                //store.setPhotoUri(photo);
                // TODO : 지도에서 값 가져오기

                // TODO : POST /stores로 생성한 bistro등록
                getUserMe(token);
                postStore(token, new Store(desc, new Location("12", "12"), name, ownerId, tel));

                Intent intent = new Intent(RegisterBistro.this, ShowOwnerBistroList.class);
                RegisterBistro.this.finish();
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterBistro.this, ShowOwnerBistroList.class);
                RegisterBistro.this.finish();
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO : Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO : Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        ImageButton btnMyPage = (ImageButton) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterBistro.this, MyPageOwner.class);
                RegisterBistro.this.finish();
                startActivity(intent);
            }
        });
    }

    private void getUserMe(String token) {
        service.getUserMe("Bearer " + token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User body = response.body();
                    if (body != null) {
                        ownerId = body.getId();
                        Log.d("user.getId()", body.getId());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void postStore(String token, Store store) {
        service.postStore("Bearer " + token, store).enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                if (response.isSuccessful()) {
                    Store body = response.body();
                    if (body != null) {
                        Log.d("data.getId()", body.getId());
                        Log.d("data.getOwnerId()", body.getOwnerId());
                        Log.d("data.getName()", body.getName());
                        Log.d("data.getPhone()", body.getPhone());
                        Log.d("data.getDescription()", body.getDescription());
                        Log.d("data.getLocation()", body.getLocation().getLat() + body.getLocation().getLng());
                        Log.d("postStore end", "======================================");
                    }
                }
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == 1) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_ALBUM:
                if (resultCode == Activity.RESULT_OK) {

                    if (data.getData() != null) {
                        try {
                            InputStream in = getContentResolver().openInputStream(data.getData());

                            Bitmap img = BitmapFactory.decodeStream(in);
                            in.close();

                            upload_btn.setImageBitmap(img);
                        } catch (Exception e) {
                        }
                    } else if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                if (resultCode == Activity.RESULT_OK) {
                    // Camera action pick 결과 전달
                    mCamera.onResult(upload_btn);
                }
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
                new AlertDialog.Builder(this).setTitle("알림").setMessage("저장소 권한이 거부되었습니다. \n앱을 재실행하여 뜨는 팝업을 통해 권한을 허용하거나, 앱 설정에서 권한을 허용해주세요.").setNeutralButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package: " + getPackageName()));
                        startActivity(intent);
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setCancelable(false).create().show();
            }
        }
    }

    public interface PhActivityRequest {
        int IMAGE_CAPTURE = 1001;
    }
}