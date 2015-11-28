package com.zhimei.liang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhimei.liang.weixiaoyuan.FirstActivity;
import com.zhimei.liang.weixiaoyuan.MainActivity;
import com.zhimei.liang.weixiaoyuan.R;

/**
 * Created by 张佳亮 on 2015/11/26.
 */
public class GuideFragment4 extends Fragment {
    private View view;
    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_4, container, false);
        initView();
        return view;
    }
    void initView(){
        textView=(TextView)view.findViewById(R.id.tvInNew);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent intent=new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });
    }


}
