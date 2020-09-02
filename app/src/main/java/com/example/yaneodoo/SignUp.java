package com.example.yaneodoo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUp extends AppCompatActivity {
    String result = null;
    String userInfo;
    int rc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        final EditText id = (EditText)findViewById(R.id.signup_id_textinput);
        final EditText pw = (EditText)findViewById(R.id.signup_password_textinput);
        final EditText name = (EditText)findViewById(R.id.signup_name_textinput);
        final EditText phone = (EditText)findViewById(R.id.signup_mobile_number_textinput);
        final RadioButton customer = (RadioButton)findViewById(R.id.signup_customer_radioButton);
        final RadioButton owner = (RadioButton)findViewById(R.id.signup_owner_radioButton);


        // 회원가입 버튼 클릭 리스너
        Button btnCustomer = (Button) findViewById(R.id.signup_signup_button);
        btnCustomer.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Open the connection
                            URL url = new URL("https://api.bistroad.kr/v1/users");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            JSONObject jsonInfo = new JSONObject();
                            jsonInfo.accumulate("username", id.getText().toString());
                            jsonInfo.accumulate("password", pw.getText().toString());
                            jsonInfo.accumulate("fullName", name.getText().toString());
                            jsonInfo.accumulate("phone", phone.getText().toString());
                            if(customer.isChecked())
                                jsonInfo.accumulate("role", "ROLE_USER");
                            else if(owner.isChecked())
                                jsonInfo.accumulate("role", "ROLE_STORE_OWNER");

                            userInfo = jsonInfo.toString();
                            conn.setRequestProperty("Accept", "application/json");
                            conn.setRequestProperty("Content-type", "application/json");

                            conn.setRequestMethod("POST");
                            conn.setDefaultUseCaches(false);
                            conn.setDoInput(true);
                            conn.setDoOutput(true);

                            OutputStream os = conn.getOutputStream();

                            os.write(userInfo.getBytes("euc-kr"));

                            os.flush();
                            if(conn.getResponseCode() == 200){
                                InputStream is = conn.getInputStream();
                                Log.d("POST", convertStreamToString(is));
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
                    }
                });

                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
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
        });
    }
}
