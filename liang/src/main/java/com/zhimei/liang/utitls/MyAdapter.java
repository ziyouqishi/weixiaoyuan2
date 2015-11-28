package com.zhimei.liang.utitls;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhimei.liang.weixiaoyuan.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 张佳亮 on 2015/10/13.
 */
public class MyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private View view;
    private ImageButton buy;
    private ArrayList<ShopGoods> al_goods;
    private int num;
    public HashMap<Integer,Float> totalPriceMap;



    public MyAdapter(Context context,ArrayList al) {
        super();
        al_goods=al;
        this.mInflater = LayoutInflater.from(context);
        totalPriceMap=new HashMap<>();
       view=LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.fragment_live_tools, null);
    }

    @Override
    public int getCount() {
        return al_goods.size();
    }

    @Override
    public Object getItem(int position) {
        return al_goods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        String color[]={"#836FFF","#8968CD","#76EE00","#7B68EE","#551A8B"};

        buy=(ImageButton)view.findViewById(R.id.already_choosed);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.shopitem,null);
           // convertView.setBackgroundColor(Color.parseColor(color[position]));
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.name = (TextView) convertView.findViewById(R.id.shop_good_name);
            holder.price = (TextView) convertView.findViewById(R.id.shop_good_price);
            holder.state = (TextView) convertView.findViewById(R.id.shop_good_state);
            holder.sell = (TextView) convertView.findViewById(R.id.shop_good_sellnum);
            holder.picture=(ImageView) convertView.findViewById(R.id.shop_good_picture);
            holder.add=(ImageView) convertView.findViewById(R.id.shop_num_jia);
            holder.decrease=(ImageView) convertView.findViewById(R.id.shop_num_jian);
            holder.num=(TextView) convertView.findViewById(R.id.shop_num);
            convertView.setTag(holder);//绑定ViewHolder对象
        }
        else{
            holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
        }
        holder.name.setText(al_goods.get(position).getName());
        holder.price.setText(al_goods.get(position).getPrice());
        holder.sell.setText(al_goods.get(position).getSell());
        holder.state.setText(al_goods.get(position).getState());
        holder.picture.setImageResource(al_goods.get(position).getPicture());
        holder.num.setText(al_goods.get(position).getBuynum() + "");
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //此时onAddButtonListener的值为LearnToolsFragment中set方法参数中的对象，并且该类重写了onChange（）方法。
                onAddButtonListener.onChange();
                num = al_goods.get(position).getBuynum();
                ++num;
                al_goods.get(position).setBuynum(num);
                holder.num.setText(al_goods.get(position).getBuynum() + "");
                totalPriceMap.put(position,num*Float.parseFloat(al_goods.get(position).getPrice().substring(2)));
            }
        });

        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /**
                 * 判断所有的Item的商品数量都为0，全部为0，则隐藏购买的按钮。
                 */
                boolean flagExist=true;

                for(ShopGoods shopGoods:al_goods){
                    if(shopGoods.getBuynum()>=2){
                        flagExist=false;
                        break;
                    }
                }

                if(flagExist){
                    onDecreaseListerer.disappear();
                }

                num = al_goods.get(position).getBuynum();
                if(num>0){
                    --num;
                    al_goods.get(position).setBuynum(num);
                    holder.num.setText(al_goods.get(position).getBuynum() + "");
                    totalPriceMap.put(position, num * Float.parseFloat(al_goods.get(position).getPrice().substring(2)));
                }
            }
        });


        return convertView;
    }

    class ViewHolder{
        public TextView name;
        public TextView price;
        public TextView state;
        public TextView sell;
        public ImageView picture;
        public ImageView add;
        public ImageView decrease;
        public TextView num;

    }


    //按下加的按钮

    public OnAddButtonListener onAddButtonListener;

    public interface OnAddButtonListener{
         void onChange();
    }

    public  void setOnAddButtonListener(OnAddButtonListener onAddButtonListener){
       this.onAddButtonListener = onAddButtonListener;
    }


  //按下减的按钮

    public interface OnDecreaseListerer{
         void disappear();
    }

    public OnDecreaseListerer onDecreaseListerer;

    public void setOnDecreaseListener(OnDecreaseListerer onDecreaseListener){
        this.onDecreaseListerer=onDecreaseListener;
    }
}
