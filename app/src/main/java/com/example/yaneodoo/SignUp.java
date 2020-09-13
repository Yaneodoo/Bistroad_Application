package com.example.yaneodoo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yaneodoo.REST.RestPostUser;

import java.util.concurrent.ExecutionException;

public class SignUp extends AppCompatActivity {
    String role;
    String userInfo;
    int rc;

    @Override
    public void onBackPressed() {
        this.finish();
    }

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
                if(customer.isChecked())
                    role = "ROLE_USER";
                else if(owner.isChecked())
                    role = "ROLE_STORE_OWNER";
                try {
                    RestPostUser restPostUser = new RestPostUser(id.getText().toString(), pw.getText().toString(), name.getText().toString(), role, phone.getText().toString());
                    try {
                        rc = restPostUser.execute().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("Login rc", String.valueOf(rc));
                    if(rc == 201){
                        String signupSuccess = id.getText().toString() + "님 앞으로 맛있는 시간 되세요!";
                        Toast successToast = Toast.makeText(getApplicationContext(), signupSuccess, Toast.LENGTH_LONG);
                        successToast.show();
                        Intent intent = new Intent(SignUp.this, Login.class);
                        startActivity(intent);
                    }
                    else if(rc == 409){
                        Toast sameIdToast = Toast.makeText(getApplicationContext(), "동일한 아이디가 이미 존재합니다.", Toast.LENGTH_LONG);
                        sameIdToast.show();
                    }
                    else if(rc == 403){
                        Toast errorToast = Toast.makeText(getApplicationContext(), "Sign Up 403 error", Toast.LENGTH_LONG);
                        errorToast.show();
                    } else if (rc == 404) {
                        Toast errorToast = Toast.makeText(getApplicationContext(), "Sign Up 401 error.", Toast.LENGTH_LONG);
                        errorToast.show();
                    } else {
                        Log.e("POST", "Failed.");
                    }
                }
                catch (Exception e) {
                    // Error calling the rest api
                    Log.e("REST_API", "POST method failed: " + e.getMessage());
                    e.printStackTrace();
                }
//                AsyncTask.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            // Open the connection
//                            URL url = new URL("https://api.bistroad.kr/v1/users");
//                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//                            JSONObject jsonInfo = new JSONObject();
//                            jsonInfo.accumulate("username", id.getText().toString());
//                            jsonInfo.accumulate("password", pw.getText().toString());
//                            jsonInfo.accumulate("fullName", name.getText().toString());
//                            jsonInfo.accumulate("phone", phone.getText().toString());
//                            if(customer.isChecked())
//                                jsonInfo.accumulate("role", "ROLE_USER");
//                            else if(owner.isChecked())
//                                jsonInfo.accumulate("role", "ROLE_STORE_OWNER");
//
//                            userInfo = jsonInfo.toString();
//                            conn.setRequestProperty("Accept", "application/json");
//                            conn.setRequestProperty("Content-type", "application/json");
//
//                            conn.setRequestMethod("POST");
//                            conn.setDefaultUseCaches(false);
//                            conn.setDoInput(true);
//                            conn.setDoOutput(true);
//
//                            OutputStream os = conn.getOutputStream();
//
//                            os.write(userInfo.getBytes("euc-kr"));
//
//                            os.flush();
//                            if(conn.getResponseCode() == 200){
//                                InputStream is = conn.getInputStream();
//                                Log.d("POST", convertStreamToString(is));
//                            }
//                            else{
//                                Log.e("POST", "Failed.");
//                            }
//                        }
//                        catch (Exception e) {
//                            // Error calling the rest api
//                            Log.e("REST_API", "POST method failed: " + e.getMessage());
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                Intent intent = new Intent(SignUp.this, Login.class);
//                startActivity(intent);
            }
        });
    }
}
