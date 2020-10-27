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
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
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

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.PhImageCapture;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetImage;
import com.example.yaneodoo.RetrofitService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterMenu extends AppCompatActivity {
    private Intent intent;
    private String token;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_ALBUM = 2222;
    private ImageView upload_btn;
    private static final int REQUEST_CODE = 0;
    private PhImageCapture mCamera;
    private File nFile =null;

    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_registration_owner);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        intent = getIntent();
        store = (Store) intent.getSerializableExtra("bistroInfo");
        final Menu menu = (Menu) intent.getSerializableExtra("menuInfo");
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

        TextView bistroNameTxtView = (TextView) findViewById(R.id.bistro_name_txtView);
        bistroNameTxtView.setText(store.getName());
        TextView bistroLocationTxtView = (TextView) findViewById(R.id.bistro_location_txtView);
        bistroLocationTxtView.setText("매장 주소 : "+store.getAddress());

        if (menu != null) {
            EditText menuNameTxtView = (EditText) findViewById(R.id.menu_name_txtView);
            menuNameTxtView.setText(menu.getName());
            EditText menuPriceTxtView = (EditText) findViewById(R.id.menu_price_txtView);
            menuPriceTxtView.setText(menu.getPrice().toString().substring(0,menu.getPrice().toString().length()-1) + "원");
            EditText meuDescTxtView = (EditText) findViewById(R.id.menu_desc_txtView);
            meuDescTxtView.setText(menu.getDescription());

            GetImage getMenuImage = new GetImage();
            if(menu.getPhoto()!=null){
                try {
                    Bitmap bitmap = getMenuImage.execute(menu.getPhoto().getSourceUrl()).get();
                    ImageView menuImage = (ImageView) findViewById(R.id.upload_btn);
                    menuImage.setImageBitmap(bitmap);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // 등록 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_complete);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameEditTxt = (EditText) findViewById(R.id.menu_name_txtView);
                EditText priceEditTxt = (EditText) findViewById(R.id.menu_price_txtView);
                EditText descEditTxt = (EditText) findViewById(R.id.menu_desc_txtView);

                if(nameEditTxt.getText().toString().equals("") || priceEditTxt.getText().toString().equals("") || descEditTxt.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "항목을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
                else {
                    String name = nameEditTxt.getText().toString();
                    String price = priceEditTxt.getText().toString();
                    String desc = descEditTxt.getText().toString();

                    Menu nMenu=new Menu();
                    nMenu.setDescription(desc);
                    nMenu.setName(name);
                    nMenu.setPrice(Integer.valueOf(price));

                    if (menu == null) { //새로운 메뉴 등록
                        Call<Menu> callpostMenu = service.postMenu("Bearer " + token, nMenu, store.getId());
                        try {
                            new postMenu().execute(callpostMenu).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{ //기존 메뉴 수정
                        Call<Menu> callpatchMenu = service.patchMenu("Bearer " + token, nMenu, store.getId(), menu.getId());
                        try {
                            new patchMenu().execute(callpatchMenu).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    finish();
                }
            }
        });

        // 이미지 업로드 버튼 클릭 리스너
        upload_btn = findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), view);
                getMenuInflater().inflate(R.menu.main_menu, pop.getMenu());

                pop.show();
                checkPermission();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.camera:
                                final int imageWidth = 200;
                                final int imageHeight = 150;
                                mCamera = new PhImageCapture(imageWidth, imageHeight, "RegisterMenu");
                                mCamera.onStart(RegisterMenu.this);
                                Log.d("UPLOAD", mCamera.toString());
                                break;
                            case R.id.gallery:
                                Log.d("UPLOAD","GALLERY");
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

        de.hdodenhof.circleimageview.CircleImageView btnMyPage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterMenu.this, MyPageOwner.class);
                intent.putExtra("ownerInfo", owner);
                RegisterMenu.this.finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
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

    private class postMenu extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Menu> call = params[0];
                Response<Menu> response = call.execute();
                Menu body = response.body();
                String menuId=body.getId();
                Log.d("POST MENU",body.getName());

                return menuId;

            } catch (IOException e) {
                Log.d("POSTMENU", "fail");
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String menuId) {
            MultipartBody.Part file = null;
            if(nFile!=null){
                try {
                    String mime=Files.probeContentType(nFile.toPath());
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse(mime), nFile);
                    file =
                            MultipartBody.Part.createFormData("file", nFile.getName(), requestFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(file!=null){
                    Call<Menu> postItemPhoto = service.postMenuPhoto("Bearer " + token, file, store.getId(), menuId);
                    new postItemPhoto().execute(postItemPhoto);
                }
            }
        }
    }

    private class patchMenu extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Menu> call = params[0];
                Response<Menu> response = call.execute();
                Menu body = response.body();
                String menuId=body.getId();
                Log.d("PATCH MENU",body.getName());

                return menuId;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String menuId) {
            MultipartBody.Part file = null;
            if(nFile!=null){
                try {
                    String mime=Files.probeContentType(nFile.toPath());
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse(mime), nFile);
                    file =
                            MultipartBody.Part.createFormData("file", nFile.getName(), requestFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(file!=null){
                    Call<Menu> postItemPhoto = service.postMenuPhoto("Bearer " + token, file, store.getId(), menuId);
                    new postItemPhoto().execute(postItemPhoto);
                }
            }
        }
    }

    private class postItemPhoto extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Menu> call = params[0];
                Response<Menu> response = call.execute();
                Menu body = response.body();

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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            default:
                if (resultCode == Activity.RESULT_OK) {
                    // Camera action pick 결과 전달
                    nFile =mCamera.getPhotoFile();
                    mCamera.onResult(upload_btn);
                }
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

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
                new AlertDialog.Builder(this).setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. \n앱을 재실행하여 뜨는 팝업을 통해 권한을 허용하거나, 앱 설정에서 권한을 허용해주세요.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
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
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }
        }
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
}
