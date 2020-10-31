package com.example.yaneodoo.Owner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Customer.ShowCustomerBistroList;
import com.example.yaneodoo.GPSTracker;
import com.example.yaneodoo.Info.Location;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.PhImageCapture;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetImage;
import com.example.yaneodoo.RetrofitService;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterBistro extends AppCompatActivity implements OnMapReadyCallback {
    private Intent intent;
    private GoogleMap mMap;
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_ALBUM = 2222;
    private ImageView upload_btn;
    private PhImageCapture mCamera;
    private File nFile =null;

    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private Store store=null;

    private Place mPlace;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 3333;

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
        store = (Store) intent.getSerializableExtra("bistroInfo");
        final User owner = (User) intent.getSerializableExtra("ownerInfo");

        GetImage getImage = new GetImage();
        if(owner.getPhoto()!=null){
            Bitmap bitmap = null;
            try {
                bitmap = getImage.execute(owner.getPhoto().getThumbnailUrl()).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            de.hdodenhof.circleimageview.CircleImageView btnMyPage = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.mypagebtn);
            btnMyPage.setImageBitmap(bitmap);
        }

        TextView ownerNameTxtView = (TextView) findViewById(R.id.owner_name_textView);
        ownerNameTxtView.setText(owner.getFullName() + " 점주님");

        if (store != null) {
            TextView bistroExistTxtView = (TextView) findViewById(R.id.bistro_exist_txtView);
            bistroExistTxtView.setText("매장 수정");

            EditText bistroAddressTxtView = (EditText) findViewById(R.id.bistro_address_txtView);
            bistroAddressTxtView.setText(store.getAddress());
            EditText bistroNameTxtView = (EditText) findViewById(R.id.bistro_name_txtView);
            bistroNameTxtView.setText(store.getName());
            EditText bistroTelTxtView = (EditText) findViewById(R.id.bistro_tel_txtView);
            bistroTelTxtView.setText(store.getPhone());
            EditText bistroDescTxtView = (EditText) findViewById(R.id.bistro_desc_txtView);
            bistroDescTxtView.setText(store.getDescription());

            GetImage getStoreImage = new GetImage();
            if(store.getPhoto()!=null){
                try {
                    Bitmap bitmap = getStoreImage.execute(store.getPhoto().getSourceUrl()).get();
                    ImageView bistroImage = (ImageView) findViewById(R.id.bistro_imagebtn);
                    bistroImage.setImageBitmap(bitmap);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // 이미지 업로드 버튼 클릭 리스너
        upload_btn = findViewById(R.id.bistro_imagebtn);
        upload_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPermissionDialog(view);
            }
        });

        // 등록 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_complete);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imgBtn = findViewById(R.id.bistro_imagebtn);
                EditText addressEditTxt = (EditText) findViewById(R.id.bistro_address_txtView);
                EditText nameEditTxt = (EditText) findViewById(R.id.bistro_name_txtView);
                EditText telEditTxt = (EditText) findViewById(R.id.bistro_tel_txtView);
                EditText descEditTxt = (EditText) findViewById(R.id.bistro_desc_txtView);

                if(addressEditTxt.getText().toString().equals("")||nameEditTxt.getText().toString().equals("")
                        || telEditTxt.getText().toString().equals("") || descEditTxt.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "주소, 상호명, 전화번호, 설명의 항목을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
                else if(mPlace==null && store==null){
                    Toast.makeText(getApplicationContext(), "매장을 검색해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    String address=addressEditTxt.getText().toString();
                    String name = nameEditTxt.getText().toString();
                    String phone = telEditTxt.getText().toString();
                    String desc = descEditTxt.getText().toString();

                    Store nStore=new Store();
                    nStore.setOwnerId(owner.getId());
                    nStore.setName(name);
                    nStore.setDescription(desc);
                    if(mPlace==null) nStore.setLocation(store.getLocation());
                    else nStore.setLocation(new Location(String.valueOf(mPlace.getLatLng().latitude), String.valueOf(mPlace.getLatLng().longitude)));
                    nStore.setPhone(phone);
                    nStore.setAddress(address);

                    if(store==null){ //새로운 가게 등록
                        Call<Store> callpostStore = service.postStore("Bearer " + token, nStore);
                        new callpostStore().execute(callpostStore);
                    }
                    else{ //존재하는 가게 수정
                        Call<Store> callpatchStore = service.patchStore("Bearer " + token, nStore, store.getId());
                        new callpatchStore().execute(callpatchStore);
                    }

                    finish();
                }
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

        de.hdodenhof.circleimageview.CircleImageView btnMyPage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterBistro.this, MyPageOwner.class);
                intent.putExtra("ownerInfo", owner);
                RegisterBistro.this.finish();
                startActivity(intent);
            }
        });

        // 구글 맵 프래그먼트를 띄운다
        // SupprotMapFragment 를 통해 레이아웃에 만든 fragment 의 ID 를 참조하고
        // 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // getMapAsync 는 무조건 main Thread 에서 호출되어야한다
        mapFragment.getMapAsync(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mPlace=place;
                Log.i("TAG", "Place: " + mPlace.getName() + ", " + mPlace.getId()+ ", " + mPlace.getLatLng().latitude+ ", "
                        + mPlace.getLatLng().longitude+ ", " + mPlace.getAddress());

                LatLng location = new LatLng(mPlace.getLatLng().latitude, mPlace.getLatLng().longitude);

                // 구글 맵에 표시할 마커에 대한 옵션 설정
                MarkerOptions makerOptions = new MarkerOptions();
                makerOptions
                        .position(location)
                        .title("매장 위치");

                mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

                // 마커를 생성한다.
                mMap.clear();
                mMap.addMarker(makerOptions);

                EditText bistroAddressTxtView = (EditText) findViewById(R.id.bistro_address_txtView);
                bistroAddressTxtView.setText(mPlace.getAddress());

                EditText nameEditTxt = (EditText) findViewById(R.id.bistro_name_txtView);
                nameEditTxt.setText(mPlace.getName());
            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDEgKY8pV-4tQjfrFH85tv4DctMFA9HhkU", Locale.KOREA);
        }
    }

    private class callpostStore extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Store> call = params[0];
                Response<Store> response = call.execute();
                Store body = response.body();
                String storeId=body.getId();

                if (body != null) {
                    Store store = new Store();
                    store.setDescription(body.getDescription());
                    store.setId(body.getId());
                    store.setLocation(body.getLocation());
                    store.setName(body.getName());
                    store.setOwnerId(body.getOwnerId());
                    store.setPhone(body.getPhone());
                    store.setPhoto(body.getPhoto());

                    Log.d("NEW STORE", store.toString());
                    Log.d("postStore end", "======================================");
                } else {
                int statusCode  = response.code();
                Log.d("CODE",Integer.toString(statusCode));
                }
                return storeId;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String storeId) {
            MultipartBody.Part file = null;
            if(nFile!=null){
                try {
                    String mime= Files.probeContentType(nFile.toPath());
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse(mime), nFile);
                    file =
                            MultipartBody.Part.createFormData("file", nFile.getName(), requestFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(file!=null){
                    Call<Store> postStorePhoto = service.postStorePhoto("Bearer " + token, file, storeId);
                    new postStorePhoto().execute(postStorePhoto);
                }
            }
        }
    }

    private class callpatchStore extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Store> call = params[0];
                Response<Store> response = call.execute();
                Store body = response.body();

                if (body != null) {
                    Store store = new Store();
                    store.setDescription(body.getDescription());
                    store.setId(body.getId());
                    store.setLocation(body.getLocation());
                    store.setName(body.getName());
                    store.setOwnerId(body.getOwnerId());
                    store.setPhone(body.getPhone());
                    store.setPhoto(body.getPhoto());

                    Log.d("PATCH STORE", store.toString());
                    Log.d("patchStore end", "======================================");
                } else {
                    int statusCode  = response.code();
                    Log.d("CODE",Integer.toString(statusCode));
                }
                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String result){
            MultipartBody.Part file = null;
            if(nFile!=null){
                try {
                    String mime= Files.probeContentType(nFile.toPath());
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse(mime), nFile);
                    file =
                            MultipartBody.Part.createFormData("file", nFile.getName(), requestFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(file!=null){
                    Call<Store> postStorePhoto = service.postStorePhoto("Bearer " + token, file, store.getId());
                    new postStorePhoto().execute(postStorePhoto);
                }
            }
        }
    }

    private class postStorePhoto extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Store> call = params[0];
                Response<Store> response = call.execute();
                Store body = response.body();

                if (body != null) {
                    Log.d("POSTPHOTO", "success");
                } else {
                    int statusCode  = response.code();
                    Log.d("POSTPHOTO CODE",Integer.toString(statusCode));
                }

                return null;

            } catch (IOException e) {
                Log.d("POSTPHOTO", "fail");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location=null;

        if(store==null){ //새로운 가게 등록 시
            // 현재 위치 설정
            GPSTracker gpsTracker = new GPSTracker(RegisterBistro.this);
            Double lat = gpsTracker.getLatitude();
            Double lon = gpsTracker.getLongitude();
            location = new LatLng(lat, lon);
        }
        else{ //존재하는 가게 수정 시
            location = new LatLng(Double.valueOf(store.getLocation().getLat()), Double.valueOf(store.getLocation().getLng()));

            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions
                    .position(location)
                    .title(store.getName());

            mMap.addMarker(makerOptions);
        }

        //카메라를 적절한 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,17));
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
            case REQUEST_TAKE_ALBUM: //갤러리
                if (resultCode == Activity.RESULT_OK) {

                    if (data.getData() != null) {
                        try {
                            InputStream in = getContentResolver().openInputStream(data.getData());
                            Bitmap img = BitmapFactory.decodeStream(in);
                            in.close();

                            Log.d("BITMAP",img.toString());
                            String imagePath = getRealPathFromURI(data.getData());
                            nFile = new File(imagePath);

                            Log.d("nFile",nFile.toString());
                            // path 경로
                            ExifInterface exif = null;
                            try { exif = new ExifInterface(imagePath);
                            } catch (IOException e) { e.printStackTrace(); }
                            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                            int exifDegree = exifOrientationToDegrees(exifOrientation);

                            img=rotate(img, exifDegree);//원본 이미지

                            if(img.getWidth()>img.getHeight()) img=cropCenterBitmap(img, img.getHeight()*4/3,img.getHeight());//4:3 이미지
                            else img=cropCenterBitmap(img, img.getWidth(),img.getWidth()*3/4);//4:3 이미지

                            upload_btn.setImageBitmap(img);
                        } catch (Exception e) {
                        }
                    } else if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);

                    Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // Handle the error.
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i("TAG", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;

            case 1001: // 카메라
                if (resultCode == Activity.RESULT_OK) {
                    nFile =mCamera.getPhotoFile();
                    mCamera.onResult(upload_btn);
                }
                break;
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0; String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) { // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅 m
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap cropCenterBitmap(Bitmap src, int w, int h) {
        if(src == null)
            return null;

        int width = src.getWidth();
        int height = src.getHeight();

        if(width < w && height < h)
            return src;

        int x = 0;
        int y = 0;

        if(width > w)
            x = (width - w)/2;

        if(height > h)
            y = (height - h)/2;

        int cw = w; // crop width
        int ch = h; // crop height

        if(w > width)
            cw = width;

        if(h > height)
            ch = height;

        return Bitmap.createBitmap(src, x, y, cw, ch);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == 0) {
                Toast.makeText(this, "카메라 권한 승인완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "카메라 권한 승인 거절", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface PhActivityRequest {
        int IMAGE_CAPTURE = 1001;
    }

    private void showPermissionDialog(View view){
        final View view1=view;
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                PopupMenu pop = new PopupMenu(getApplicationContext(), view1);
                getMenuInflater().inflate(R.menu.main_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.camera:
                                final int imageWidth = 200;
                                final int imageHeight = 150;
                                mCamera = new PhImageCapture(imageWidth, imageHeight, "RegisterBistro");
                                mCamera.onStart(RegisterBistro.this);
                                break;
                            case R.id.gallery:
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                startActivityForResult(intent, REQUEST_TAKE_ALBUM);
                                break;
                        }
                        return true;
                    }
                });
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(RegisterBistro.this, "권한 거부", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용해 주세요.")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }
}