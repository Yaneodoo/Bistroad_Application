package com.example.yaneodoo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.yaneodoo.Customer.MyPageLeaveReview;
import com.example.yaneodoo.Owner.RegisterBistro;
import com.example.yaneodoo.Owner.RegisterMenu;

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
    private File photoFile = null;

    private String usedActivity;

    public PhImageCapture(int a_reqWidth, int a_reqHeight, String usedActivity) {
        mReqWidth = a_reqWidth;
        mReqHeight = a_reqHeight;
        this.usedActivity = usedActivity;
    }

    public File getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(File photoFile) {
        this.photoFile = photoFile;
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

        File photoPath = a_activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            photoFile = File.createTempFile(PhUtil.getTempFileName(), ".png", photoPath);
            setPhotoFile(photoFile);
            mStrPhotoPath = photoFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO : photoFile 생성됨

        /**
         * File provider 를 이용 파일 공유
         * Android N 부터 file URI 를 이용할 경우 FileUriExposedException 이 발생한다.
         */
        Uri contentUri = FileProvider.getUriForFile(a_activity, BuildConfig.APPLICATION_ID + ".provider", photoFile);
        Log.d("URI", contentUri.toString());

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

        if (usedActivity.equals("RegisterMenu"))
            a_activity.startActivityForResult(pickIntent, RegisterMenu.PhActivityRequest.IMAGE_CAPTURE);
        else if(usedActivity.equals("RegisterBistro"))
            a_activity.startActivityForResult(pickIntent, RegisterBistro.PhActivityRequest.IMAGE_CAPTURE);
        else
            a_activity.startActivityForResult(pickIntent, MyPageLeaveReview.PhActivityRequest.IMAGE_CAPTURE);
    }

    /**
     * 카메라로 찍은 image 를 image view 에 설정
     */
    public void onResult(ImageView a_ivPhoto) {
        try {
            // Resize bitmap for memory
            Bitmap bitmap = PhUtil.decodeSampledBitmapFromFile(mStrPhotoPath, mReqWidth, mReqHeight);
            bitmap = Bitmap.createScaledBitmap(bitmap, mReqWidth, mReqHeight, true);

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(mStrPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int exifOrientation;
            int exifDegree;
            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else { exifDegree = 0; }

            bitmap=rotate(bitmap, exifDegree); //회전각도 적용

            a_ivPhoto.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
