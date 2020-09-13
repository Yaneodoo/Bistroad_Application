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
        final EditText confirmPw = (EditText)findViewById(R.id.signup_password_confirm_textinput);
        final EditText name = (EditText)findViewById(R.id.signup_name_textinput);
        final EditText phone = (EditText)findViewById(R.id.signup_mobile_number_textinput);
        final RadioButton customer = (RadioButton)findViewById(R.id.signup_customer_radioButton);
        final RadioButton owner = (RadioButton)findViewById(R.id.signup_owner_radioButton);


        // 회원가입 버튼 클릭 리스너
        Button btnSignup = (Button) findViewById(R.id.signup_signup_button);
        btnSignup.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().length() == 0){
                    Toast noIdToast = Toast.makeText(getApplicationContext(), "이름을 입력해 주세요.", Toast.LENGTH_LONG);
                    noIdToast.show();
                }
                else if(id.getText().toString().length() == 0){
                    Toast noIdToast = Toast.makeText(getApplicationContext(), "ID를 입력해 주세요.", Toast.LENGTH_LONG);
                    noIdToast.show();
                }
                else if(pw.getText().toString().length() == 0){
                    Toast noIdToast = Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요.", Toast.LENGTH_LONG);
                    noIdToast.show();
                }
                else if(confirmPw.getText().toString().length() == 0){
                    Toast noIdToast = Toast.makeText(getApplicationContext(), "확인 비밀번호를 입력해 주세요.", Toast.LENGTH_LONG);
                    noIdToast.show();
                }
                else if(pw.getText().toString() != confirmPw.getText().toString()){
                    Toast noIdToast = Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG);
                    noIdToast.show();
                }
                else if(phone.getText().toString().length() == 0){
                    Toast noIdToast = Toast.makeText(getApplicationContext(), "전화번호를 입력해 주세요.", Toast.LENGTH_LONG);
                    noIdToast.show();
                }
                else {
                    if (customer.isChecked())
                        role = "ROLE_USER";
                    else if (owner.isChecked())
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
                        if (rc == 201) {
                            String signupSuccess = id.getText().toString() + "님 앞으로 맛있는 시간 되세요!";
                            Toast successToast = Toast.makeText(getApplicationContext(), signupSuccess, Toast.LENGTH_LONG);
                            successToast.show();
                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                        } else if (rc == 409) {
                            Toast sameIdToast = Toast.makeText(getApplicationContext(), "동일한 아이디가 이미 존재합니다.", Toast.LENGTH_LONG);
                            sameIdToast.show();
                        } else if (rc == 403) {
                            Toast errorToast = Toast.makeText(getApplicationContext(), "Sign Up 403 error", Toast.LENGTH_LONG);
                            errorToast.show();
                        } else if (rc == 404) {
                            Toast errorToast = Toast.makeText(getApplicationContext(), "Sign Up 401 error.", Toast.LENGTH_LONG);
                            errorToast.show();
                        } else {
                            Log.e("POST", "Failed.");
                        }
                        SignUp.this.finish();
                    } catch (Exception e) {
                        // Error calling the rest api
                        Log.e("REST_API", "POST method failed: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
