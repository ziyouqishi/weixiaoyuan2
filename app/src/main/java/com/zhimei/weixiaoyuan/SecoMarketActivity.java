package com.zhimei.weixiaoyuan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class SecoMarketActivity extends Activity {
    private ImageView back;
    private ImageView backhome;
    private GridView gv;
    private ArrayList<HashMap<String, Object>> item_list;
    private  SimpleAdapter type_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seco_market);
        initData();

        initView();
    }
    void initData(){

        item_list=new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("picture",R.mipmap.book);
        map1.put("name","书籍");
        item_list.add(map1);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("picture",R.mipmap.shouji);
        map2.put("name","手机");
        item_list.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("picture",R.mipmap.bike);
        map3.put("name","校园代步");
        item_list.add(map3);

        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("picture",R.mipmap.sports);
        map4.put("name","体育健身");
        item_list.add(map4);

        HashMap<String, Object> map5 = new HashMap<String, Object>();
        map5.put("picture",R.mipmap.compuer);
        map5.put("name","电脑");
        item_list.add(map5);

        HashMap<String, Object> map6 = new HashMap<String, Object>();
        map6.put("picture",R.mipmap.cloths);
        map6.put("name","衣服鞋帽");
        item_list.add(map6);

        HashMap<String, Object> map7 = new HashMap<String, Object>();
        map7.put("picture",R.mipmap.camara);
        map7.put("name","数码产品");
        item_list.add(map7);

        HashMap<String, Object> map8 = new HashMap<String, Object>();
        map8.put("picture",R.mipmap.others);
        map8.put("name","其他");
        item_list.add(map8);

        type_adapter = new SimpleAdapter(this,
                item_list,// 数据源
                R.layout.sec_item,// 显示布局
                new String[] { "picture", "name" },
                new int[] { R.id.sec_iv_item, R.id.sec_tv_item });



    }

    void initView(){
        gv=(GridView)findViewById(R.id.sec_gridview);
        back=(ImageView)findViewById(R.id.sec_back);
        backhome=(ImageView)findViewById(R.id.sec_index);

        gv.setAdapter(type_adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SecoMarketActivity.this,"根据不同item跳转到不同页面",Toast.LENGTH_SHORT).show();
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
                Intent intent=new Intent(SecoMarketActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });



    }

}
