package com.example.yaneodoo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.Owner.RegisterBistro;
import com.example.yaneodoo.REST.GetImage;
import com.example.yaneodoo.REST.RestPatchUser;
import com.example.yaneodoo.REST.RestPostUser;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoEdit extends AppCompatActivity {
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_ALBUM = 2222;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 3333;
    String role, token, userId, realname, phoneNum, idName, curPwd;
    int rc;
    private de.hdodenhof.circleimageview.CircleImageView upload_btn;
    private PhImageCapture mCamera;
    private String profileUrl;
    private File nFile =null;

    private Retrofit mRetrofit;
    private RetrofitService service;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_edit);
        final TextView id = (TextView)findViewById(R.id.info_id_text);
        final EditText pw = (EditText)findViewById(R.id.info_password_textinput);
        final EditText confirmPw = (EditText)findViewById(R.id.info_password_confirm_textinput);
        final TextView name = (TextView)findViewById(R.id.info_name_text);
        final EditText phone = (EditText)findViewById(R.id.info_mobile_number_textinput);
        final EditText curPw = (EditText)findViewById(R.id.info_current_password_textinput);

        final SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);
        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");
        idName = getSharedPreferences("sFile", MODE_PRIVATE).getString("fullName", "");
        realname = getSharedPreferences("sFile", MODE_PRIVATE).getString("realname", "");
        phoneNum = getSharedPreferences("sFile", MODE_PRIVATE).getString("phone", "");
        userId = getSharedPreferences("sFile", MODE_PRIVATE).getString("id", "");
        curPwd = getSharedPreferences("sFile", MODE_PRIVATE).getString("bPwd", "");
        role = getSharedPreferences("sFile", MODE_PRIVATE).getString("role", "");

        id.setText(realname);
        name.setText(idName);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        id.setText(idName);
        name.setText(realname);
        phone.setText(phoneNum);

        // 이미지 업로드 버튼 클릭 리스너
        upload_btn = findViewById(R.id.profile_image);
        upload_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPermissionDialog(view);
            }
        });
        profileUrl = tk.getString("profileUrl","noProfile");
        if(profileUrl.equals("noProfile")){

        }
        else {
            GetImage getStoreImage = new GetImage();
            try {
                Bitmap sbitmap = getStoreImage.execute(profileUrl).get();
                de.hdodenhof.circleimageview.CircleImageView bistroRepresentImage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);
                bistroRepresentImage.setImageBitmap(sbitmap);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 정보수정 버튼 클릭 리스너
        Button btnSignup = (Button) findViewById(R.id.info_edit_button);
        btnSignup.setOnClickListener(new TextView.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(!curPw.getText().toString().equals(curPwd)){
                    Toast noIdToast = Toast.makeText(getApplicationContext(), "현재비밀번호가 틀렸습니다.", Toast.LENGTH_LONG);
                    noIdToast.show();
                }
                else if(pw.getText().toString().length() == 0 && confirmPw.getText().toString().length() == 0){
                    try {
                        RestPatchUser restPostUser = new RestPatchUser(id.getText().toString(), curPw.getText().toString(), name.getText().toString(), role, phone.getText().toString(), userId, token);
                        try {
                            rc = restPostUser.execute().get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("Login rc", String.valueOf(rc));
                        if (rc == 200 || rc == 201) {
                            String signupSuccess = "성공적으로 정보가 변경되었습니다.";
                            Toast successToast = Toast.makeText(getApplicationContext(), signupSuccess, Toast.LENGTH_LONG);
                            successToast.show();
                            SharedPreferences.Editor editor = tk.edit();
                            editor.putString("bistrotk", "");
                            editor.commit();
                            Intent intent = new Intent(InfoEdit.this, Login.class);
                            startActivity(intent);
                            InfoEdit.this.finish();
                        } else if (rc == 409) {
                            Toast sameIdToast = Toast.makeText(getApplicationContext(), "동일한 아이디가 이미 존재합니다.", Toast.LENGTH_LONG);
                            sameIdToast.show();
                        } else if (rc == 403) {
                            Toast errorToast = Toast.makeText(getApplicationContext(), "Sign Up 403 error", Toast.LENGTH_LONG);
                            errorToast.show();
                        } else if (rc == 404) {
                            Toast errorToast = Toast.makeText(getApplicationContext(), "Sign Up 401 error.", Toast.LENGTH_LONG);
                            errorToast.show();
                        } else {
                            Log.e("POST", "Failed.");
                        }
                    } catch (Exception e) {
                        // Error calling the rest api
                        Log.e("REST_API", "POST method failed: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                else if(pw.getText().toString().length() == 0){
                    Toast noIdToast = Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요.", Toast.LENGTH_LONG);
                    noIdToast.show();
                }
                else if(confirmPw.getText().toString().length() == 0){
                    Toast noIdToast = Toast.makeText(getApplicationContext(), "확인 비밀번호를 입력해 주세요.", Toast.LENGTH_LONG);
                    noIdToast.show();
                }
                else if(!pw.getText().toString().equals(confirmPw.getText().toString())){
                    Toast noIdToast = Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG);
                    noIdToast.show();
                }
                else if(phone.getText().toString().length() == 0){
                    Toast noIdToast = Toast.makeText(getApplicationContext(), "전화번호를 입력해 주세요.", Toast.LENGTH_LONG);
                    noIdToast.show();
                }
                else {
                    try {
                        RestPatchUser restPostUser = new RestPatchUser(id.getText().toString(), pw.getText().toString(), name.getText().toString(), role, phone.getText().toString(), userId, token);
                        try {
                            rc = restPostUser.execute().get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("Login rc", String.valueOf(rc));
                        if (rc == 200 || rc == 201) {

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
                                    Call<User> postUserPhoto = service.postUserPhoto("Bearer " + token, file, userId);
                                    new postUserPhoto().execute(postUserPhoto);
                                }
                            }

                            String signupSuccess = "성공적으로 정보가 변경되었습니다.";
                            Toast successToast = Toast.makeText(getApplicationContext(), signupSuccess, Toast.LENGTH_LONG);
                            successToast.show();
                            SharedPreferences.Editor editor = tk.edit();
                            editor.putString("bistrotk", "");
                            editor.commit();
                            Intent intent = new Intent(InfoEdit.this, Login.class);
                            startActivity(intent);
                            InfoEdit.this.finish();
                        } else if (rc == 409) {
                            Toast sameIdToast = Toast.makeText(getApplicationContext(), "동일한 아이디가 이미 존재합니다.", Toast.LENGTH_LONG);
                            sameIdToast.show();
                        } else if (rc == 403) {
                            Toast errorToast = Toast.makeText(getApplicationContext(), "Sign Up 403 error", Toast.LENGTH_LONG);
                            errorToast.show();
                        } else if (rc == 404) {
                            Toast errorToast = Toast.makeText(getApplicationContext(), "Sign Up 401 error.", Toast.LENGTH_LONG);
                            errorToast.show();
                        } else {
                            Log.e("POST", "Failed.");
                        }
                    } catch (Exception e) {
                        // Error calling the rest api
                        Log.e("REST_API", "POST method failed: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
        Button btnBack = (Button) findViewById(R.id.info_back_button);
        btnBack.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoEdit.this.finish();
            }
        });
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
                            // path 경로
                            ExifInterface exif = null;
                            try { exif = new ExifInterface(imagePath);
                            } catch (IOException e) { e.printStackTrace(); }
                            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                            int exifDegree = exifOrientationToDegrees(exifOrientation);

                            img=rotate(img, exifDegree);//원본 이미지

                            upload_btn.setImageBitmap(img);
                        } catch (Exception e) {
                        }
                    } else if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case 1001: // 카메라
                if (resultCode == Activity.RESULT_OK) {
                    // Camera action pick 결과 전달
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

    private class postUserPhoto extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<User> call = params[0];
                Response<User> response = call.execute();
                User body = response.body();

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
                                final int imageHeight = 200;
                                mCamera = new PhImageCapture(imageWidth, imageHeight, "InfoEdit");
                                mCamera.onStart(InfoEdit.this);
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
                Toast.makeText(InfoEdit.this, "권한 거부", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용해 주세요.")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }
}
