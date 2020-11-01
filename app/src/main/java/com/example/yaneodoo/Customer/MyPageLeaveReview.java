package com.example.yaneodoo.Customer;

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
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yaneodoo.Info.Review4Leave;
import com.example.yaneodoo.InfoEdit;
import com.example.yaneodoo.ListView.LeaveReviewListAdapter;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.yaneodoo.Info.Location;
import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.Request;
import com.example.yaneodoo.Info.Review;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;
import com.example.yaneodoo.ListView.OrderListViewAdapter;
import com.example.yaneodoo.ListView.OrderListViewItem;
import com.example.yaneodoo.Login;
import com.example.yaneodoo.Owner.MyPageOwner;
import com.example.yaneodoo.Owner.RegisterBistro;
import com.example.yaneodoo.Owner.ShowOwnerBistroList;
import com.example.yaneodoo.PhImageCapture;
import com.example.yaneodoo.R;
import com.example.yaneodoo.RetrofitService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyPageLeaveReview extends AppCompatActivity {
    private String token, id, name, itemId, orderId, storeId, storeName, timestamp;
    private Intent intent;
    private Retrofit mRetrofit;
    private RetrofitService service;
    private PhImageCapture mCamera;
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_ALBUM = 2222;
    private ImageButton upload_btn;
    private String baseUrl = "https://api.bistroad.kr/v1/";

    private LeaveReviewListAdapter adapter = new LeaveReviewListAdapter();
    private ListView listview;

    private String aaaa=null;

    private File nFile =null;
    private User user = new User();
    private Order order;
    private Store store;
    private List<Request> requests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_leave_review);
        final SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);
        listview = (ListView) findViewById(R.id.leave_review_list_view);

        token = getSharedPreferences("sFile", MODE_PRIVATE).getString("bistrotk", "");
        name = getSharedPreferences("sFile", MODE_PRIVATE).getString("fullName", "");
        id = getSharedPreferences("sFile", MODE_PRIVATE).getString("id", "");
        final TextView nameText = (TextView)findViewById(R.id.customer_name_textView);
        nameText.setText(name+" 고객님");

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = mRetrofit.create(RetrofitService.class);

        intent = getIntent();
        final Menu menu = (Menu) intent.getSerializableExtra("menuInfo");
        user = (User) intent.getSerializableExtra("userInfo");
        order = (Order) intent.getSerializableExtra("orderInfo");
        store = order.getStore();
        orderId = order.getId();
        timestamp = order.getTimestamp();
        requests = order.getRequests();

        storeId = store.getId();
        storeName = store.getName();
        final TextView storeNameText = (TextView)findViewById(R.id.bistro_name_txtView);
        storeNameText.setText(storeName);

        getRequestsList(requests);

        // 홈 버튼 클릭 리스너
        TextView btnHome = (TextView) findViewById(R.id.homebtn);
        btnHome.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageLeaveReview.this, ShowCustomerBistroList.class);
                MyPageLeaveReview.this.finish();
                startActivity(intent);
            }
        });
        de.hdodenhof.circleimageview.CircleImageView btnMyPage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.mypagebtn);
        btnMyPage.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageLeaveReview.this, MyPageCustomer.class);
                MyPageLeaveReview.this.finish();
                startActivity(intent);
            }
        });

        // 등록 버튼 클릭 리스너
        Button addbtn = (Button) findViewById(R.id.btn_complete);
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Review> reviews = new ArrayList<>();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime date = LocalDateTime.now();
                int count = 0;
                for (int i = 0; i < listview.getCount(); i++){
                    Request request = (Request)listview.getItemAtPosition(i);
                    itemId = request.getMenu().getId();
                    View lView = listview.getChildAt(i);
                    EditText editText = lView.findViewById(R.id.menu_review_editText);
                    RatingBar ratingBar = lView.findViewById(R.id.menu_ratingBar);
                    ImageButton foodImage = lView.findViewById(R.id.upload_btn);

                    Integer stars = Math.round(ratingBar.getRating());
                    String content = editText.getText().toString();
                    if(content.equals(""))
                        break;
                    count++;
                    Review review4Leave = new Review(content, itemId, orderId, stars, storeId, date.toString()+"+09:00", user.getId());

                    String imgPath = (String) foodImage.getTooltipText();
                    review4Leave.setImgPath(imgPath);
                    reviews.add(review4Leave);
                }
                if(count < listview.getCount())
                    Toast.makeText(getApplicationContext(), "리뷰를 모두 작성해 주십시오.", Toast.LENGTH_LONG);
                else{
                    Toast.makeText(getApplicationContext(), "등록중 입니다. 창이 닫힐 때까지 기다려 주십시오.", Toast.LENGTH_LONG);
                    for (int i = 0; i < reviews.size(); i++){
                        Log.d("review",reviews.get(i).toString());
                        Log.d("token", token);
                        aaaa=reviews.get(i).getImgPath();
                        nFile= new File(aaaa);
                        Call<Review> callpostReview = service.postReview("Bearer " + token, reviews.get(i));
                        try {
                            new callpostReview().execute(callpostReview).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getApplicationContext(), "리뷰가 등록 되었습니다.", Toast.LENGTH_LONG);
                    MyPageLeaveReview.this.finish();
                }
            }
        });
    }

    //주문하기 버튼 클릭 리스너
    public void uploadFoodImage(View v) {
        LinearLayout parentRow = (LinearLayout) v.getParent();
        Integer position = Integer.parseInt((String) parentRow.getTag());

        upload_btn=(ImageButton) v;
        showPermissionDialog(v);
    }

    private void getRequestsList(List<Request> requests) {
        for (int i = requests.size()-1; i >= 0; i--) {
            adapter.addItem(requests.get(i));
            Log.d("menu data", "--------------------------------------");
        }
        listview.setAdapter(adapter);
    }

    private class callpostReview extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Review> call = params[0];
                Response<Review> response = call.execute();
                Review body = response.body();
                String storeId=body.getId();

                if (body != null) {
                    Review review = new Review();
                    review.setContents(body.getContents());
                    review.setId(body.getId());
                    review.setItem(body.getItem());
                    review.setStars(body.getStars());
                    review.setWriter(body.getWriter());
                    review.setTimestamp(body.getTimestamp());

                    Log.d("postReview end", "======================================");
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
        protected void onPostExecute(String reviewId) {
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
                    Call<Review> postReviewPhoto = service.postReviewPhoto("Bearer " + token, file, reviewId);
                    try {
                        new postReviewPhoto().execute(postReviewPhoto).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private class postReviewPhoto extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call[] params) {
            try {
                Call<Review> call = params[0];
                Response<Review> response = call.execute();
                Review body = response.body();

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
                                mCamera = new PhImageCapture(imageWidth, imageHeight, "MyPageLeaveReview");
                                mCamera.onStart(MyPageLeaveReview.this);
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
                Toast.makeText(MyPageLeaveReview.this, "권한 거부", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용해 주세요.")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
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

                            upload_btn.setTooltipText(imagePath);

                            Log.d("nFile",nFile.toString());
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

}