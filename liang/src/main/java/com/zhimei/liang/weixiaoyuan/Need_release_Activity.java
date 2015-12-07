package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhimei.liang.utitls.DemandObject;
import com.zhimei.liang.utitls.FileHelper;
import com.zhimei.liang.utitls.MyApplication;

import cn.bmob.v3.listener.SaveListener;

public class Need_release_Activity extends Activity {
    private EditText et_name;
    private EditText et_description;
    private Spinner type;
    private  String[] planets;
    private String catagary;//商品的种类
    private String name;
    private String descreption;
    private ImageButton ensure,back,index;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_release_);
        inintViews();
    }

    void inintViews(){

        back=(ImageButton)findViewById(R.id.need_back);
        index=(ImageButton)findViewById(R.id.need_index);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(Need_release_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        et_name=(EditText)findViewById(R.id.rel_two_getname);
        et_description=(EditText)findViewById(R.id.need_two_getdetail);
        type=(Spinner)findViewById(R.id.rel_spinner1);
        ensure=(ImageButton)findViewById(R.id.need_two_commit_ensure);

        //获取数组资源
        Resources res = getResources();
        planets = res.getStringArray(R.array.goods_catagory);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catagary = planets[position];
                Toast.makeText(Need_release_Activity.this, planets[position], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString().trim();
                descreption = et_description.getText().toString().trim();
                if (name.equals("")) {
                    Toast.makeText(Need_release_Activity.this, "亲，请输入名称", Toast.LENGTH_SHORT).show();
                } else if (descreption.equals("")) {
                    Toast.makeText(Need_release_Activity.this, "亲，请输入备注", Toast.LENGTH_SHORT).show();
                } else {
                    loadInformation();
                }

            }
        });

    }

    void loadInformation(){
        progressDialog= ProgressDialog.show(Need_release_Activity.this, "", "正在连接服务器中");
        DemandObject demandObject=new DemandObject();
        demandObject.setPublishMan(MyApplication.getCurrentName());
        demandObject.setGoodName(name);
        demandObject.setCategary(catagary);
        demandObject.setDescreption(descreption);
        demandObject.save(Need_release_Activity.this, new SaveListener() {
            /**
             * 发布无论成功与否，都要回到主界面
             */
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(Need_release_Activity.this, "需求发布成功", Toast.LENGTH_SHORT).show();
                new FileHelper().storeUpScore(Need_release_Activity.this, 3);
                finish();
                Intent intent=new Intent(Need_release_Activity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(Need_release_Activity.this, s, Toast.LENGTH_SHORT).show();
                finish();
                Intent intent=new Intent(Need_release_Activity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });


    }
}
