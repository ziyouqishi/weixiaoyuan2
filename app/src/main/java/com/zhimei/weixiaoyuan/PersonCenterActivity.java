package com.zhimei.weixiaoyuan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class PersonCenterActivity extends Activity {
    private Button login;
    private ImageView back;
    private ImageView backhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        initview();
    }

    void initview(){
        login=(Button)findViewById(R.id.per_bt_login);
        back=(ImageView)findViewById(R.id.per_back);
        backhome=(ImageView)findViewById(R.id.per_index);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonCenterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

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
                Intent intent=new Intent(PersonCenterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
