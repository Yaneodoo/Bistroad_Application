package com.example.yaneodoo.REST;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestPostUser extends AsyncTask<Integer, Void, Integer> {
    // Variable to store url
    protected String mUsername, mRole, mPhone, mId, mPwd;
    int rc;

    // Constructor
    public RestPostUser(String id, String pwd, String username, String role, String phone) {
        mId = id;
        mPwd = pwd;
        mUsername = username;
        mRole = role;
        mPhone = phone;
    }

    // Background work

    @Override
    protected Integer doInBackground(Integer... params) {
        try {
            // Open the connection
            URL url = new URL("https://api.bistroad.kr/v1/users");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            JSONObject jsonInfo = new JSONObject();
            jsonInfo.accumulate("username", mId);
            jsonInfo.accumulate("password", mPwd);
            jsonInfo.accumulate("fullName", mUsername);
            jsonInfo.accumulate("phone", mPhone);
            jsonInfo.accumulate("role", mRole);

            String userInfo = jsonInfo.toString();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");

            conn.setRequestMethod("POST");
            conn.setDefaultUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            os.write(userInfo.getBytes("UTF-8"));

            os.flush();
            rc = conn.getResponseCode();
            Log.d("PostUser",String.valueOf(rc));
            if(rc == 201 || rc == 409 || rc == 403 || rc == 404 || rc == 401 ){
//                InputStream is = conn.getInputStream();
//                Log.d("POST", convertStreamToString(is));
            }
            else{
                Log.e("POST", "Failed.");
            }
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API", "POST method failed: " + e.getMessage());
            e.printStackTrace();
        }
        return rc;
    }

    @Override
    protected void onPostExecute(Integer aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}