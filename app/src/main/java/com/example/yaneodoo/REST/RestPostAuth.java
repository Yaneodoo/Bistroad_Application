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

public class RestPostAuth extends AsyncTask<Integer, Void, Integer> {
    // Variable to store url
    protected String mId, mPwd, name, loginInfo;
    SharedPreferences tk;
    int rc;

    // Constructor
    public RestPostAuth(String id, String pwd, SharedPreferences tk) {
        mId = id;
        mPwd = pwd;
        this.tk = tk;
    }

    // Background work

    @Override
    protected Integer doInBackground(Integer... params) {
        try {
            // Open the connection
            URL url = new URL("https://api.bistroad.kr/v1/auth/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            JSONObject jsonInfo = new JSONObject();
            jsonInfo.accumulate("username", mId);
            jsonInfo.accumulate("password", mPwd);

            String userInfo = jsonInfo.toString();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");

            conn.setRequestMethod("POST");
            conn.setDefaultUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            os.write(userInfo.getBytes("euc-kr"));

            os.flush();
            rc = conn.getResponseCode();
            Log.d("GetAuth",String.valueOf(rc));

            if(rc == 200){
                InputStream is = conn.getInputStream();
                loginInfo = convertStreamToString(is);
                JSONObject jsonLogin = new JSONObject(loginInfo);
                String token = jsonLogin.getString("access_token");
                SharedPreferences.Editor editor = tk.edit();
                editor.putString("bistrotk", token);
                editor.putString("bId",mId);
                editor.putString("bPwd",mPwd);
                editor.commit();
            }
            else if(rc == 404 || rc == 401){

            }
            else{
                Log.e("POST", "Failed.");
            }
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API: ", "POST method failed: " + e.getMessage());
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