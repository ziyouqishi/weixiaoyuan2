package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhimei.liang.utitls.MyApplication;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends Activity {
    private ImageView back;
    private ImageView backhome;
    private TextView register;
    private Button login;
    private EditText nameEt,passwordEt;
    private String name,password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
    }

    void initview(){
        back=(ImageView)findViewById(R.id.log_back);
        backhome=(ImageView)findViewById(R.id.log_index);
        nameEt=(EditText)findViewById(R.id.log_et_ID);
        passwordEt=(EditText)findViewById(R.id.log_et_pass);
        register=(TextView)findViewById(R.id.log_tv_rigister);
        login=(Button)findViewById(R.id.log_ensure_bu);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameEt.getText().toString().trim();
                password = passwordEt.getText().toString().trim();
                if (name.equals("")) {
                    Toast.makeText(LoginActivity.this, "亲，请填写昵称", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, "亲，请填写密码", Toast.LENGTH_SHORT).show();
                } else {
                    executeLogin();
                }
            }
        });

    }

    void executeLogin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final BmobUser bmobUser = new BmobUser();

                bmobUser.setUsername(name);
                bmobUser.setPassword(password);

                bmobUser.login(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(LoginActivity.this, "亲，登录成功", Toast.LENGTH_SHORT).show();
                        MyApplication.setSignUPSuccess(true);
                        MyApplication.setCurrentName(name);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        MyApplication.setSignUPSuccess(false);
                        Toast.makeText(LoginActivity.this, "亲，登录失败，昵称或者密码错误", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).start();
    }

}
