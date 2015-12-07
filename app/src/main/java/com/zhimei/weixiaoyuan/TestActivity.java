package com.zhimei.weixiaoyuan;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class TestActivity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
       // initView();
    }
   /* void initView(){
        textView=(TextView)findViewById(R.id.text);

        SpannableString ss = new SpannableString("红色打电话斜体删除线绿色下划线图片:.");
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 2,
                //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 7,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, 18, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance()); //实现文本的滚动

    }*/

}
