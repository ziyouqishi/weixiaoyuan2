package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class FirstActivity extends Activity {
    private int  count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        isFirstUse();
    }

    boolean isFirstUse(){
        final SharedPreferences preferences;
        preferences = getSharedPreferences("use_count", MODE_PRIVATE);
        count = preferences.getInt("count", 0);
        if(count==0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(2000);
                        finish();
                        SharedPreferences.Editor editor = preferences.edit();
                        //存入数据
                        editor.putInt("count", ++count);
                        editor.commit();
                        Intent intent=new Intent(FirstActivity.this,GuideActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }
                    catch(Exception e){

                    }


                }
            }).start();
        }
        else{
            SharedPreferences.Editor editor = preferences.edit();
            //存入数据
            editor.putInt("count", ++count);
            editor.commit();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(2000);
                        finish();
                        Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                    }
                    catch (Exception e){

                    }

                }
            }).start();

        }

        return true;
    }


}
