package com.zhimei.liang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.zhimei.liang.utitls.SecHandAdapter;
import com.zhimei.liang.weixiaoyuan.Goods_release_Activity;
import com.zhimei.liang.weixiaoyuan.Need_release_Activity;
import com.zhimei.liang.weixiaoyuan.QueryGoodsActivity;
import com.zhimei.liang.weixiaoyuan.R;


import java.util.ArrayList;
import java.util.HashMap;


public class SecHandFragment extends Fragment {
    private View view;
    private GridView gv;
    private ArrayList<HashMap<String, Object>> item_list;
    private SimpleAdapter type_adapter;
    private SecHandAdapter adapter;
    private EditText search_content;
    private ImageButton search_ensdure;
    private Button fabuxinxi,fabuxuqiu;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_seco_market, container, false);
        initData();
        initView();
        return view;
    }

    void initView() {
        gv = (GridView) view.findViewById(R.id.sec_gridview);
        search_content=(EditText)view.findViewById(R.id.sec_two_frame_et);
        search_ensdure=(ImageButton)view.findViewById(R.id.sec_two_deletecontent);
        fabuxinxi=(Button)view.findViewById(R.id.sec_publish);
        fabuxuqiu=(Button)view.findViewById(R.id.sec_need);
        final String category[]={"书籍","手机","校园代步","体育健身","电脑","衣服鞋帽","数码产品","其他"};

       // gv.setAdapter(type_adapter);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(view.getContext(), QueryGoodsActivity.class);
                intent.putExtra("queryData", category[position]);
                intent.putExtra("way","category");
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        search_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_ensdure.setVisibility(View.VISIBLE);
            }
        });

        search_ensdure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(search_content.getText().toString())){
                    Toast.makeText(view.getContext(), "请输入你需要查询的数据", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(view.getContext(), "正在进行搜索", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(view.getContext(), QueryGoodsActivity.class);
                    intent.putExtra("queryData",search_content.getText().toString());
                    intent.putExtra("way","name");
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                }

            }
        });

        fabuxinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(view.getContext(), Goods_release_Activity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        fabuxuqiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(view.getContext(), Need_release_Activity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });



    }

    void initData(){

        item_list=new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("picture",R.mipmap.book);
        map1.put("name","书籍");
        item_list.add(map1);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("picture",R.mipmap.shouji);
        map2.put("name","手机");
        item_list.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("picture",R.mipmap.bike);
        map3.put("name","校园代步");
        item_list.add(map3);

        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("picture",R.mipmap.sports);
        map4.put("name","体育健身");
        item_list.add(map4);

        HashMap<String, Object> map5 = new HashMap<String, Object>();
        map5.put("picture",R.mipmap.compuer);
        map5.put("name","电脑");
        item_list.add(map5);

        HashMap<String, Object> map6 = new HashMap<String, Object>();
        map6.put("picture",R.mipmap.cloths);
        map6.put("name","衣服鞋帽");
        item_list.add(map6);

        HashMap<String, Object> map7 = new HashMap<String, Object>();
        map7.put("picture",R.mipmap.camara);
        map7.put("name","数码产品");
        item_list.add(map7);

        HashMap<String, Object> map8 = new HashMap<String, Object>();
        map8.put("picture",R.mipmap.others);
        map8.put("name","其他");
        item_list.add(map8);

        type_adapter = new SimpleAdapter(view.getContext(),
                item_list,// 数据源
                R.layout.sec_item,// 显示布局
                new String[] { "picture", "name" },
                new int[] { R.id.sec_iv_item, R.id.sec_tv_item });
        adapter=new SecHandAdapter(item_list,view.getContext());





    }
}