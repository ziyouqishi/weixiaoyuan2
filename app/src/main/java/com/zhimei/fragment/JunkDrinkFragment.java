package com.zhimei.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.zhimei.utitls.MyAdapter;
import com.zhimei.utitls.MyApplication;
import com.zhimei.utitls.ShopGoods;
import com.zhimei.weixiaoyuan.R;

import java.util.ArrayList;
import java.util.HashMap;


public class JunkDrinkFragment extends Fragment {
    private ArrayList<ShopGoods> al_goods;
    private ArrayList<HashMap<String,Object>> al_map;
    private ListView lv;
    private  View view;
    private Button buy;

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
        buy=(Button) view.findViewById(R.id.already_choosed);

        int test=R.drawable.goods;

        for(int i=0;i<15;i++){
            ShopGoods shopGoods=new ShopGoods(test,"营业中","已售34份","¥ 43.8","壁纸");
            al_goods.add(shopGoods);
        }

        for(int j=0;j<al_goods.size();j++){
            HashMap<String,Object> map=new HashMap<>();
            map.put("picture",al_goods.get(j).getPicture());
            map.put("name",al_goods.get(j).getName());
            map.put("state",al_goods.get(j).getState());
            map.put("price",al_goods.get(j).getPrice());
            map.put("sell",al_goods.get(j).getSell());
            al_map.add(map);
        }


        MyAdapter goods_adapter=new MyAdapter(view.getContext(),al_goods);
        goods_adapter.setOnAddButtonListener(new MyAdapter.OnAddButtonListener() {
            @Override
            public void onChange() {
                buy.setVisibility(View.VISIBLE);
            }
        });

        goods_adapter.setOnDecreaseListener(new MyAdapter.OnDecreaseListerer() {
            @Override
            public void disappear() {
                buy.setVisibility(View.INVISIBLE);
            }
        });

        lv.setAdapter(goods_adapter);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContext(), "进入结算界面", Toast.LENGTH_SHORT).show();
            }
        });


    }






}
