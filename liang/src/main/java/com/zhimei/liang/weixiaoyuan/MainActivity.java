package com.zhimei.liang.weixiaoyuan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhimei.liang.fragment.*;
import com.zhimei.liang.utitls.SecondHandGoods;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private GridView gridview;

    private ArrayList<SecondHandGoods> goods_list;
    private RelativeLayout all;
    private RelativeLayout per_center;
    private RelativeLayout sec_market;
    private RelativeLayout donation;
    private RelativeLayout sch_marcket;
    private ImageButton index;
    private ImageButton supmarket;
    private ImageButton secondhand;
    private ImageButton loveing;
    private ImageButton center;
    private Fragment frg_index,frg_market,frg_hand,frg_donation,frg_individul;
    private TextView shangpin,xiaoyuanchaoshi,ershoushichang,aixinjuanzeng,gerenzhongxin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        gridview=(GridView)findViewById(R.id.gridview);
        initview();
        setSelect(0);
    }

   /* @Override
    protected void onRestart() {
        super.onRestart();
        gridview=(GridView)findViewById(R.id.gridview);
        initview();
        setSelect(0);
    }*/

    void initview(){
        index=(ImageButton)findViewById(R.id.all_icon);
        supmarket=(ImageButton)findViewById(R.id.sch_icon);
        secondhand=(ImageButton)findViewById(R.id.sec_icon);
        loveing=(ImageButton)findViewById(R.id.don_icon);
        center=(ImageButton)findViewById(R.id.per_icon);
        all=(RelativeLayout)findViewById(R.id.all);
        per_center=(RelativeLayout)findViewById(R.id.per_center);
        sec_market=(RelativeLayout)findViewById(R.id.sec_market);
        donation=(RelativeLayout)findViewById(R.id.donation);
        sch_marcket=(RelativeLayout)findViewById(R.id.sch_marcket);
        shangpin=(TextView)findViewById(R.id.shangpin);
        xiaoyuanchaoshi=(TextView)findViewById(R.id.xiaoyuanchaoshi);
        ershoushichang=(TextView)findViewById(R.id.ershoushichang);
        aixinjuanzeng=(TextView)findViewById(R.id.aixinjuanzneg);
        gerenzhongxin=(TextView)findViewById(R.id.gerenzhongxin);




        all.setOnClickListener(this);
        per_center.setOnClickListener(this);
        sec_market.setOnClickListener(this);
        donation.setOnClickListener(this);
        sch_marcket.setOnClickListener(this);




    }



    @Override
    public void onClick(View v) {
        resetImage();
        switch(v.getId()){
            case R.id.all:
              setSelect(0);
                break;
            case R.id.per_center:
                setSelect(1);
                break;
            case R.id.sec_market:
                setSelect(2);
                break;
            case R.id.donation:
                setSelect(3);
                break;
            case R.id.sch_marcket:
                setSelect(4);
                break;
        }

    }

    /**
     * 点击的时候切换图片
     */
    private void resetImage(){
        index.setBackgroundResource(R.mipmap.all);
        supmarket.setBackgroundResource(R.mipmap.chaoshi);
        secondhand.setBackgroundResource(R.mipmap.second);
        loveing.setBackgroundResource(R.mipmap.aixin);
        center.setBackgroundResource(R.mipmap.touxiang);
        shangpin.setTextColor(0xff4F4F4F);
        xiaoyuanchaoshi.setTextColor(0xff4F4F4F);
        ershoushichang.setTextColor(0xff4F4F4F);
        aixinjuanzeng.setTextColor(0xff4F4F4F);
        gerenzhongxin.setTextColor(0xff4F4F4F);

    }

    private void setSelect(int i){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        hideAFrament(transaction);
        //将图片切换为亮色
        //并且设置内容
        switch(i){
            case 0:
                if(frg_index==null){
                    frg_index=new IndexFragment();
                    transaction.add(R.id.content,frg_index);
                }
                else{
                    transaction.show(frg_index);
                }
               index.setBackgroundResource(R.mipmap.all_press);
                shangpin.setTextColor(0xff32CD32);
                break;
            case 1:
                if(frg_market==null){
                    frg_market=new SupernarketFragment();
                    transaction.add(R.id.content,frg_market);
                }
                else{
                    transaction.show(frg_market);
                }
                supmarket .setBackgroundResource(R.mipmap.gouwu_press);
                xiaoyuanchaoshi.setTextColor(0xff32CD32);
                break;
            case 2:
                if(frg_hand==null){
                    frg_hand=new SecHandFragment();
                    transaction.add(R.id.content,frg_hand);
                }
                else{
                    transaction.show(frg_hand);
                }
                secondhand.setBackgroundResource(R.mipmap.ershou_press);
                ershoushichang.setTextColor(0xff32CD32);
                break;
            case 3:
                if(frg_donation==null){
                    frg_donation=new DonationFragment();
                    transaction.add(R.id.content,frg_donation);
                }
                else{
                    transaction.show(frg_donation);
                }
                loveing.setBackgroundResource(R.mipmap.aixin_press);
                aixinjuanzeng.setTextColor(0xff32CD32);
                break;
            case 4:
                if(frg_individul==null){
                    frg_individul=new IndividulFragment();
                    transaction.add(R.id.content,frg_individul);
                }
                else{
                    transaction.show(frg_individul);
                }
                center.setBackgroundResource(R.mipmap.geren_press);
                gerenzhongxin.setTextColor(0xff32CD32);
                break;

        }
        transaction.commit();
    }

    private void hideAFrament(FragmentTransaction transaction){

        if(frg_index!=null){
            transaction.hide(frg_index);
        }
        if(frg_market!=null){
            transaction.hide(frg_market);
        }
        if(frg_hand!=null){
            transaction.hide(frg_hand);
        }
        if(frg_donation!=null){
            transaction.hide(frg_donation);
        }
        if(frg_individul!=null){
            transaction.hide(frg_individul);
        }
    }


    /**
     *
     * @param fragment
     * 解决 tab中的fragment重叠问题
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        super.onAttachFragment(fragment);

        if (frg_index == null && fragment instanceof IndexFragment) {
            frg_index = (IndexFragment)fragment;
        }else if (frg_market == null && fragment instanceof SupernarketFragment) {
            frg_market = (SupernarketFragment)fragment;
        }else if (frg_hand == null && fragment instanceof SecHandFragment) {
            frg_hand = (SecHandFragment)fragment;
        }else if(frg_donation == null && fragment instanceof DonationFragment){
            frg_donation = (DonationFragment)fragment;
        }else if(frg_individul == null && fragment instanceof IndividulFragment){
            frg_individul = (IndividulFragment)fragment;
        }
    }

}
