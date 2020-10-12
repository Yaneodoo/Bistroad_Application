package com.example.yaneodoo.REST;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

public class GetUserImage extends AsyncTask<String, Void, Bitmap> {
    Bitmap bitmap = null;
    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            bitmap = BitmapFactory.decodeStream(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}