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

public class RestGetStores extends AsyncTask<Integer, Void, String> {
    // Variable to store url
    protected String sUrl, orderInfo, orderId;
    SharedPreferences tk;
    int rc;

    // Constructor
    public RestGetStores(String sId, SharedPreferences tk) {
        sUrl = "https://api.bistroad.kr/v1/stores/"+sId+"/orders?size=1";
        this.tk = tk;
    }

    // Background work

    @Override
    protected String doInBackground(Integer... params) {
        try {
            // Open the connection
            URL url = new URL(sUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rc = conn.getResponseCode();
            Log.d("RC", String.valueOf(rc));

            if(rc == 200){
                InputStream is = conn.getInputStream();
                orderInfo = convertStreamToString(is);
                int reqLength = orderInfo.length();
                if(reqLength == 3)
                    orderId = "noOrder";
                else {
                    orderInfo = orderInfo.substring(1,orderInfo.length()-2);
                    JSONObject jsonGPS = new JSONObject(orderInfo);
                    orderId = jsonGPS.getString("id");
                }
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