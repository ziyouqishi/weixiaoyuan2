package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhimei.liang.customview.CircleImageView;
import com.zhimei.liang.customview.RoundImageView;
import com.zhimei.liang.utitls.MyApplication;

import java.util.HashMap;

public class TradeWindowActivity extends Activity {
    private RoundImageView publishManPicture;
    private TextView et_publishManName;
    private TextView et_goodName;
    private TextView et_goodDescription;
    private TextView et_goodPrice;
    private TextView et_time;
    private ImageButton index,back;
    private RelativeLayout duanxin,phone,chat,zhifubao;
    private HashMap<String,Object> hashMap;
    private ImageView goodPicture;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_window);
        initViews();
    }

    void initViews(){
        hashMap= MyApplication.getHashmap();
        et_publishManName=(TextView)findViewById(R.id.publishman_name);
        et_goodName=(TextView)findViewById(R.id.goods_name);
        et_goodDescription=(TextView)findViewById(R.id.good_miaoshu);
        et_goodPrice=(TextView)findViewById(R.id.goods_price);
        et_time=(TextView)findViewById(R.id.publish_time);
        index=(ImageButton)findViewById(R.id.trade_window_succ_index);
        back=(ImageButton)findViewById(R.id.trade_window_succ_back);
        duanxin=(RelativeLayout)findViewById(R.id.all);
        phone=(RelativeLayout)findViewById(R.id.per_center);
        chat=(RelativeLayout)findViewById(R.id.trade_windowation);
        zhifubao=(RelativeLayout)findViewById(R.id.sec_market);
        goodPicture=(ImageView)findViewById(R.id.trade_goods_picture);
        publishManPicture=(RoundImageView)findViewById(R.id.publishman_touxiang);

        et_goodDescription.setText(hashMap.get("description").toString());
        if(hashMap.get("publishMan")==null){
            et_publishManName.setText("自由的心");
        }
        else{
            et_publishManName.setText(hashMap.get("publishMan").toString());
        }

        et_goodName.setText(hashMap.get("name").toString());
        et_goodPrice.setText(hashMap.get("price").toString());
        et_time.setText(hashMap.get("time").toString());
        goodPicture.setImageBitmap((Bitmap)hashMap.get("bitmap"));






    }

}
