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

public class RestGetNearestStore extends AsyncTask<Integer, Void, String> {
    // Variable to store url
    protected String mToken, sName, sUrl, sLat, sLon, storeInfo;
    //protected float sLat, sLon;
    int rc;

    // Constructor
    public RestGetNearestStore(float lat, float lon) {
        sLat = String.valueOf(lat);
        sLon = String.valueOf(lon);
    }

    // Background work

    @Override
    protected String doInBackground(Integer... params) {
        try {
            sUrl = "https://api.bistroad.kr/v1/stores/nearby?originLat="+sLat+"&originLng="+sLon+"&radius=1&size=1";
            Log.d("sUrl", sUrl);
            // Open the connection
            URL url = new URL(sUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rc = conn.getResponseCode();
            Log.d("RC", String.valueOf(rc));

            if(rc == 200){
                InputStream is = conn.getInputStream();
                storeInfo = convertStreamToString(is);
                int reqLength = storeInfo.length();
                if(reqLength == 3)
                    sName = "noStore";
                else {
                    storeInfo = storeInfo.substring(1,storeInfo.length()-2);
                    JSONObject jsonGPS = new JSONObject(storeInfo);
                    sName = jsonGPS.getString("name");
                }
            }
            else{
                Log.e("GET", "Failed.");
            }
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API: ", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }
        return sName;
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