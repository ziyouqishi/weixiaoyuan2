package com.zhimei.liang.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.zhimei.liang.utitls.FileHelper;
import com.zhimei.liang.utitls.MyAdapter;
import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.utitls.ShopGoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.zhimei.liang.weixiaoyuan.R;


public class LiveToolsFragment extends Fragment {
    private ArrayList<ShopGoods> al_goods;
    private ArrayList<HashMap<String,Object>> al_map;
    private ListView lv;
    private  View view;
    private ImageButton buy;
    private MyAdapter goods_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view=inflater.inflate(R.layout.fragment_live_tools, container, false);
        initView();
        return view;
    }

    void initView(){
        al_goods=new ArrayList<>();
        al_map=new ArrayList<>();
        lv=(ListView)view.findViewById(R.id.lv_live);
        buy=(ImageButton) view.findViewById(R.id.already_choosed);

        int test=R.drawable.goods;

       /* for(int i=0;i<15;i++){
            ShopGoods shopGoods=new ShopGoods(test,"营业中","已售34份","¥ 1.0","壁纸");
            al_goods.add(shopGoods);
        }*/

        ShopGoods shopGoods1=new ShopGoods(R.mipmap.live1,"已售完","已售341份","¥ 23","台灯");
        ShopGoods shopGoods2=new ShopGoods(R.mipmap.live2,"营业中","已售34份","¥ 43","手机架");
        ShopGoods shopGoods3=new ShopGoods(R.mipmap.live3,"已售完","已售121份","¥ 13","牙膏");
        al_goods.add(shopGoods1);
        al_goods.add(shopGoods2);
        al_goods.add(shopGoods3);

        for(int j=0;j<al_goods.size();j++){
            HashMap<String,Object> map=new HashMap<>();
            map.put("picture",al_goods.get(j).getPicture());
            map.put("name",al_goods.get(j).getName());
            map.put("state",al_goods.get(j).getState());
            map.put("price",al_goods.get(j).getPrice());
            map.put("sell",al_goods.get(j).getSell());
            al_map.add(map);
        }

        goods_adapter=new MyAdapter(view.getContext(),al_goods);
        goods_adapter.setOnAddButtonListener(new MyAdapter.OnAddButtonListener() {
            @Override
            public void onChange() {
                buy.setVisibility(View.VISIBLE);
            }
        });

        lv.setAdapter(goods_adapter);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Integer> set = goods_adapter.totalPriceMap.keySet();
                Iterator<Integer> it = set.iterator();
               float total=0;
                while (it.hasNext()) {
                    Float price=goods_adapter.totalPriceMap.get((Integer)it.next());
                    total+=price;
                }

                Toast.makeText(MyApplication.getContext(),total+"", Toast.LENGTH_SHORT).show();
                new BmobPay(getActivity()).pay(total, "商品总价", new PayListener() {
                    @Override
                    public void orderId(String s) {
                        Log.i("liang", s);

                    }

                    @Override
                    public void succeed() {
                        new FileHelper().storeUpScore(view.getContext(), 3);

                    }

                    @Override
                    public void fail(int i, String s) {

                    }

                    @Override
                    public void unknow() {

                    }
                });
            }
        });

        goods_adapter.setOnDecreaseListener(new MyAdapter.OnDecreaseListerer() {
            @Override
            public void disappear() {
                buy.setVisibility(View.INVISIBLE);
            }
        });

    }
}
