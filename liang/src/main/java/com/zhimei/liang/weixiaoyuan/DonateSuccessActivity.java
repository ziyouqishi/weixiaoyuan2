package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhimei.liang.weixiaoyuan.R;

public class DonateSuccessActivity extends Activity {
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_success);
        initView();
    }
    void initView(){
        rl=(RelativeLayout)findViewById(R.id.don_succ_two_haha);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DonateSuccessActivity.this,"进入积分界面",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
