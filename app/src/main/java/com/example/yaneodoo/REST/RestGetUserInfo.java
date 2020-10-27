package com.example.yaneodoo.REST;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.yaneodoo.Customer.ShowCustomerBistroList;
import com.example.yaneodoo.Owner.ShowOwnerBistroList;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class RestGetUserInfo extends AsyncTask<Integer, Void, String> {
    // Variable to store url
    protected String mURL, mToken, name, role, loginInfo, id, phone, profileUrl, realname, photo;
    SharedPreferences tk;
    int rc;

    // Constructor
    public RestGetUserInfo(String url, String token, SharedPreferences tk) {
        mURL = url;
        mToken = token;
        this.tk = tk;
    }

    // Background work

    @Override
    protected String doInBackground(Integer... params) {
        try {
            // Open the connection
            URL url = new URL("https://api.bistroad.kr/v1/users/me");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + mToken);
            rc = conn.getResponseCode();
            Log.d("RC", String.valueOf(rc));

            if(rc == 200){
                InputStream is = conn.getInputStream();
                loginInfo = convertStreamToString(is);
                Log.d("POST", loginInfo);
                JSONObject jsonLogin = new JSONObject(loginInfo);
                name = jsonLogin.getString("fullName");
                realname = jsonLogin.getString("username");
                role = jsonLogin.getString("role");
                id = jsonLogin.getString("id");
                phone = jsonLogin.getString("phone");
                //Todo photo객체 가져오기
                //photo = jsonLogin.getString("photo");
                //JSONObject jsonPhoto = new JSONObject(photo);
                //profileUrl = jsonPhoto.getString("sourceUrl");

                SharedPreferences.Editor editor = tk.edit();
                editor.putString("phone", phone);
                editor.putString("realname", realname);
                editor.putString("profileUrl", profileUrl);
                editor.putString("fullName", name); //
                editor.putString("role", role); //
                editor.putString("id", id);
                editor.commit();
            }
            else{
                Log.e("GET", "Failed.");
            }
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API: ", "POST method failed: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String aVoid) {
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

    private String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;

        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}