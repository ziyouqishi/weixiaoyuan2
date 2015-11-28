package com.zhimei.liang.fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhimei.liang.customview.RoundImageView;
import com.zhimei.liang.utitls.FileHelper;
import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.utitls.User;
import com.zhimei.liang.weixiaoyuan.AlterAddressActivity;
import com.zhimei.liang.weixiaoyuan.DemandCenterActivity;
import com.zhimei.liang.weixiaoyuan.DonationRecordActivity;
import com.zhimei.liang.weixiaoyuan.R;

import com.zhimei.liang.weixiaoyuan.LoginActivity;
import com.zhimei.liang.weixiaoyuan.ScoreActivity;
import com.zhimei.liang.weixiaoyuan.TradeRecordActivity;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class IndividulFragment extends Fragment {
    private  View view;
    private Button login;
    private RelativeLayout myScore;
    private RelativeLayout record;
    private RelativeLayout address;
    private RelativeLayout donationRecord;
    private RelativeLayout demandCenter;
    private String pictureUrl;
    private RoundImageView touxiang;
    private TextView name;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_person_center,container,false);
        initview();
        return view;
    }
    void initview(){
        touxiang=(RoundImageView)view.findViewById(R.id.per_iv_image);
        name=(TextView)view.findViewById(R.id.per_tv_name);
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
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        myScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isSignUPSuccess()){
                    Intent intent=new Intent(view.getContext(),ScoreActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
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
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
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
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
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
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
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
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                }
                else{
                    Toast.makeText(view.getContext(),"亲，请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    void query(String name){
        BmobQuery<User> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("username", MyApplication.getCurrentName());
        bmobQuery.findObjects(view.getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                pictureUrl=list.get(0).getPicture().getFileUrl(view.getContext());
                new LoadInformation().execute();

            }

            @Override
            public void onError(int i, String s) {
            }
        });

    }

    class LoadInformation extends AsyncTask<Void,Integer,Boolean>{
        Bitmap bitmap;
        @Override
        protected Boolean doInBackground(Void... params) {
             bitmap=new FileHelper().getHttpBitmap(pictureUrl);

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(bitmap!=null){
                touxiang.setImageBitmap(bitmap);
                name.setText(MyApplication.getCurrentName());

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    /**
     * 界面的每次启动都会调用此方法
     */
    @Override
    public void onResume() {
        super.onResume();

        if(MyApplication.getCurrentName()!=null){
            query(MyApplication.getCurrentName());
        }
    }
}