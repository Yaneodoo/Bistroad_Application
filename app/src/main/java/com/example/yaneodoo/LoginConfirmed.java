package com.example.yaneodoo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.Customer.ShowCustomerBistroList;
import com.example.yaneodoo.Owner.ShowOwnerBistroList;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LoginConfirmed extends AppCompatActivity {
    private String token;
    private String loginInfo;
    private String name;
    private String role;
    private int rc;
    private String  url = "https://api.bistroad.kr/v1/users/me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_confirmed);

        SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);
        token = tk.getString("bistrotk","");
        Log.d("TOKEN", token);
        final TextView loginText = (TextView)findViewById(R.id.login_profile_text);
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // Open the connection
//                    URL url = new URL("https://api.bistroad.kr/v1/users/me");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.setRequestProperty("Authorization", "Bearer " + token);
//                    rc = conn.getResponseCode();
//                    Log.e("RC", String.valueOf(rc));
//
//                    if(rc == 200){
//                        InputStream is = conn.getInputStream();
//                        loginInfo = convertStreamToString(is);
//                        //Log.d("POST", loginInfo);
//                        JSONObject jsonLogin = new JSONObject(loginInfo);
//                        name = jsonLogin.getString("fullName");
//                        role = jsonLogin.getString("role");
//                        SharedPreferences tk = getSharedPreferences("sFile", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = tk.edit();
//                        editor.putString("fullName", name); //
//                        editor.putString("role", role); //
//                        editor.commit();
//                        Log.d("name", name);
//                        Log.d("role", role);
//                    }
//                    else{
//                        Log.e("POST", "Failed.");
//                    }
//                }
//                catch (Exception e) {
//                    // Error calling the rest api
//                    Log.e("REST_API: ", "POST method failed: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        });
        RESTApi restApi = new RESTApi(url, token, tk);
        try {
            String rs = restApi.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        name = tk.getString("fullName","");
        role = tk.getString("role","");
        Log.d("name", name);
        Log.d("role", role);

        loginText.setText(name);
        Handler hd = new Handler();

        if(role.equals("ROLE_STORE_OWNER"))
            hd.postDelayed(new confirmedHandlerOwner(), 2000); // 1초 후에 hd handler 실행  3000ms = 3초
        else if(role.equals("ROLE_USER"))
            hd.postDelayed(new confirmedHandlerCustomer(), 2000); // 1초 후에 hd handler 실행  3000ms = 3초

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

    private class confirmedHandlerCustomer implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), ShowCustomerBistroList.class)); //로딩이 끝난 후, ChoiceFunction 이동
            LoginConfirmed.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }

    private class confirmedHandlerOwner implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), ShowOwnerBistroList.class)); //로딩이 끝난 후, ChoiceFunction 이동
            LoginConfirmed.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }

    @Override
    public void onBackPressed() {
        //플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }
}