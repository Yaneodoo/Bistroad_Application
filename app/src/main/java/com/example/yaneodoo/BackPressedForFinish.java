package com.example.yaneodoo;

import android.app.Activity;
import android.widget.Toast;

public class BackPressedForFinish {
    private long backKeyPressedTime = 0;
    private long TIME_INTERVAL = 2000;      // 첫번째 버튼 클릭과 두번째 버튼 클릭 사이의 종료를 위한 시간차
    private Toast toast;
    private Activity activity;

    public BackPressedForFinish(Activity _activity) {
        this.activity = _activity;
    }

    // 종료할 액티비티에서 호출할 함수
    public void onBackPressed() {
        // 마지막 '뒤로'버튼 클릭 시간이 이전 '뒤로'버튼 클릭시간과의 차이가 TIME_INTERVAL(여기서는 2초)보다 클 때
        if (System.currentTimeMillis() > backKeyPressedTime + TIME_INTERVAL) {
            backKeyPressedTime = System.currentTimeMillis();
            showMessage();
        } else {
            // 마지막 '뒤로'버튼 클릭시간이 이전 '뒤로'버튼 클릭시간과의 차이가 TIME_INTERVAL(2초)보다 작을때
            toast.cancel();
            activity.finish();
            toast = Toast.makeText(activity, "이용해 주셔서 감사합니다.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void showMessage() {
        toast = Toast.makeText(activity, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

}