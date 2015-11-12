package com.zhimei.weixiaoyuan;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


public class NetTestActivity extends Activity {
    private TextView content;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_test);
        initView();
    }
    void initView(){
        button=(Button)findViewById(R.id.net_button);
        content=(TextView)findViewById(R.id.net_content);
    }
    void sendRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    HttpClient httpClient=new DefaultHttpClient();
                    HttpPost httpPost=new HttpPost("http://210.33.40.130/default2.aspx");
                   // List
                }
                catch (Exception e){

                }

            }
        }).start();
    }
}
