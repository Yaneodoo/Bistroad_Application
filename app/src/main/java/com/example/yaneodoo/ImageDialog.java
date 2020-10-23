package com.example.yaneodoo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.REST.GetImage;

import java.util.concurrent.ExecutionException;

public class ImageDialog extends Activity {

    private ImageView mDialog;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_dialog);

        Intent intent=getIntent();
        Menu menu=(Menu)intent.getSerializableExtra("menuInfo");

        GetImage getImage = new GetImage();
        try {
            bitmap = getImage.execute(menu.getPhoto().getSourceUrl()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mDialog = (ImageView)findViewById(R.id.popup_image);
        mDialog.setImageBitmap(bitmap);
        mDialog.setClickable(true);

        mDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
