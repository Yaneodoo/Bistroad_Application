package com.example.yaneodoo.REST;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestGetUser extends AsyncTask<Integer, Void, String> {
    // Variable to store url
    protected String mid, mToken, userInfo, name;
    SharedPreferences tk;
    int rc;

    // Constructor
    public RestGetUser(String uid, String token) {
        mid = uid;
        mToken=token;
    }

    // Background work

    @Override
    protected String doInBackground(Integer... params) {
        try {
            // Open the connection
            URL url = new URL("https://api.bistroad.kr/v1/users/"+mid);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + mToken);
            rc = conn.getResponseCode();
            Log.d("RC", String.valueOf(rc));

            if(rc == 200){
                InputStream is = conn.getInputStream();
                userInfo = convertStreamToString(is);
                //Log.d("POST", loginInfo);
                JSONObject jsonLogin = new JSONObject(userInfo);
                name = jsonLogin.getString("fullName");
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
        return name;
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