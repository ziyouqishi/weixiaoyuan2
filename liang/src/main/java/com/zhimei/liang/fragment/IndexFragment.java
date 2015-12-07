package com.zhimei.liang.fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.zhimei.liang.utitls.FileHelper;
import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.utitls.TradeRecord;
import com.zhimei.liang.weixiaoyuan.LoginActivity;
import com.zhimei.liang.weixiaoyuan.R;

import com.zhimei.liang.utitls.SecondHandGoods;
import com.zhimei.liang.weixiaoyuan.TradeWindowActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ThumbnailUrlListener;


public class IndexFragment extends Fragment {
    private View view;
    private GridView gridview;
    private int loopNum=0;//循环计数
    private ArrayList<SecondHandGoods> goods_list;
    private ArrayAdapter<String> adapter;
    private Animation animation;
    private  SimpleAdapter goods_adapter;
    private SwipeRefreshLayout swipeLayout;
    private BmobQuery<SecondHandGoods> query ;
    private final static int DATACHANGED=1;
    private   Bitmap bitmap;
    private boolean isRefreshFlag=false;
    private ProgressDialog progressDialog;
    private ArrayList<HashMap<String, Object>> item_list;
    private  ArrayList<HashMap<String,Object>> al;//存储网络上查询到的资源
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case DATACHANGED:
                    goods_adapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frgment_allgoode,container,false);
         initView();
        initreso();
        return view;
    }

    void initView(){
        progressDialog=new ProgressDialog(view.getContext());
        progressDialog.setMessage("正在加载数据，请稍后");
        progressDialog.setCancelable(true);
        progressDialog.show();
        judgeAndquery();
        gridview=(GridView)view.findViewById(R.id.gridview);


        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                if (isRefreshFlag) {
                    item_list.clear();
                    goods_adapter.notifyDataSetChanged();
                    isRefreshFlag = false;
                    judgeAndquery();

                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            swipeLayout.setRefreshing(false);
                            //进行数据更新
                        }
                    }, 1500);
                    Toast.makeText(view.getContext(), "正在加载数据，请稍后", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
    /**
     *资源初始化
     */
    void initreso(){
        goods_list=new ArrayList<SecondHandGoods>();
        item_list = new ArrayList<HashMap<String, Object>>();
        bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.nullpicture);
        animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.myanim);
        /**
         * 得到商品的图片
         */
        //  Bitmap picture= BitmapFactory.decodeResource(getResources(), R.drawable.goods);

        //模拟了六个商品
       /* int test=R.drawable.goods;
        SecondHandGoods good1=new SecondHandGoods(picture,"22.2","墙纸","2天前","这是我今年九月份才买的，只用了几天...");
        SecondHandGoods good2=new SecondHandGoods(picture,"312.2","墙纸2","5天前","这是我今年九月份才买的，只用了几天...");
        SecondHandGoods good3=new SecondHandGoods(picture,"34.2","墙纸3","6天前","这是我今年九月份才买的，只用了几天...");
        SecondHandGoods good4=new SecondHandGoods(picture,"52.2","墙纸4","1天前","这是我今年九月份才买的，只用了几天...");
        SecondHandGoods good5=new SecondHandGoods(picture,"62.2","墙纸5","3天前","这是我今年九月份才买的，只用了几天...");
        SecondHandGoods good6=new SecondHandGoods(picture,"72.2","墙纸6","4天前","这是我今年九月份才买的，只用了几天...");
        SecondHandGoods good7=new SecondHandGoods(picture,"32.2","墙纸7","2天前","这是我今年九月份才买的，只用了几天...");

        goods_list.add(good1);
        goods_list.add(good2);
        goods_list.add(good3);
        goods_list.add(good4);
        goods_list.add(good5);
        goods_list.add(good6);
        goods_list.add(good7);*/

       /* for(int i=0;i<goods_list.size();i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("bitmap",goods_list.get(i).getPhoto());
            map.put("price",goods_list.get(i).getPrice());
            map.put("time",goods_list.get(i).getVar_data());
            map.put("name",goods_list.get(i).getName());
            map.put("description",goods_list.get(i).getDescription());
            item_list.add(map);
        }*/

         goods_adapter = new SimpleAdapter(view.getContext(),
                item_list,// 数据源
                R.layout.item,// 显示布局
                new String[] { "bitmap", "price","time","name","description" },
                new int[] { R.id.goods_image, R.id.goods_price,R.id.goods_time,R.id.goods_name,R.id.goods_attention });

        gridview.setAdapter(goods_adapter);


        /**
         * 将Bitmap转化为ImageView在SimpleAdapter中进行显示，否则，无法进行正常显示。
         */
        goods_adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {

                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap((Bitmap) data);
                    return true;
                } else {
                    return false;
                }
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                // Toast.makeText(MainActivity.this,"item单击事件",Toast.LENGTH_SHORT).show();
                animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.myanim);
                ImageView iv = (ImageView) view.findViewById(R.id.goods_image);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        MyApplication.setHashmap(item_list.get(position));
                            Intent intent = new Intent(view.getContext(), TradeWindowActivity.class);
                            startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                if(MyApplication.isSignUPSuccess()){
                    if(item_list.get(position).get("bitmap").equals(bitmap)){
                        Toast.makeText(view.getContext(),"图片正在加载中，请稍后",Toast.LENGTH_SHORT).show();
                    }else{
                        iv.startAnimation(animation);
                    }

                }
                else{
                    Toast.makeText(view.getContext(),"亲，请登录",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                }



            }
        });

    }

    /**
     * 判断记录是否为空并且查询数据
     */

    void judgeAndquery(){
        query = new BmobQuery<SecondHandGoods>();
        al=new ArrayList<>();
        query.count(view.getContext(), SecondHandGoods.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (i > 0) {
                    querydata();
                } else {
                    Toast.makeText(view.getContext(), "当前还没有任何商品哦，赶紧去发布商品吧:-D", Toast.LENGTH_LONG).show();
                    swipeLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });


    }


    void querydata(){

        query.findObjects(view.getContext(), new FindListener<SecondHandGoods>() {
            @Override
            public void onSuccess(final List<SecondHandGoods> list) {
                for (SecondHandGoods secondHandGoods : list) {
                    final HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("name", secondHandGoods.getName());
                    map.put("price", secondHandGoods.getPrice());
                    map.put("time", dealTime(secondHandGoods.getCreatedAt()));
                    map.put("tradeway", secondHandGoods.getTradeWay());
                    map.put("description", secondHandGoods.getDescription());
                    map.put("publishMan", secondHandGoods.getPublishMan());
                    map.put("url", secondHandGoods.getPictureFile().getFileUrl(view.getContext()));
                    al.add(map);

                }
                progressDialog.dismiss();
                new Loadgoods().execute();


            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(view.getContext(), s, Toast.LENGTH_LONG).show();
                Log.i("liang", s + "   失败");
                swipeLayout.setRefreshing(false);
            }
        });


    }

    /**
     * 时间格式处理
     * @param time
     * @return
     */
    String dealTime(String time){
        String tm=time.substring(0,10);
        return tm;
    }


    class Loadgoods extends AsyncTask<Void,Integer,Boolean> {
        private ArrayList<TradeRecord>  arrayList=new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            swipeLayout.setRefreshing(false);
            new Thread(new Runnable() {
                /**
                 * 求出item_list从哪个位置上开始是新加载的数据
                 */
              //  int count=item_list.size()-al.size();


                @Override
                public void run() {
                    for(int i=0;i<item_list.size();i++){
                     Bitmap bitmaps=new FileHelper().getHttpBitmap(al.get(i).get("url").toString());
                        item_list.get(i).put("bitmap", bitmaps);
                        Message message=new Message();
                        message.what=DATACHANGED;
                        handler.sendMessage(message);
                    }
                    isRefreshFlag=true;
                }
            }).start();


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }



        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for(HashMap map:al){

                /**
                 * 每次得到数据，边将数据加载进simpleAdapter的数据院中
                 * 并用handler通知ListView数据更新
                 */

                HashMap<String,Object> maps=new HashMap<>();
                maps.put("bitmap",bitmap);
                maps.put("name", map.get("name").toString());
                maps.put("price","¥ "+map.get("price").toString());
                maps.put("time",map.get("time").toString());
                maps.put("description",map.get("description").toString());
                maps.put("publishMan",map.get("publishMan"));
                item_list.add(maps);
                Message message=new Message();
                message.what=DATACHANGED;
                handler.sendMessage(message);

            }
            return true;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       new FileHelper().recycle(bitmap);
    }


}
