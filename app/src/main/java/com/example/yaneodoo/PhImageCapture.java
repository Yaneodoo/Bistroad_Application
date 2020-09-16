package com.example.yaneodoo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.yaneodoo.Owner.RegisterBistro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PhImageCapture extends AppCompatActivity {
    // Resized image size
    private int mReqWidth;
    private int mReqHeight;

    // Photo path
    private String mStrPhotoPath;

    public PhImageCapture(int a_reqWidth, int a_reqHeight) {
        mReqWidth = a_reqWidth;
        mReqHeight = a_reqHeight;
    }

    /**
     * 카메라 호출 action pick 실행
     */
    public void onStart(Activity a_activity) {
        final boolean isValidImageCapture = PhUtil.isValidIntent(a_activity, MediaStore.ACTION_IMAGE_CAPTURE);
        if (isValidImageCapture == false) {
            return;
        }

        // 사진 파일
        File photoFile = null;
        File photoPath = a_activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            photoFile = File.createTempFile(PhUtil.getTempFileName(), ".png", photoPath);
            mStrPhotoPath = photoFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * File provider 를 이용 파일 공유
         * Android N 부터 file URI 를 이용할 경우 FileUriExposedException 이 발생한다.
         */
        Uri contentUri = FileProvider.getUriForFile(a_activity, BuildConfig.APPLICATION_ID + ".provider", photoFile);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        a_activity.sendBroadcast(mediaScanIntent);

        // Camera 사용을 위해 ACTION_IMAGE_CAPTURE 전달
        Intent pickIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pickIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

        // For legacy version
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            List<ResolveInfo> resInfoList = a_activity.getPackageManager().queryIntentActivities(pickIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                a_activity.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }

        a_activity.startActivityForResult(pickIntent, RegisterBistro.PhActivityRequest.IMAGE_CAPTURE);
    }

    /**
     * 카메라로 찍은 image 를 image view 에 설정
     */
    public void onResult(ImageView a_ivPhoto) {
        try {
            // Resize bitmap for memory
            Bitmap bitmap = PhUtil.decodeSampledBitmapFromFile(mStrPhotoPath, mReqWidth, mReqHeight);
            bitmap = Bitmap.createScaledBitmap(bitmap, mReqWidth, mReqHeight, true);
            a_ivPhoto.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
