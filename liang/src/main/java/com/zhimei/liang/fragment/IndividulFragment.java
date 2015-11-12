package com.zhimei.liang.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.weixiaoyuan.AlterAddressActivity;
import com.zhimei.liang.weixiaoyuan.DemandCenterActivity;
import com.zhimei.liang.weixiaoyuan.DonationRecordActivity;
import com.zhimei.liang.weixiaoyuan.R;

import com.zhimei.liang.weixiaoyuan.LoginActivity;
import com.zhimei.liang.weixiaoyuan.ScoreActivity;
import com.zhimei.liang.weixiaoyuan.TradeRecordActivity;

public class IndividulFragment extends Fragment {
    private  View view;
    private Button login;
    private RelativeLayout myScore;
    private RelativeLayout record;
    private RelativeLayout address;
    private RelativeLayout donationRecord;
    private RelativeLayout demandCenter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_person_center,container,false);
        initview();
        return view;
    }
    void initview(){
        login=(Button)view.findViewById(R.id.per_bt_login);
        myScore=(RelativeLayout)view.findViewById(R.id.per_score);
        record=(RelativeLayout)view.findViewById(R.id.per_tradedetail);
        address=(RelativeLayout)view.findViewById(R.id.per_modifyaddress);
        donationRecord=(RelativeLayout)view.findViewById(R.id.per_donatedetail);
        demandCenter=(RelativeLayout)view.findViewById(R.id.per_demand);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(view.getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        myScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isSignUPSuccess()){
                    Intent intent=new Intent(view.getContext(),ScoreActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(view.getContext(),"亲，请先登录",Toast.LENGTH_SHORT).show();
                }

            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isSignUPSuccess()){
                    Intent intent=new Intent(view.getContext(),TradeRecordActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(view.getContext(),"亲，请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isSignUPSuccess()){
                    Intent intent=new Intent(view.getContext(),AlterAddressActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(view.getContext(),"亲，请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });

        donationRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isSignUPSuccess()){
                    Intent intent = new Intent(view.getContext(), DonationRecordActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(view.getContext(),"亲，请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });

        demandCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isSignUPSuccess()){
                    Intent intent = new Intent(view.getContext(), DemandCenterActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(view.getContext(),"亲，请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}