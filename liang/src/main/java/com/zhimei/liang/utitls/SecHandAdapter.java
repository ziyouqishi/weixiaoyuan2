package com.zhimei.liang.utitls;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhimei.liang.weixiaoyuan.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 张佳亮 on 2015/11/23.
 */
public class SecHandAdapter extends BaseAdapter {
    private ArrayList<HashMap<String,Object>> arrayList;
    private LayoutInflater mInflater;

    public SecHandAdapter(ArrayList<HashMap<String,Object>> arrayList,Context context) {
        super();
        this.arrayList=arrayList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        String color[]={"#8A2BE2","#EEB422","#EE7621","#00CD66","#1E90FF","#4F94CD","#00F5FF","#00CD00"};
        if(convertView==null){
            convertView = mInflater.inflate(R.layout.sec_item,null);
            convertView.setBackgroundColor(Color.parseColor(color[position]));
            //convertView.setBackgroundColor(Color.parseColor("#5CACEE"));
            holder=new ViewHolder();
            holder.name=(TextView) convertView.findViewById(R.id.sec_tv_item);
            holder.picture=(ImageView) convertView.findViewById(R.id.sec_iv_item);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.name.setText(arrayList.get(position).get("name").toString());
        holder.name.setTextColor(Color.parseColor("#FFFFFF"));
        holder.picture.setImageResource((int)arrayList.get(position).get("picture"));
        return convertView;
    }

    class ViewHolder{
        public ImageView picture;
        public TextView name;
        public RelativeLayout relativeLayout;

    }
}
