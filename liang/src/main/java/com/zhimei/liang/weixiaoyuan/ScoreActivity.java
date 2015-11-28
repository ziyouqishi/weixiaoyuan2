package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhimei.liang.weixiaoyuan.R;

public class ScoreActivity extends Activity {
    private TextView score;
    private Button exchange;
    private ImageButton back,index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initView();
    }

    void initView(){
        back=(ImageButton)findViewById(R.id.score_back);
        index=(ImageButton)findViewById(R.id.score_index);

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
                Intent intent=new Intent(ScoreActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        score=(TextView)findViewById(R.id.score_tv2_shuzi);
        exchange=(Button)findViewById(R.id.exchange);
        score.setText(getScore()+"");
        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ScoreActivity.this,"你当前的积分不够",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 读取存储的分数值
     * @return
     */

    int  getScore(){

        SharedPreferences pref=getSharedPreferences("weixiaoyuan", MODE_PRIVATE);
        int score=pref.getInt("score",0);
        return  score;
    }


}
